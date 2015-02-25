package com.sebulli.fakturama.database_check;

import java.io.PrintWriter;
import java.util.List;

public class Scrambler {

	// Reference to the database
	Database database;
	// File to export the scrambled database
	PrintWriter outputWriter;
	
	/**
	 * Constructor
	 * Set a reference to the database
	 * 
	 * @param database A reference to the database
	 */
	public Scrambler(Database database, PrintWriter outputWriter) {
		this.database = database;
		this.outputWriter = outputWriter;
	}
	
	/**
	 * Write the scrambled data to the output file
	 */
	private void writeScrambledData() {
		// Scan all tables
		for (String key : database.dataTableKeys) {
			
			// Get the table and a list with all datasets in this table
			Datatable table = database.tableDataset.get(key);
			List<Dataset> datasets = table.getDatatable();
			
			// Scan all datasets (rows)
			for (Dataset dataset : datasets) {
				String line ="";
				for (String data: dataset.getData()) {

					if (data.startsWith("'") && data.endsWith("'")) {
						// Remove the trailing and leading ","
						data = data.substring(1, data.length()-1);

						// Escape '
						//line = line.replace("'", "$FACTURAMA_DBSCRAMBLER_QUOT$");
						data = data.replace("'", "''");
						
						// Add the ' again
						data = "'" + data + "'";
					} else {
						// Escape '
						data = data.replace("'", "''");
					}
					
					line += data + ",";
				}
				
				// Remove the leading ","
				line = line.substring(0, line.length()-1);
				// Write the line to the scrambled database
				outputWriter.println("INSERT INTO " + key + " VALUES(" + line + ")");
			}
		}
	}

	/**
	 * Scramble a property
	 * @param key
	 */
	private void scrambleProperty(String key) {
		// Get the table and a list with all datasets in this table
		Datatable table = database.tableDataset.get("PROPERTIES");
		List<Dataset> datasets = table.getDatatable();
		
		// Scan all datasets (rows)
		for (Dataset dataset : datasets) {
			
			// Modify the data
			if (dataset.getData().get(1).equals("'" + key + "'")) {
				//Set the data
				dataset.getData().set(2, "'XXX'");
			}
		}
	}
	
	/**
	 * Replace all cells of a specified table and column with default values
	 * 
	 * @param tableName
	 * @param ColumnName
	 */
	private void scrambleData(String tableName, String ColumnName) {
		// Get the table and a list with all datasets in this table
		Datatable table = database.tableDataset.get(tableName);
		List<Dataset> datasets = table.getDatatable();
		
		int columnNr = database.getColumnIndexByName(tableName, ColumnName);
		
		
		// Get the column information with column data type
		Columntype type = Columntype.NONE;
		if ((columnNr >= 0) && (columnNr <database.tableHeaders.get(tableName).columns.size())) {
			TableColumn tablecolumn = database.tableHeaders.get(tableName).columns.get(columnNr);
			type = tablecolumn.getType();
		}

		// Scan all datasets (rows)
		for (Dataset dataset : datasets) {
			
			// Modify the data
			if ((columnNr) >= 0 && (columnNr< dataset.getData().size())) {
				
				// Get the cell content
				String data = dataset.getData().get(columnNr);
				
				// Replace it with default data
				if ((type == Columntype.VARCHAR_256 || type == Columntype.VARCHAR_32768
						|| type == Columntype.VARCHAR_60000)
						&& data.startsWith("'") && data.endsWith("'")) {
					data = "'XXX'";
				} else if (type == Columntype.BOOLEAN){
					data = "FALSE";
				} else if (type == Columntype.INTEGER){
					data = "0";
				} else if (type == Columntype.DOUBLE){
					data = "0.0E0";
				}
				
				//Set the data
				dataset.getData().set(columnNr, data);
			}
		}
	}
	
