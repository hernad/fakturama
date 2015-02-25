/* 
 * Fakturama - Free Invoicing Software - http://fakturama.sebulli.com
 * 
 * Copyright (C) 2012 Gerd Bartelt
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerd Bartelt - initial API and implementation
 */

package com.sebulli.fakturama.editors;

import static com.sebulli.fakturama.Translate._;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TreeSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.Workspace;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetProduct;
import com.sebulli.fakturama.data.DataSetVAT;
import com.sebulli.fakturama.data.UniData;
import com.sebulli.fakturama.data.UniDataSet;
import com.sebulli.fakturama.data.UniDataType;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.misc.DataUtils;
import com.sebulli.fakturama.views.datasettable.ViewProductTable;

/**
 * The product editor
 * 
 * @author Gerd Bartelt
 */
public class ProductEditor extends Editor {

	// Editor's ID
	public static final String ID = "com.sebulli.fakturama.editors.productEditor";

	// This UniDataSet represents the editor's input 
	private DataSetProduct product;

	// SWT widgets of the editor
	private Composite top;
	private Text textItemNr;
	private Text textName;
	private Text textDescription;
	private Combo comboVat;
	private Text textWeight;
	private Text textQuantity;
	private Text textQuantityUnit;
	private ComboViewer comboViewer;
	private Combo comboCategory;
	private Label labelProductPicture;
	private Composite photoComposite;
	private Text textProductPicturePath;

	// Widgets (and variables) for the scaled price.
	private Label[] labelBlock = new Label[5];
	private Text[] textBlock = new Text[5];
	private NetText[] netText = new NetText[5];
	private GrossText[] grossText = new GrossText[5];
	private UniData[] net = new UniData[5];
	private int scaledPrices;

	// These flags are set by the preference settings.
	// They define, if elements of the editor are displayed, or not.
	private boolean useWeight;
	private boolean useQuantity;
	private boolean useQuantityUnit;
	private boolean useItemNr;
	private boolean useNet;
	private boolean useGross;
	private boolean useVat;
	private boolean useDescription;
	private boolean usePicture;

	// These are (non visible) values of the document
	private Double vat = 0.0;
	private int vatId = 0;
	private String filename1 = "";
	private String filename2 = "";
	private String picturePath = "";
	private Display display;
	private String pictureName = "";

	// defines, if the product is new created
	private boolean newProduct;

	/**
	 * Constructor
	 * 
	 * Associate the table view with the editor
	 */
	public ProductEditor() {
		tableViewID = ViewProductTable.ID;
		editorID = "product";
	}

	/**
	 * Saves the contents of this part
	 * 
	 * @param monitor
	 *            Progress monitor
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {

		/*
		 * the following parameters are not saved:
		 * - id (constant) 
		 * - options (not yet implemented)
		 * - date_added (not modified by editor)
		 */

