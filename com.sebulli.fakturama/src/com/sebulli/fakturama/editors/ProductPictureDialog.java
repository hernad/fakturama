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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.sebulli.fakturama.Workspace;

/**
 * This class represents the product picture preview dialog
 * 
 * @author Gerd Bartelt
 */
public class ProductPictureDialog {

	// The picture name
	String pictureName ="";
	
	//The shell to display the dialog
	Shell shell = null;
	
	// The scaled image with width and height (used to resize the dialog)
	Image scaledImage = null;
	int width = 300;
	int height = 200;

	/**
	 * Constructor
	 * 
	 * @param 
	 * 			shell for the dialog
	 * @param 
	 * 			pictureName The name of the product picture to show
	 */
	public ProductPictureDialog(Shell shell, String pictureName) {

		this.pictureName = pictureName;
		this.shell = shell;

		if (pictureName.isEmpty())
			return;
		
		try {
			// Display the picture, if it is set.
			if (!pictureName.isEmpty()) {


				// Load the image, based on the picture name
				Image image = new Image(shell.getDisplay(),  Workspace.INSTANCE.getWorkspace() + Workspace.productPictureFolderName + pictureName);

				// Get the pictures size
				width = image.getBounds().width;
				height = image.getBounds().height;

				// Scale it to maximum 250px
				int maxWidth = 250;
				
				// Maximum picture width 
				if (width > maxWidth) {
					height = maxWidth * height / width;
					width = maxWidth;
				}

				// Rescale the picture to the maximum width
				scaledImage = new Image(shell.getDisplay(), image.getImageData().scaledTo(width, height));

			}
		}
		catch (Exception e) {
		}
	}
	
	/**
	 * Open a dialog with the picture
	 * 
	 */
	public void show() {
		
		// Exit, if no picture is set
		if (pictureName.isEmpty())
			return;
		
		// Exit, if no picture is set
		if (scaledImage == null) 
			return;

		// The Dialog
		Dialog dialog = new Dialog(shell) {
		    @Override
		    protected Control createDialogArea(Composite parent) {
		        Composite composite = (Composite) super.createDialogArea(parent);
				GridLayoutFactory.swtDefaults().numColumns(1).applyTo(composite);
				GridDataFactory.fillDefaults().grab(true,false).align(SWT.FILL, SWT.FILL).applyTo(composite);

				// Add a label that contains the image
				Label label = new Label(composite, SWT.NONE);
		        label.setImage(scaledImage);
				GridDataFactory.fillDefaults().grab(true,false).align(SWT.CENTER, SWT.TOP).applyTo(label);
		        return composite;
		    }

		    @Override
		    protected Point getInitialSize() {
		    	// Scale the dialog to the picture
		        return new Point(width + 50, height + 100);
		    }

		    @Override
		    protected void configureShell(Shell newShell) {
		        super.configureShell(newShell);
		        newShell.setText(_("Preview"));
		    }
		};
		    
		// Open the dialog
		dialog.open();
		
	}
}
