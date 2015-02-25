/**
 * 
 */
package com.sebulli.fakturama.actions;

import static com.sebulli.fakturama.Translate._;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.misc.DocumentType;
import com.sebulli.fakturama.views.datasettable.ViewDataSetTable;

/**
 * @author rheydenr
 *
 */
public class CreateCollectiveInvoiceAction extends Action {

	public CreateCollectiveInvoiceAction() {
		super();
		//T: Text of the action
		this.setText(_("create collective invoice"));
		setSettings(ICommandIds.CMD_CREATE_COLLECTIVE_INVOICE, "/icons/16/collective_invoice_16.png");

    }

	/**
	 * Set command ID and icon for this action.
	 * 
	 * @param cmd
	 *            command ID
	 * @param image
	 *            Actions's icon
	 */
	private void setSettings(String cmd, String image) {
		setId(cmd);
		setActionDefinitionId(cmd);
		setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor(image));
	}
	
	@Override
	public void run() {

		// cancel, if the data base is not connected.
		if (!DataBaseConnectionState.INSTANCE.isConnected())
			return;

		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();

		// Get the active part (view)
		IWorkbenchPart part = null;
		if (page != null)
			part = page.getActivePart();

		ISelection selection;

		// Cast the part to ViewDataSetTable
		if (part instanceof ViewDataSetTable) {

			ViewDataSetTable view = (ViewDataSetTable) part;

			// does the view exist ?
			if (view != null) {

				//get the selection
				selection = view.getSite().getSelectionProvider().getSelection();

				if (selection != null && selection instanceof IStructuredSelection) {
/*
 * Steps:
 * 1. Create a new collective invoice from delivery documents which aren't part of a
 *    collective invoice
 * 2. Open the (read-only) view for collective invoices
 * 3. Print the document (has to be initiated by user, because we don't know the template 
 *    which has to be used)
 *    
 * Checks:
 * - only selections with more than one items are accepted
 * - only items which aren't part of a collective invoice are accepted
 * - only delivery documents are accepted
 * - only items which don't have a reference to a "normal" invoice
 *   are accepted
 *   
 */
					
					Iterator iterator = ((IStructuredSelection) selection).iterator();
					while(iterator.hasNext()) {
						Object obj = iterator.next();
						DataSetDocument uds = (DataSetDocument) obj;
						if (uds instanceof DataSetDocument) {

							// Do it only, if it is allowed to mark this kind of document as paid.
							if (DocumentType.getType(uds.getIntValueByKey("category")).hasPaid()) {

								// change the state
//								uds.setPaid(paid);

								// also in the database
								Data.INSTANCE.updateDataSet(uds);
							}
						}
					}
				}
			}
		}
	}
}