		if (newProduct) {
			// Check, if the item number is the next one
			int result = setNextNr(textItemNr.getText(), "itemnr", Data.INSTANCE.getProducts());

			// It's not the next free ID
			if (result == ERROR_NOT_NEXT_ID) {
				// Display an error message
				MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);

				//T: Title of the dialog that appears if the item/product number is not valid.
				messageBox.setText(_("Error in item number"));
				
				//T: Text of the dialog that appears if the item/product number is not valid.
				messageBox.setMessage(_("Item number is not the next free one:") + " " + textItemNr.getText() + "\n" +
						//T: Text of the dialog that appears if the number is not valid.
						_("See Preferences/Number Range."));
				messageBox.open();
			}

		}

		// Always set the editor's data set to "undeleted"
		product.setBooleanValueByKey("deleted", false);

		// Set the product data
		product.setStringValueByKey("itemnr", textItemNr.getText());
		product.setStringValueByKey("name", textName.getText());
		product.setStringValueByKey("category", comboCategory.getText());
		product.setStringValueByKey("description", DataUtils.removeCR(textDescription.getText()));
		product.setStringValueByKey("qunit", textQuantityUnit.getText());

		int i;
		Double lastScaledPrice = 0.0;

		// Set all of the scaled prices
		for (i = 0; i < scaledPrices; i++) {
			String indexNr = Integer.toString(i + 1);
			product.setDoubleValueByKey("price" + indexNr, lastScaledPrice = net[i].getValueAsDouble());
			product.setStringValueByKey("block" + indexNr, textBlock[i].getText());
		}
		
		for (; i < 5; i++) {
			String indexNr = Integer.toString(i + 1);
			product.setDoubleValueByKey("price" + indexNr, lastScaledPrice);
		}

		// Set the product data
		product.setIntValueByKey("vatid", vatId);
		product.setStringValueByKey("weight", textWeight.getText());
		product.setStringValueByKey("quantity", textQuantity.getText());
		product.setStringValueByKey("picturename", pictureName);

		// If it is a new product, add it to the product list and
		// to the data base
		if (newProduct) {
			product = Data.INSTANCE.getProducts().addNewDataSet(product);
			newProduct = false;

		}
		// If it's not new, update at least the data base
		else {
			Data.INSTANCE.getProducts().updateDataSet(product);
		}

		// Set the Editor's name to the product name.
		setPartName(product.getStringValueByKey("name"));

		// Refresh the table view of all contacts
		refreshView();
		checkDirty();

	}

	/**
	 * There is no saveAs function
	 */
	@Override
	public void doSaveAs() {
	}

	/**
	 * Initializes the editor. If an existing data set is opened, the local
	 * variable "product" is set to This data set. If the editor is opened to
	 * create a new one, a new data set is created and the local variable
	 * "product" is set to this one.
	 * 
	 * @param input
	 *            The editor's input
	 * @param site
	 *            The editor's site
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		// Set the site and the input
		setSite(site);
		setInput(input);

		// Set the editor's data set to the editor's input
		product = (DataSetProduct) ((UniDataSetEditorInput) input).getUniDataSet();

		// Test, if the editor is opened to create a new data set. This is,
		// if there is no input set.
		newProduct = (product == null);

		// If new ..
		if (newProduct) {

			// Create a new data set
			product = new DataSetProduct(((UniDataSetEditorInput) input).getCategory());
			
			//T: Header of product editor
			setPartName(_("New product"));

			// Set the vat to the standard value
			product.setIntValueByKey("vatid", Data.INSTANCE.getPropertyAsInt("standardvat"));

			// Get the next item number
			product.setStringValueByKey("itemnr", getNextNr());

		}
		else {

			// Set the Editor's name to the product name.
			setPartName(product.getStringValueByKey("name"));
		}
	}

	/**
	 * Returns whether the contents of this part have changed since the last
	 * save operation
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		/*
		 * the following parameters are not checked:
		 * - id (constant) 
		 * - options (not yet implemented)
		 * - date_added (not modified by editor)
		 */

		if (product.getBooleanValueByKey("deleted")) { return true; }
		if (newProduct) { return true; }

		if (!product.getStringValueByKey("itemnr").equals(textItemNr.getText())) { return true; }
		if (!product.getStringValueByKey("name").equals(textName.getText())) { return true; }
		if (!DataUtils.MultiLineStringsAreEqual(product.getStringValueByKey("description"),textDescription.getText())) { return true; }

		// Test all scaled prices
		for (int i = 0; i < scaledPrices; i++) {
			String indexNr = Integer.toString(i + 1);
			if (product.getDoubleValueByKey("price" + indexNr) != net[i].getValueAsDouble()) { return true; }
			if (!product.getStringValueByKey("block" + indexNr).equals(textBlock[i].getText())) { return true; }
		}

		if (product.getIntValueByKey("vatid") != vatId) { return true; }
		if (!DataUtils.DoublesAreEqual(product.getStringValueByKey("weight"),textWeight.getText())) { return true; }
		if (!DataUtils.DoublesAreEqual(product.getStringValueByKey("quantity"),textQuantity.getText())) { return true; }
		if (!product.getStringValueByKey("category").equals(comboCategory.getText())) { return true; }
		if (!product.getStringValueByKey("picturename").equals(pictureName)) { return true; }
		if (!product.getStringValueByKey("qunit").equals(textQuantityUnit.getText())) { return true; }

		
		return false;
	}

	/**
	 * Returns whether the "Save As" operation is supported by this part.
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 * @return False, SaveAs is not allowed
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * Set the variable picturePath to the path of the product picture, which is
	 * a combination of the selected workspace, the /pics/products/ folder and
	 * the product name.
	 * 
	 * Also update the text widget textProductPicturePath which is displayed
	 * under the product picture.
	 */
	private void createPicturePathFromPictureName() {

		// Get the workspace
		filename1 = Workspace.INSTANCE.getWorkspace();

		// add the picture sub folder
		filename2 = Workspace.productPictureFolderName;

		// Set the variables
		picturePath = filename1 + filename2;
		filename2 += pictureName;

		// Display the text under the product picture
		if (textProductPicturePath != null) {
			textProductPicturePath.setText(filename2);
		}
	}

	/**
	 * Create the picture name based on the product's item number
	 */
	private void createPictureName() {

		pictureName = createPictureName(textName.getText(), textItemNr.getText());

		// Add the full path.
		createPicturePathFromPictureName();
	}

	/**
	 * Create the picture name based on the product's item number Remove illegal
	 * characters and add an ".jpg"
	 * 
	 * @param name
	 *            The name of the product
	 * @param itemNr
	 *            The item number of the product
	 * @return Picture name as String
	 */
	public static String createPictureName(String name, String itemNr) {

		String pictureName;

		// Get the product's item number
		pictureName = itemNr;

		// If the product name is different to the item number,
		// add also the product name to the pictures name
		if (!name.equals(itemNr))
			pictureName += "_" + name;

		// Remove all illegal characters that are not allowed as file name.
		final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':', ' ', '.' };
		for (char c : ILLEGAL_CHARACTERS)
			pictureName = pictureName.replace(c, '_');

		// Add the .*jpg
		pictureName += ".jpg";

		return pictureName;
	}

	/**
	 * Reload the product picture
	 */
	private void setPicture() {

		try {
			// Display the picture, if a product picture is set.
			if (!pictureName.isEmpty()) {

				// Load the image, based on the picture name
				Image image = new Image(display, filename1 + filename2);

				// Get the pictures size
				int width = image.getBounds().width;
				int height = image.getBounds().height;

				// Maximum picture width is 250px
				if (width > 250) {
					height = 250 * height / width;
					width = 250;
				}

				// Rescale the picture to maximum 250px width
				Image scaledImage = new Image(display, image.getImageData().scaledTo(width, height));
				labelProductPicture.setImage(scaledImage);
			}
			// Display an empty background, if no picture is set.
			else {
				try {
					labelProductPicture.setImage((Activator.getImageDescriptor("/icons/product/nopicture.png").createImage()));
				}
				catch (Exception e1) {
					Logger.logError(e1, "Icon not found");
				}
			}
		}
		catch (Exception e) {

			// Show an error icon, if the picture is not found
			try {
				labelProductPicture.setImage((Activator.getImageDescriptor("/icons/product/picturenotfound.png").createImage()));
			}
			catch (Exception e1) {
				Logger.logError(e1, "Icon not found");
			}
		}

	}

	/**
	 * Creates the SWT controls for this workbench part
	 * 
	 * @param the
	 *            parent control
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {

		// Get a reference to the display
		display = parent.getDisplay();

		// Some of this editos's control elements can be hidden.
		// Get the these settings from the preference store
		useItemNr = Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_ITEMNR");
		useDescription = Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_DESCRIPTION");
		scaledPrices = Activator.getDefault().getPreferenceStore().getInt("PRODUCT_SCALED_PRICES");
		useWeight = Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_WEIGHT");
		useNet = (Activator.getDefault().getPreferenceStore().getInt("PRODUCT_USE_NET_GROSS") != 2);
		useGross = (Activator.getDefault().getPreferenceStore().getInt("PRODUCT_USE_NET_GROSS") != 1);
		useVat = Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_VAT");
		usePicture = Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_PICTURE");
		useQuantity = Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_QUANTITY");
		useQuantityUnit = Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_QUNIT");
		
		// Get the product VAT
		vatId = product.getIntValueByKey("vatid");
		try {
			vat = Data.INSTANCE.getVATs().getDatasetById(vatId).getDoubleValueByKey("value");
		}
		catch (IndexOutOfBoundsException e) {
			vat = 0.0;
		}

		// Create the top Composite
		top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(top);

		// Create an invisible container for all hidden components
		Composite invisible = new Composite(top, SWT.NONE);
		invisible.setVisible(false);
		GridDataFactory.fillDefaults().hint(0, 0).span(2, 1).applyTo(invisible);

		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(top, ContextHelpConstants.PRODUCT_EDITOR);
		
		// Group: Product description
		Group productDescGroup = new Group(top, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(productDescGroup);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(productDescGroup);

		productDescGroup.setText(_("Description"));

		// Item number
		Label labelItemNr = new Label(useItemNr ? productDescGroup : invisible, SWT.NONE);
		labelItemNr.setText(_("Item Number"));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelItemNr);
		textItemNr = new Text(useItemNr ? productDescGroup : invisible, SWT.BORDER);
		textItemNr.setText(product.getStringValueByKey("itemnr"));
		superviceControl(textItemNr, 64);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textItemNr);

		// Product name
		Label labelName = new Label(productDescGroup, SWT.NONE);
		labelName.setText(_("Name"));
		//T: Tool Tip Text
		labelName.setToolTipText(_("Name of the product. This is used for the items in the document."));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelName);
		textName = new Text(productDescGroup, SWT.BORDER);
		textName.setText(product.getStringValueByKey("name"));
		textName.setToolTipText(labelName.getToolTipText());
		superviceControl(textName, 64);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textName);

		// Product category
		Label labelCategory = new Label(productDescGroup, SWT.NONE);
		labelCategory.setText(_("Category"));
		//T: Tool Tip Text
		labelCategory.setToolTipText(_("You can set a category to classify the products. This is also the web shop category."));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelCategory);

		comboCategory = new Combo(productDescGroup, SWT.BORDER);
		comboCategory.setText(product.getStringValueByKey("category"));
		comboCategory.setToolTipText(labelCategory.getToolTipText());
		superviceControl(comboCategory);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboCategory);

		// Collect all category strings
		TreeSet<String> categories = new TreeSet<String>();
		categories.addAll(Data.INSTANCE.getProducts().getCategoryStrings());

		// Add all category strings to the combo
		for (Object category : categories) {
			comboCategory.add(category.toString());
		}

		
		
		

		// Product description
		Label labelDescription = new Label(useDescription ? productDescGroup : invisible, SWT.NONE);
		labelDescription.setText(_("Description"));
		//T: Tool Tip Text
		labelDescription.setToolTipText(_("Additional description. Can be used for the item. This can be selected in preferences/documents."));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelDescription);
		textDescription = new Text(useDescription ? productDescGroup : invisible, SWT.BORDER | SWT.MULTI);
		textDescription.setText(DataUtils.makeOSLineFeeds(product.getStringValueByKey("description")));
		textDescription.setToolTipText(labelDescription.getToolTipText());
		superviceControl(textDescription, 250);
		GridDataFactory.fillDefaults().hint(10, 80).grab(true, false).applyTo(textDescription);

		// Product quantity
		Label labelQuantityUnit = new Label(useQuantityUnit ? productDescGroup : invisible, SWT.NONE);
		//T: Product Editor - Label Product quantity unit
		labelQuantityUnit.setText(_("Quantity unit"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelQuantityUnit);
		textQuantityUnit = new Text(useQuantityUnit ? productDescGroup : invisible, SWT.BORDER);
		textQuantityUnit.setText(product.getFormatedStringValueByKey("qunit"));
		superviceControl(textQuantityUnit, 16);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textQuantityUnit);

		
		// Product price
		Label labelPrice = new Label(productDescGroup, SWT.NONE);

		// Use net or gross price
		if (useNet && useGross)
			//T: Label in the product editor
			labelPrice.setText(_("Price"));
		else if (useNet)
			//T: Label in the product editor
			labelPrice.setText(_("Price (net)"));
		else if (useGross)
			//T: Label in the product editor
			labelPrice.setText(_("Price (gross)"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelPrice);

		// Create a container composite for the scaled price
		Composite pricetable = new Composite(productDescGroup, SWT.NONE);
		GridLayoutFactory.swtDefaults().margins(0, 0).numColumns((scaledPrices > 1) ? (useNet && useGross) ? 4 : 3 : 2).applyTo(pricetable);

		// If there is a net and gross column, and 2 columns for the quantity
		// there are 2 cells in the top left corner, that are empty
		if ((scaledPrices >= 2) && useNet && useGross) {
			new Label(pricetable, SWT.NONE);
			new Label(pricetable, SWT.NONE);
		}

		// Display the heading for the net and gross columns
		if (useNet && useGross) {
			Label labelNet = new Label(pricetable, SWT.CENTER);
			labelNet.setText(_("Net"));
			Label labelGross = new Label(pricetable, SWT.CENTER);
			labelGross.setText(_("Gross"));
		}

		// Create a row for each entry of the scaled price table
		for (int i = 0; i < 5; i++) {

			String indexNr = Integer.toString(i + 1);

			// Get the net price scaled price
			net[i] = new UniData(UniDataType.STRING, product.getDoubleValueByKey("price" + indexNr));

			// Create the columns for the quantity
			labelBlock[i] = new Label(((i < scaledPrices) && (scaledPrices >= 2)) ? pricetable : invisible, SWT.NONE);
			//T: Product Editor - Label Scaled Prices "from" .. Quantity the price is ..
			labelBlock[i].setText(_("from", "QUANTITY"));

			textBlock[i] = new Text(((i < scaledPrices) && (scaledPrices >= 2)) ? pricetable : invisible, SWT.BORDER | SWT.RIGHT);
			textBlock[i].setText(product.getFormatedStringValueByKey("block" + indexNr));
			superviceControl(textBlock[i], 6);
			GridDataFactory.swtDefaults().hint(40, SWT.DEFAULT).applyTo(textBlock[i]);

			// Create the net columns
			if (useNet) {
				netText[i] = new NetText(this, (i < scaledPrices) ? pricetable : invisible, SWT.BORDER | SWT.RIGHT, net[i], vat);
				GridDataFactory.swtDefaults().hint(80, SWT.DEFAULT).applyTo(netText[i].getNetText());
			}

			// Create the gross columns
			if (useGross) {
				grossText[i] = new GrossText(this, (i < scaledPrices) ? pricetable : invisible, SWT.BORDER | SWT.RIGHT, net[i], vat);
				GridDataFactory.swtDefaults().hint(80, SWT.DEFAULT).applyTo(grossText[i].getGrossText());
			}

			// If a net and gross column was created, link both together,
			// so, if one is modified, the other will be recalculated.
			if (useNet && useGross) {
				netText[i].setGrossText(grossText[i].getGrossText());
				grossText[i].setNetText(netText[i].getNetText());
			}
		}

		// Set the tab order
		if (scaledPrices >= 2)
			setTabOrder(textDescription, textBlock[0]);
		else if (useNet)
			setTabOrder(textDescription, netText[0].getNetText());
		else
			setTabOrder(textDescription, grossText[0].getGrossText());

		// product VAT
		Label labelVat = new Label(useVat ? productDescGroup : invisible, SWT.NONE);
		labelVat.setText(_("VAT"));
		//T: Tool Tip Text
		labelVat.setToolTipText(_("Tax rate of the product"));
		
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelVat);
		comboVat = new Combo(useVat ? productDescGroup : invisible, SWT.BORDER);
		comboVat.setToolTipText(labelVat.getToolTipText());
		comboViewer = new ComboViewer(comboVat);
		comboViewer.setContentProvider(new UniDataSetContentProvider());
		comboViewer.setLabelProvider(new UniDataSetLabelProvider());
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {

				// Handle selection changed event 
				ISelection selection = event.getSelection();
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				if (!structuredSelection.isEmpty()) {

					// Get the first element ...
					Object firstElement = structuredSelection.getFirstElement();

					// Get the selected VAT
					UniDataSet selectedVat = (UniDataSet) firstElement;

					// Store the old value
					Double oldVat = vat;

					// Get the new value
					vatId = selectedVat.getIntValueByKey("id");
					vat = selectedVat.getDoubleValueByKey("value");

					// Recalculate all the price values
					for (int i = 0; i < scaledPrices; i++) {

						// Recalculate the price values if gross is selected,
						// So the gross value will stay constant.
						if (!useNet) {
							net[i].setValue(net[i].getValueAsDouble() * ((1 + oldVat) / (1 + vat)));
						}

						// Update net and gross text widget
						if (netText[i] != null)
							netText[i].setVatValue(vat);
						if (grossText[i] != null)
							grossText[i].setVatValue(vat);
					}
				}

				// Check, if the document has changed.
				checkDirty();
			}
		});

		// Create a JFace combo viewer for the VAT list
		comboViewer.setInput(Data.INSTANCE.getVATs().getActiveDatasetsPrefereCategory(DataSetVAT.getSalesTaxString()));
		try {
			comboViewer.setSelection(new StructuredSelection(Data.INSTANCE.getVATs().getDatasetById(vatId)), true);
		}
		catch (IndexOutOfBoundsException e) {
			comboVat.setText("invalid");
			vatId = -1;
		}
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboVat);

		// Product weight
		Label labelWeight = new Label(useWeight ? productDescGroup : invisible, SWT.NONE);
		//T: Product Editor - Label Product Weight with unit (kg)
		labelWeight.setText(_("Weight (kg)"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelWeight);
		textWeight = new Text(useWeight ? productDescGroup : invisible, SWT.BORDER);
		textWeight.setText(product.getStringValueByKey("weight"));
		superviceControl(textWeight, 16);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textWeight);

		// Product quantity
		Label labelQuantity = new Label(useQuantity ? productDescGroup : invisible, SWT.NONE);
		//T: Product Editor - Label Product quantity
		labelQuantity.setText(_("Quantity"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelQuantity);
		textQuantity = new Text(useQuantity ? productDescGroup : invisible, SWT.BORDER);
		textQuantity.setText(product.getFormatedStringValueByKey("quantity"));
		superviceControl(textQuantity, 16);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textQuantity);

		// Group: Product picture
		Group productPictureGroup = new Group(usePicture ? top : invisible, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(productPictureGroup);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(productPictureGroup);
		productPictureGroup.setText(_("Product Picture"));

		// The photo
		photoComposite = new Composite(productPictureGroup, SWT.BORDER);
		GridLayoutFactory.swtDefaults().margins(10, 10).numColumns(1).applyTo(photoComposite);
		GridDataFactory.fillDefaults().indent(0, 10).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(photoComposite);
		photoComposite.setBackground(new Color(null, 255, 255, 255));

		// The picture name label
		labelProductPicture = new Label(photoComposite, SWT.NONE);
		pictureName = product.getStringValueByKey("picturename");
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(labelProductPicture);

		// The picture path
		textProductPicturePath = new Text(photoComposite, SWT.NONE);
		textProductPicturePath.setEditable(false);
		textProductPicturePath.setBackground(new Color(null, 255, 255, 255));
		superviceControl(textProductPicturePath, 250);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(textProductPicturePath);

		// Load the picture from the picture path
		createPicturePathFromPictureName();
		setPicture();

		// Add a button to select a new picture
		Button selectPictureButton = new Button(productPictureGroup, SWT.PUSH);
		//T: Product Editor - Button Description: Select a Picture
		selectPictureButton.setText(_("Select a Picture"));
		selectPictureButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				// Open a file dialog to select the picture
				FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				fileDialog.setFilterPath(Activator.getDefault().getPreferenceStore().getString("GENERAL_WORKSPACE"));
				//T: Product Editor - File Dialog: Select a Picture
				fileDialog.setText(_("Select a Picture"));
				String selectedFile = fileDialog.open();

				if (selectedFile != null) {

					// Create a new subfolder, if it does not exists yet.
					createPictureName();
					File directory = new File(picturePath);
					if (!directory.exists())
						directory.mkdirs();

					// Copy the picture into the picture folder
					File inputFile = new File(selectedFile);
					File outputFile = new File(filename1 + filename2);

					try {

						// Copy it
						FileOutputStream out = new FileOutputStream(outputFile);
						FileInputStream ins = new FileInputStream(inputFile);
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						int c;

						// Read the file to an input buffer
						while ((c = ins.read()) != -1) {
							byteArrayOutputStream.write((byte) c);
						}

						// Write it to an file
						out.write(byteArrayOutputStream.toByteArray());

						// Close the streams
						byteArrayOutputStream.close();
						ins.close();
						out.close();

					}
					catch (IOException e1) {
						Logger.logError(e1, "Error copying picture from " + selectedFile + " to " + filename1 + filename2);
					}

					// Display the new picture
					setPicture();
					checkDirty();
				}

			}
		});
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(selectPictureButton);

	}

	/**
	 * Set the focus to the top composite.
	 * 
	 * @see com.sebulli.fakturama.editors.Editor#setFocus()
	 */
	@Override
	public void setFocus() {
		if(top != null) 
			top.setFocus();
	}


	/**
	 * Test, if there is a document with the same number
	 * 
	 * @return TRUE, if one with the same number is found
	 */
	public boolean thereIsOneWithSameNumber() {

		// Cancel, if there is already a document with the same ID
		if (Data.INSTANCE.getDocuments().isExistingDataSet(product, "itemnr", textItemNr.getText())) {
			// Display an error message
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);

			//T: Title of the dialog that appears if the item/product number is not valid.
			messageBox.setText(_("Error in item number"));

			//T: Text of the dialog that appears if the item/product number is not valid.
			messageBox.setMessage(_("There is already a product with the number:") + " " + textItemNr.getText());
			messageBox.open();

			return true;
		}

		return false;
	}

	/**
	 * Returns, if save is allowed
	 * 
	 * @return TRUE, if save is allowed
	 * 
	 * @see com.sebulli.fakturama.editors.Editor#saveAllowed()
	 */
	@Override
	protected boolean saveAllowed() {
		// Save is allowed, if there is no product with the same number
		return !thereIsOneWithSameNumber();
	}

}