	/**
	 * Run the scramble script
	 * 
	 */
	public void run () {
		
		// Info message
		Logger.getInstance().logText("Scrambling database file ...");

		// Scramble cells
		scrambleData("CONTACTS", "NAME");
		scrambleData("CONTACTS", "ACCOUNT");
		scrambleData("CONTACTS", "ACCOUNT_HOLDER");
		scrambleData("CONTACTS", "BANK_CODE");
		scrambleData("CONTACTS", "BANK_NAME");
		scrambleData("CONTACTS", "BIC");
		scrambleData("CONTACTS", "CITY");
		scrambleData("CONTACTS", "COMPANY");
		scrambleData("CONTACTS", "COUNTRY");
		scrambleData("CONTACTS", "DELIVERY_CITY");
		scrambleData("CONTACTS", "DELIVERY_COMPANY");
		scrambleData("CONTACTS", "DELIVERY_COUNTRY");
		scrambleData("CONTACTS", "DELIVERY_FIRSTNAME");
		scrambleData("CONTACTS", "DELIVERY_NAME");
		scrambleData("CONTACTS", "DELIVERY_STREET");
		scrambleData("CONTACTS", "DELIVERY_TITLE");
		scrambleData("CONTACTS", "DELIVERY_ZIP");
		scrambleData("CONTACTS", "EMAIL");
		scrambleData("CONTACTS", "FAX");
		scrambleData("CONTACTS", "FIRSTNAME");
		scrambleData("CONTACTS", "IBAN");
		scrambleData("CONTACTS", "MOBILE");
		scrambleData("CONTACTS", "NOTE");
		scrambleData("CONTACTS", "NR");
		scrambleData("CONTACTS", "PHONE");
		scrambleData("CONTACTS", "STREET");
		scrambleData("CONTACTS", "TITLE");
		scrambleData("CONTACTS", "VATNR");
		scrambleData("CONTACTS", "WEBSITE");
		scrambleData("CONTACTS", "ZIP");
		scrambleData("CONTACTS", "BIRTHDAY");
		scrambleData("CONTACTS", "DELIVERY_BIRTHDAY");
		scrambleData("CONTACTS", "MANDAT_REF");
		scrambleData("CONTACTS", "SUPPLIERNUMBER");
		scrambleData("CONTACTS", "FIRSTNAME");
		scrambleData("CONTACTS", "FIRSTNAME");
		scrambleData("CONTACTS", "FIRSTNAME");
		scrambleData("CONTACTS", "FIRSTNAME");
		scrambleData("CONTACTS", "FIRSTNAME");
		scrambleData("CONTACTS", "FIRSTNAME");
		
		scrambleData("PRODUCTS", "DESCRIPTION");
		scrambleData("PRODUCTS", "ITEMNR");
		scrambleData("PRODUCTS", "NAME");
		scrambleData("PRODUCTS", "PRICE1");
		scrambleData("PRODUCTS", "PRICE2");
		scrambleData("PRODUCTS", "PRICE3");
		scrambleData("PRODUCTS", "PRICE4");
		scrambleData("PRODUCTS", "PRICE5");
		scrambleData("PRODUCTS", "PRICE4");
		scrambleData("PRODUCTS", "PRICE4");
		scrambleData("PRODUCTS", "PRICE4");
		scrambleData("PRODUCTS", "PICTURENAME");

				
		
		
		scrambleData("DOCUMENTS", "DESCRIPTION");
		scrambleData("DOCUMENTS", "ADDRESS");
		scrambleData("DOCUMENTS", "ADDRESSFIRSTLINE");
		scrambleData("DOCUMENTS", "CUSTOMERREF");
		scrambleData("DOCUMENTS", "DELIVERYADDRESS");
		scrambleData("DOCUMENTS", "MESSAGE");
		scrambleData("DOCUMENTS", "PAYVALUE");
		scrambleData("DOCUMENTS", "SHIPPING");
		scrambleData("DOCUMENTS", "SHIPPINGVAT");
		scrambleData("DOCUMENTS", "TOTAL");
		scrambleData("DOCUMENTS", "MESSAGE2");
		scrambleData("DOCUMENTS", "MESSAGE3");
		scrambleData("DOCUMENTS", "CONSULTANT");
		
		scrambleData("ITEMS", "DESCRIPTION");
		scrambleData("ITEMS", "ITEMNR");
		scrambleData("ITEMS", "NAME");
		scrambleData("ITEMS", "PRICE");
		scrambleData("ITEMS", "VATVALUE");
		scrambleData("ITEMS", "PICTURENAME");
		
		scrambleData("EXPENDITURES", "DOCUMENTNR");
		scrambleData("EXPENDITURES", "NAME");
		scrambleData("EXPENDITURES", "NR");
		scrambleData("EXPENDITURES", "PAID");
		scrambleData("EXPENDITURES", "TOTAL");

		scrambleData("RECEIPTVOUCHERS", "DOCUMENTNR");
		scrambleData("RECEIPTVOUCHERS", "NAME");
		scrambleData("RECEIPTVOUCHERS", "NR");
		scrambleData("RECEIPTVOUCHERS", "PAID");
		scrambleData("RECEIPTVOUCHERS", "TOTAL");

		scrambleData("EXPENDITUREITEMS", "NAME");
		scrambleData("EXPENDITUREITEMS", "PRICE");

		scrambleData("RECEIPTVOUCHERITEMS", "NAME");
		scrambleData("RECEIPTVOUCHERITEMS", "PRICE");
		
		scrambleProperty("YOURCOMPANY_COMPANY_NAME");
		scrambleProperty("YOURCOMPANY_COMPANY_OWNER");
		scrambleProperty("YOURCOMPANY_COMPANY_STREET");
		scrambleProperty("YOURCOMPANY_COMPANY_ZIP");
		scrambleProperty("YOURCOMPANY_COMPANY_CITY");
		scrambleProperty("YOURCOMPANY_COMPANY_FAX");
		scrambleProperty("YOURCOMPANY_COMPANY_EMAIL");
		scrambleProperty("YOURCOMPANY_COMPANY_WEBSITE");
		scrambleProperty("YOURCOMPANY_COMPANY_VATNR");
		scrambleProperty("WEBSHOP_URL");
		scrambleProperty("WEBSHOP_USER");
		scrambleProperty("WEBSHOP_PASSWORD");
		scrambleProperty("YOURCOMPANY_COMPANY_TAXOFFICE");
		scrambleProperty("YOURCOMPANY_COMPANY_BANKACCOUNTNR");
		scrambleProperty("YOURCOMPANY_COMPANY_BANKCODE");
		scrambleProperty("YOURCOMPANY_COMPANY_BANK");
		scrambleProperty("YOURCOMPANY_COMPANY_IBAN");
		scrambleProperty("YOURCOMPANY_COMPANY_BIC");
		scrambleProperty("YOURCOMPANY_CREDITORID");

		// Write the scrambled data to the output file
		writeScrambledData();

	}
}
