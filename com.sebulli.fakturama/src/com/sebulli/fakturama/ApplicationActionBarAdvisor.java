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

package com.sebulli.fakturama;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.sebulli.fakturama.actions.DeleteDataSetAction;
import com.sebulli.fakturama.actions.InstallAction;
import com.sebulli.fakturama.actions.MarkDocumentAsPaidAction;
import com.sebulli.fakturama.actions.MarkOrderAsAction;
import com.sebulli.fakturama.actions.MoveEntryDownAction;
import com.sebulli.fakturama.actions.MoveEntryUpAction;
import com.sebulli.fakturama.actions.NewContactAction;
import com.sebulli.fakturama.actions.NewDocumentAction;
import com.sebulli.fakturama.actions.NewExpenditureVoucherAction;
import com.sebulli.fakturama.actions.NewListEntryAction;
import com.sebulli.fakturama.actions.NewPaymentAction;
import com.sebulli.fakturama.actions.NewProductAction;
import com.sebulli.fakturama.actions.NewReceiptVoucherAction;
import com.sebulli.fakturama.actions.NewShippingAction;
import com.sebulli.fakturama.actions.NewTextAction;
import com.sebulli.fakturama.actions.NewVatAction;
import com.sebulli.fakturama.actions.OpenBrowserEditorAction;
import com.sebulli.fakturama.actions.OpenCalculatorAction;
import com.sebulli.fakturama.actions.OpenContactsAction;
import com.sebulli.fakturama.actions.OpenDocumentsAction;
import com.sebulli.fakturama.actions.OpenExpenditureVouchersAction;
import com.sebulli.fakturama.actions.OpenListsAction;
import com.sebulli.fakturama.actions.OpenParcelServiceAction;
import com.sebulli.fakturama.actions.OpenPaymentsAction;
import com.sebulli.fakturama.actions.OpenProductsAction;
import com.sebulli.fakturama.actions.OpenReceiptVouchersAction;
import com.sebulli.fakturama.actions.OpenShippingsAction;
import com.sebulli.fakturama.actions.OpenTextsAction;
import com.sebulli.fakturama.actions.OpenVatsAction;
import com.sebulli.fakturama.actions.ReorganizeDocumentsAction;
import com.sebulli.fakturama.actions.SelectWorkspaceAction;
import com.sebulli.fakturama.actions.UpdateAction;
import com.sebulli.fakturama.actions.WebShopImportAction;
import com.sebulli.fakturama.misc.DocumentType;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 * 
 * If the action is in the tool bar and in the menu, 2 actions have to be
 * defined: one with a 16x16 pixel icon for the menu and one with 32x32 pixel in
 * the tool bar.
 * 
 * The tool bar version of an action is called xxTB
 * 
 * @author Gerd Bartelt
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction printAction;
	private IWorkbenchAction printActionTB;
	private IWorkbenchAction closeAction;
	private IWorkbenchAction closeAllAction;
	private IWorkbenchAction saveAction;
	private IWorkbenchAction saveActionTB;
	private IWorkbenchAction saveAllAction;
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction importWizardAction;
	private IWorkbenchAction exportWizardAction;
	private IWorkbenchAction openPreferencesAction;
	private IWorkbenchAction resetViewAction;
	private IWorkbenchAction showHelpAction;
    private IWorkbenchAction searchHelpAction;
    private IWorkbenchAction dynamicHelpAction;	
    private IWorkbenchAction introAction;

	
	private OpenBrowserEditorAction openBrowserEditorAction;
	private OpenBrowserEditorAction openBrowserEditorActionTB;
	private OpenParcelServiceAction openParcelServiceAction;
	private OpenParcelServiceAction openParcelServiceActionTB;
	private OpenCalculatorAction openCalculatorAction;
	private OpenCalculatorAction openCalculatorActionTB;
	private OpenContactsAction openContactsAction;
	private OpenProductsAction openProductsAction;
	private OpenVatsAction openVatsAction;
	private OpenDocumentsAction openDocumentsAction;
	private OpenShippingsAction openShippingsAction;
	private OpenPaymentsAction openPaymentsAction;
	private OpenTextsAction openTextsAction;
	private OpenListsAction openListsAction;
	private OpenExpenditureVouchersAction openExpenditureVouchersAction;
	private OpenReceiptVouchersAction openReceiptVouchersAction;
	private NewProductAction newProductAction;
	private NewProductAction newProductActionTB;
	private NewContactAction newContactAction;
	private NewContactAction newContactActionTB;
	private NewVatAction newVatAction;
	private NewShippingAction newShippingAction;
	private NewPaymentAction newPaymentAction;
	private NewDocumentAction newLetterAction;
	private NewDocumentAction newLetterActionTB;
	private NewDocumentAction newOfferAction;
	private NewDocumentAction newOfferActionTB;
	private NewDocumentAction newOrderAction;
	private NewDocumentAction newOrderActionTB;
	private NewDocumentAction newConfirmationAction;
	private NewDocumentAction newConfirmationActionTB;
	private NewDocumentAction newInvoiceAction;
	private NewDocumentAction newInvoiceActionTB;
	private NewDocumentAction newDeliveryAction;
	private NewDocumentAction newDeliveryActionTB;
	private NewDocumentAction newCreditAction;
	private NewDocumentAction newCreditActionTB;
	private NewDocumentAction newDunningAction;
	private NewDocumentAction newDunningActionTB;
	private NewDocumentAction newProformaAction;
	private NewDocumentAction newProformaActionTB;
	private NewTextAction newTextAction;
	private NewListEntryAction newListEntryAction;
	private NewExpenditureVoucherAction newExpenditureVoucherAction;
	private NewExpenditureVoucherAction newExpenditureVoucherActionTB;
	private NewReceiptVoucherAction newReceiptVoucherAction;
	private NewReceiptVoucherAction newReceiptVoucherActionTB;
	private SelectWorkspaceAction selectWorkspaceAction;
	private WebShopImportAction webShopImportAction;
	private WebShopImportAction webShopImportActionTB;
	private MarkOrderAsAction markAsPendingAction;
	private MarkOrderAsAction markAsProcessingAction;
	private MarkOrderAsAction markAsShippedAction;
	private MarkDocumentAsPaidAction markDocumentAsPaidAction;
	private MarkDocumentAsPaidAction markDocumentAsUnpaidAction;
	private DeleteDataSetAction deleteDataSetAction;
	private UpdateAction updateAction;
	private InstallAction installAction;
	private MoveEntryUpAction moveEntryUpAction;
	private MoveEntryDownAction moveEntryDownAction;
	private ReorganizeDocumentsAction reorganizeDocumentsAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	/**
	 * Creates the actions and registers them. Registering is needed to ensure
	 * that key bindings work. The corresponding commands key bindings are
	 * defined in the plugin.xml file. Registering also provides automatic
	 * disposal of the actions when the window is closed.
	 * 
	 * @param window
	 *            Workbench Window
	 */
	@Override
	protected void makeActions(final IWorkbenchWindow window) {

		exportWizardAction = ActionFactory.EXPORT.create(window);
		exportWizardAction.setImageDescriptor(Activator.getImageDescriptor("/icons/16/export_16.png"));
		//T: Text of the actions in the main menu
		exportWizardAction.setText(_("Export.."));
		register(exportWizardAction);

		importWizardAction = ActionFactory.IMPORT.create(window);
		importWizardAction.setImageDescriptor(Activator.getImageDescriptor("/icons/16/import_16.png"));
		//T: Text of the actions in the main menu
		importWizardAction.setText(_("Import.."));
		register(importWizardAction);

		openPreferencesAction = ActionFactory.PREFERENCES.create(window);
		//T: Text of the actions in the main menu
		openPreferencesAction.setText(_("Preferences"));
		register(openPreferencesAction);

		exitAction = ActionFactory.QUIT.create(window);
		//T: Text of the actions in the main menu
		exitAction.setText(_("Quit Fakturama"));
		register(exitAction);

		printAction = ActionFactory.PRINT.create(window);
		//T: Text of the actions in the main menu
		printAction.setText(_("Print"));
		register(printAction);

		printActionTB = ActionFactory.PRINT.create(window);
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		printActionTB.setText(_("Print", "TOOLBAR"));
		register(printActionTB);

		closeAction = ActionFactory.CLOSE.create(window);
		//T: Text of the actions in the main menu
		closeAction.setText(_("Close"));
		register(closeAction);

		closeAllAction = ActionFactory.CLOSE_ALL.create(window);
		//T: Text of the actions in the main menu
		closeAllAction.setText(_("Close All"));
		register(closeAllAction);

		saveAction = ActionFactory.SAVE.create(window);
		//T: Text of the actions in the main menu
		saveAction.setText(_("Save"));
		register(saveAction);

		saveActionTB = ActionFactory.SAVE.create(window);
		register(saveActionTB);

		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		//T: Text of the actions in the main menu
		saveAllAction.setText(_("Save All"));
		register(saveAllAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		aboutAction.setImageDescriptor(Activator.getImageDescriptor("/icons/16/app_16.png"));

		//T: Text of the actions in the main menu
		aboutAction.setText(_("About Fakturama"));
		register(aboutAction);
		
		showHelpAction = ActionFactory.HELP_CONTENTS.create(window);
		//T: Text of the actions in the main menu
		showHelpAction.setText(_("Help Contents"));
		register(showHelpAction);

		searchHelpAction = ActionFactory.HELP_SEARCH.create(window);
		//T: Text of the actions in the main menu
		searchHelpAction.setText(_("Search"));
		register(searchHelpAction);

		dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
		//T: Text of the actions in the main menu
		dynamicHelpAction.setText(_("Dynamic Help"));
		register(dynamicHelpAction);
		
		introAction = ActionFactory.INTRO.create(window);
		//T: Text of the actions in the main menu
		introAction.setText(_("Show Intro"));
		register(introAction);


		resetViewAction = ActionFactory.RESET_PERSPECTIVE.create(window);
		//T: Text of the actions in the main menu
		resetViewAction.setText(_("Reset Perspective"));
		register(resetViewAction);

		openParcelServiceAction = new OpenParcelServiceAction();
		register(openParcelServiceAction);
		openParcelServiceActionTB = new OpenParcelServiceAction();
		register(openParcelServiceActionTB);

		openBrowserEditorAction = new OpenBrowserEditorAction(true);
		register(openBrowserEditorAction);
		openBrowserEditorActionTB = new OpenBrowserEditorAction(true);
		register(openBrowserEditorActionTB);

		openCalculatorAction = new OpenCalculatorAction();
		register(openCalculatorAction);
		openCalculatorActionTB = new OpenCalculatorAction();
		register(openCalculatorActionTB);

		deleteDataSetAction = new DeleteDataSetAction();
		register(deleteDataSetAction);

		moveEntryUpAction = new MoveEntryUpAction();
		register(moveEntryUpAction);
		moveEntryDownAction = new MoveEntryDownAction();
		register(moveEntryDownAction);
		
		webShopImportAction = new WebShopImportAction();
		register(webShopImportAction);
		webShopImportActionTB = new WebShopImportAction();
		register(webShopImportActionTB);
		

		openProductsAction = new OpenProductsAction();
		register(openProductsAction);
		newProductAction = new NewProductAction();
		register(newProductAction);
		newProductActionTB = new NewProductAction();
		register(newProductActionTB);

		openContactsAction = new OpenContactsAction();
		register(openContactsAction);
		newContactAction = new NewContactAction(null);
		register(newContactAction);
		newContactActionTB = new NewContactAction(null);
		register(newContactActionTB);

		openVatsAction = new OpenVatsAction();
		register(openVatsAction);
		newVatAction = new NewVatAction();
		register(newVatAction);

		openShippingsAction = new OpenShippingsAction();
		register(openShippingsAction);
		newShippingAction = new NewShippingAction();
		register(newShippingAction);

		openPaymentsAction = new OpenPaymentsAction();
		register(openPaymentsAction);
		newPaymentAction = new NewPaymentAction();
		register(newPaymentAction);

		openTextsAction = new OpenTextsAction();
		register(openTextsAction);
		newTextAction = new NewTextAction();
		register(newTextAction);

		openListsAction = new OpenListsAction();
		register(openListsAction);
		newListEntryAction = new NewListEntryAction();
		register(newListEntryAction);

		openExpenditureVouchersAction = new OpenExpenditureVouchersAction();
		register(openExpenditureVouchersAction);
		newExpenditureVoucherAction = new NewExpenditureVoucherAction();
		register(newExpenditureVoucherAction);
		newExpenditureVoucherActionTB = new NewExpenditureVoucherAction();
		register(newExpenditureVoucherActionTB);

		openReceiptVouchersAction = new OpenReceiptVouchersAction();
		register(openReceiptVouchersAction);
		newReceiptVoucherAction = new NewReceiptVoucherAction();
		register(newReceiptVoucherAction);
		newReceiptVoucherActionTB = new NewReceiptVoucherAction();
		register(newReceiptVoucherActionTB);

		openDocumentsAction = new OpenDocumentsAction();
		register(openDocumentsAction);

		newLetterAction = new NewDocumentAction(DocumentType.LETTER);
		register(newLetterAction);
		newLetterActionTB = new NewDocumentAction(DocumentType.LETTER);
		register(newLetterActionTB);
		
		newOfferAction = new NewDocumentAction(DocumentType.OFFER);
		register(newOfferAction);
		newOfferActionTB = new NewDocumentAction(DocumentType.OFFER);
		register(newOfferActionTB);

		newOrderAction = new NewDocumentAction(DocumentType.ORDER);
		register(newOrderAction);
		newOrderActionTB = new NewDocumentAction(DocumentType.ORDER);
		register(newOrderActionTB);

		newConfirmationAction = new NewDocumentAction(DocumentType.CONFIRMATION);
		register(newConfirmationAction);
		newConfirmationActionTB = new NewDocumentAction(DocumentType.CONFIRMATION);
		register(newConfirmationActionTB);

		newInvoiceAction = new NewDocumentAction(DocumentType.INVOICE);
		register(newInvoiceAction);
		newInvoiceActionTB = new NewDocumentAction(DocumentType.INVOICE);
		register(newInvoiceActionTB);

		newDeliveryAction = new NewDocumentAction(DocumentType.DELIVERY);
		register(newDeliveryAction);
		newDeliveryActionTB = new NewDocumentAction(DocumentType.DELIVERY);
		register(newDeliveryActionTB);

		newCreditAction = new NewDocumentAction(DocumentType.CREDIT);
		register(newCreditAction);
		newCreditActionTB = new NewDocumentAction(DocumentType.CREDIT);
		register(newCreditActionTB);

		newDunningAction = new NewDocumentAction(DocumentType.DUNNING);
		register(newDunningAction);
		newDunningActionTB = new NewDocumentAction(DocumentType.DUNNING);
		register(newDunningActionTB);

		newProformaAction = new NewDocumentAction(DocumentType.PROFORMA);
		register(newProformaAction);
		newProformaActionTB = new NewDocumentAction(DocumentType.PROFORMA);
		register(newProformaActionTB);

		selectWorkspaceAction = new SelectWorkspaceAction();
		register(selectWorkspaceAction);

		markAsPendingAction = new MarkOrderAsAction( MarkOrderAsAction.PENDING);
		register(markAsPendingAction);
		
		markAsProcessingAction = new MarkOrderAsAction( MarkOrderAsAction.PROCESSING);
		register(markAsProcessingAction);

		markAsShippedAction = new MarkOrderAsAction( MarkOrderAsAction.SHIPPED);
		register(markAsShippedAction);
		
		markDocumentAsPaidAction = new MarkDocumentAsPaidAction(true);
		register(markDocumentAsPaidAction);
		
		markDocumentAsUnpaidAction = new MarkDocumentAsPaidAction(false);
		register(markDocumentAsUnpaidAction);
		
		reorganizeDocumentsAction = new ReorganizeDocumentsAction();
		register(reorganizeDocumentsAction);
		
		updateAction = new UpdateAction();
		register(updateAction);

		installAction = new InstallAction();
		register(installAction);

	}

	/**
	 * Fill the menu bar.
	 * 
	 * On MAC OS X the entries "about" and "preferences" are in a special menu.
	 * So on this OS, the entries are not added to the menu.
	 * 
	 * @param menuBar
	 *            menu bar to fill
	 */
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {

		//T: Title of the menus in the main menu
		MenuManager fileMenu = new MenuManager(_("File"), IWorkbenchActionConstants.M_FILE);
		//T: Title of the menus in the main menu
		MenuManager editMenu = new MenuManager(_("Edit"), IWorkbenchActionConstants.M_EDIT);
		//T: Title of the menus in the main menu
		MenuManager dataMenu = new MenuManager(_("Data"), "com.sebulli.faktura.menu.data");
		//T: Title of the menus in the main menu
		MenuManager newMenu = new MenuManager(_("New"), "com.sebulli.faktura.menu.create");
		//T: Title of the menus in the main menu
		MenuManager extraMenu = new MenuManager(_("Extra"), "com.sebulli.faktura.menu.extra");
		//T: Title of the menus in the main menu
		MenuManager windowMenu = new MenuManager(_("Window"), IWorkbenchActionConstants.M_WINDOW);
		//T: Title of the menus in the main menu
		MenuManager helpMenu = new MenuManager(_("Help"), IWorkbenchActionConstants.M_HELP);
		//T: Title of the menus in the main menu
		MenuManager hiddenMenu = new MenuManager("Hidden", "com.sebulli.faktura.menu.hidden");
		hiddenMenu.setVisible(false);

		menuBar.add(fileMenu);
		// Add a group marker indicating where action set menus will appear.
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(editMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(dataMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(newMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(extraMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(windowMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(helpMenu);
		menuBar.add(hiddenMenu);

		// File menu
		fileMenu.add(closeAction);
		fileMenu.add(closeAllAction);
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
		fileMenu.add(new Separator());
		fileMenu.add(saveAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
		fileMenu.add(new Separator());
		fileMenu.add(printAction);
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.PRINT_EXT));
		fileMenu.add(new Separator());
		fileMenu.add(selectWorkspaceAction);
		if (OSDependent.canAddPreferenceAboutMenu()) {
			fileMenu.add(new Separator());
			fileMenu.add(openPreferencesAction);
		}
		else {
			hiddenMenu.add(openPreferencesAction);
		}
		fileMenu.add(new Separator());
		fileMenu.add(webShopImportAction);
		fileMenu.add(importWizardAction);
		fileMenu.add(exportWizardAction);
		fileMenu.add(new GroupMarker(ActionFactory.EXPORT.getId()));
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);

		// edit menu
		editMenu.add(deleteDataSetAction);
		editMenu.add(new Separator());
		editMenu.add(markDocumentAsUnpaidAction);
		editMenu.add(markDocumentAsPaidAction);
		editMenu.add(new Separator());
		editMenu.add(markAsPendingAction);
		editMenu.add(markAsProcessingAction);
		editMenu.add(markAsShippedAction);
		
		

		// data menu
		dataMenu.add(openDocumentsAction);
		dataMenu.add(openProductsAction);
		dataMenu.add(openContactsAction);
		dataMenu.add(openPaymentsAction);
		dataMenu.add(openShippingsAction);
		dataMenu.add(openVatsAction);
		dataMenu.add(openTextsAction);
		dataMenu.add(openListsAction);
		dataMenu.add(openExpenditureVouchersAction);
		dataMenu.add(openReceiptVouchersAction);

		// create menu
		newMenu.add(newLetterAction);
		newMenu.add(newOfferAction);
		newMenu.add(newOrderAction);
		newMenu.add(newConfirmationAction);
		newMenu.add(newInvoiceAction);
		newMenu.add(newDeliveryAction);
		newMenu.add(newCreditAction);
		newMenu.add(newDunningAction);
		newMenu.add(newProformaAction);
		newMenu.add(new Separator());
		newMenu.add(newProductAction);
		newMenu.add(newContactAction);
		newMenu.add(newPaymentAction);
		newMenu.add(newShippingAction);
		newMenu.add(newVatAction);
		newMenu.add(newTextAction);
		newMenu.add(newListEntryAction);
		newMenu.add(newExpenditureVoucherAction);
		newMenu.add(newReceiptVoucherAction);
		newMenu.add(new Separator());
		newMenu.add(openParcelServiceAction);
		
		// extra menu
		extraMenu.add(reorganizeDocumentsAction);

		// window menu
		windowMenu.add(resetViewAction);
		windowMenu.add(new Separator());
		windowMenu.add(openCalculatorAction);

		// Help menu
		helpMenu.add(openBrowserEditorAction);
		helpMenu.add(showHelpAction);
		helpMenu.add(searchHelpAction);
		helpMenu.add(dynamicHelpAction);

		// Intro
		helpMenu.add(new Separator());
		helpMenu.add(introAction);

		helpMenu.add(new Separator());
//		helpMenu.add(updateAction);
//		helpMenu.add(installAction);
		
		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));


		if (OSDependent.canAddAboutMenuItem()) {
			helpMenu.add(new Separator());
			helpMenu.add(aboutAction);
		}
		else {
			hiddenMenu.add(aboutAction);
		}

	}

	/**
	 * Fill the cool bar with 3 Toolbars.
	 * 
	 * 1st with general tool items like save and print. 2nd with tool items to
	 * create a new document 3rd with some extra items like calculator
	 * 
	 * The icons of the actions are replaced by 32x32 pixel icons. If the action
	 * is in the tool bar and in the menu, 2 actions have to be defined: one
	 * with a 16x16 pixel icon for the menu and one with 32x32 pixel in the tool
	 * bar.
	 * 
	 * @param collBar
	 *            cool bar to fill
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		
		IToolBarManager toolbar1 = new ToolBarManager(SWT.FLAT);
		IToolBarManager toolbar2 = new ToolBarManager(SWT.FLAT);
		IToolBarManager toolbar3 = new ToolBarManager(SWT.FLAT);
		IToolBarManager toolbar4 = new ToolBarManager(SWT.FLAT);

		ToolBarContributionItem tbci1 = new ToolBarContributionItem(toolbar1, "main1");
		ToolBarContributionItem tbci2 = new ToolBarContributionItem(toolbar2, "main2");
		ToolBarContributionItem tbci3 = new ToolBarContributionItem(toolbar3, "main3");
		ToolBarContributionItem tbci4 = new ToolBarContributionItem(toolbar4, "main4");
		
		webShopImportActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/shop_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		webShopImportActionTB.setText(_("Web Shop", "TOOLBAR"));
		ActionContributionItem webShopImportCI = new ActionContributionItem(webShopImportActionTB);
		webShopImportCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_WEBSHOP"))
			toolbar1.add(webShopImportCI);

		printActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/printoo_32.png"));
		printActionTB.setDisabledImageDescriptor(Activator.getImageDescriptor("/icons/32/printoo_dis_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		printActionTB.setText(_("Print", "TOOLBAR"));
		ActionContributionItem printActionTBCI = new ActionContributionItem(printActionTB);
		printActionTBCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_PRINT"))
			toolbar1.add(printActionTBCI);

		saveActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/save_32.png"));
		saveActionTB.setDisabledImageDescriptor(Activator.getImageDescriptor("/icons/32/save_dis_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		saveActionTB.setText(_("Save", "TOOLBAR"));
		ActionContributionItem saveCI = new ActionContributionItem(saveActionTB);
		saveCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_SAVE"))
			toolbar1.add(saveCI);

		newLetterActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/letter_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newLetterActionTB.setText(_("Letter", "TOOLBAR"));
		ActionContributionItem newLetterCI = new ActionContributionItem(newLetterActionTB);
		newLetterCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_LETTER"))
			toolbar2.add(newLetterCI);

		newOfferActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/offer_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newOfferActionTB.setText(_("Offer", "TOOLBAR"));
		ActionContributionItem newOfferCI = new ActionContributionItem(newOfferActionTB);
		newOfferCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_OFFER"))
			toolbar2.add(newOfferCI);

		newOrderActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/order_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newOrderActionTB.setText(_("Order", "TOOLBAR"));
		ActionContributionItem newOrderCI = new ActionContributionItem(newOrderActionTB);
		newOrderCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_ORDER"))
			toolbar2.add(newOrderCI);

		newConfirmationActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/confirmation_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newConfirmationActionTB.setText(_("Confirmation", "TOOLBAR"));
		ActionContributionItem newConfirmationCI = new ActionContributionItem(newConfirmationActionTB);
		newConfirmationCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_CONFIRMATION"))
			toolbar2.add(newConfirmationCI);

		newInvoiceActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/invoice_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newInvoiceActionTB.setText(_("Invoice", "TOOLBAR"));
		ActionContributionItem newInvoiceCI = new ActionContributionItem(newInvoiceActionTB);
		newInvoiceCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_INVOICE"))
			toolbar2.add(newInvoiceCI);

		newDeliveryActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/delivery_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newDeliveryActionTB.setText(_("Delivery", "TOOLBAR"));
		ActionContributionItem newDeliveryCI = new ActionContributionItem(newDeliveryActionTB);
		newDeliveryCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_DELIVERY"))
			toolbar2.add(newDeliveryCI);

		newCreditActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/credit_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newCreditActionTB.setText(_("Credit", "TOOLBAR"));
		ActionContributionItem newCreditCI = new ActionContributionItem(newCreditActionTB);
		newCreditCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_CREDIT"))
			toolbar2.add(newCreditCI);

		
		newDunningActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/dunning_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newDunningActionTB.setText(_("Dunning", "TOOLBAR"));
		ActionContributionItem newDunningCI = new ActionContributionItem(newDunningActionTB);
		newDunningCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_DUNNING"))
			toolbar2.add(newDunningCI);
		
		newProformaActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/proforma_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newProformaActionTB.setText(_("Proforma", "TOOLBAR"));
		ActionContributionItem newProformaCI = new ActionContributionItem(newProformaActionTB);
		newProformaCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_PROFORMA"))
			toolbar2.add(newProformaCI);

		newProductActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/product_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newProductActionTB.setText(_("Product", "TOOLBAR"));
		ActionContributionItem newProductCI = new ActionContributionItem(newProductActionTB);
		newProductCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_NEW_PRODUCT"))
			toolbar3.add(newProductCI);

		newContactActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/contact_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newContactActionTB.setText(_("Contact", "TOOLBAR"));
		ActionContributionItem newContactCI = new ActionContributionItem(newContactActionTB);
		newContactCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_NEW_CONTACT"))
			toolbar3.add(newContactCI);

		newExpenditureVoucherActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/expenditure_voucher_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newExpenditureVoucherActionTB.setText(_("Expenditure", "TOOLBAR"));
		ActionContributionItem newExpenditureVoucherCI = new ActionContributionItem(newExpenditureVoucherActionTB);
		newExpenditureVoucherCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_NEW_EXPENDITUREVOUCHER"))
			toolbar3.add(newExpenditureVoucherCI);

		newReceiptVoucherActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/receipt_voucher_new_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		newReceiptVoucherActionTB.setText(_("Receipt", "TOOLBAR"));
		ActionContributionItem newReceiptVoucherCI = new ActionContributionItem(newReceiptVoucherActionTB);
		newReceiptVoucherCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_NEW_RECEIPTVOUCHER"))
			toolbar3.add(newReceiptVoucherCI);

		openParcelServiceActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/parcel_service_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		openParcelServiceActionTB.setText(_("Parcel", "TOOLBAR"));
		ActionContributionItem openParcelServiceCI = new ActionContributionItem(openParcelServiceActionTB);
		openParcelServiceCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_OPEN_PARCELSERVICE"))
			toolbar4.add(openParcelServiceCI);

		openBrowserEditorActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/www_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		openBrowserEditorActionTB.setText(_("www", "TOOLBAR"));
		ActionContributionItem openBrowserEditorCI = new ActionContributionItem(openBrowserEditorActionTB);
		openBrowserEditorCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_OPEN_BROWSER"))
			toolbar4.add(openBrowserEditorCI);

		openCalculatorActionTB.setImageDescriptor(Activator.getImageDescriptor("/icons/32/calculator_32.png"));
		//T: Text of the actions in the tool bar. Keep it short that it can be placed under the icon.
		openCalculatorActionTB.setText(_("Calculator", "TOOLBAR"));
		ActionContributionItem openCalculatorCI = new ActionContributionItem(openCalculatorActionTB);
		openCalculatorCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		if (Activator.getDefault().getPreferenceStore().getBoolean("TOOLBAR_SHOW_OPEN_CALCULATOR"))
			toolbar4.add(openCalculatorCI);

		// Is there at least one icon in the toolbar ?
		if (toolbar1.getItems().length > 0)
			coolBar.add(tbci1);
		// Is there at least one icon in the toolbar ?
		if (toolbar2.getItems().length > 0)
			coolBar.add(tbci2);
		// Is there at least one icon in the toolbar ?
		if (toolbar3.getItems().length > 0)
			coolBar.add(tbci3);
		// Is there at least one icon in the toolbar ?
		if (toolbar4.getItems().length > 0)
			coolBar.add(tbci4);

		
	}
}
