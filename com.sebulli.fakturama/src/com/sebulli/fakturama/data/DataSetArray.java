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

package com.sebulli.fakturama.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * Array List of UniDataSets This list is used to store all the data sets like
 * documents, contacts, products ...
 * 
 * @author Gerd Bartelt
 * 
 * @param <T>
 */
public class DataSetArray<T> {

	// The Array List to store the data
	private ArrayList<T> datasets = new ArrayList<T>();

	// Reference to the data base
	private DataBase db = null;

	private UniDataSet udsTemplate;
	private int categoryStringsCode = 0;
	private Properties oldProps = new Properties();

	/**
	 * Constructor
	 */
	public DataSetArray() {
		this.db = null;
	}

	/**
	 * Constructor When this constructor is used, the data base table is copied
	 * into this ArrayList
	 * 
	 * @param db
	 *            Data base
	 * @param udsTemplate
	 *            Template of the UniDataSet
	 */
	public DataSetArray(DataBase db, UniDataSet udsTemplate) {
		this.db = db;
		if (this.db != null)
			this.db.getTable(datasets, udsTemplate);
		this.udsTemplate = udsTemplate;
	}

	/**
	 * Gets the Template of this ArrayList
	 * 
	 * @return UniDataSet template
	 */
	public UniDataSet getTemplate() {
		return this.udsTemplate;
	}

	/**
	 * Get the next free ID
	 * 
	 * @return next free ID
	 */
	public int getNextFreeId() {
		int maxId = -1;
		for (T dataset : datasets) {
			if (maxId < ((UniDataSet) dataset).getIntValueByKey("id")) {
				maxId = ((UniDataSet) dataset).getIntValueByKey("id");
			}
		}
		return maxId + 1;
	}

	/**
	 * Add a data set to the list of data sets. The ID of the new data set is
	 * set to the next free id. Also this new data set is insert into the data
	 * base
	 * 
	 * @param dataset
	 *            New data set
	 * @return the new data set (with modified ID)
	 */
	public T addNewDataSet(T dataset) {
		((UniDataSet) dataset).setIntValueByKey("id", getNextFreeId());
		datasets.add(dataset);
		if (this.db != null)
			this.db.insertUniDataSet((UniDataSet) dataset);
		return datasets.get(datasets.size() - 1);
	}

	/**
	 * Add a data set to the list of data set, but do it only, if the data set
	 * is not yet existing.
	 * 
	 * @param dataset
	 *            New data set
	 * @return the new data set, or an the existing one
	 */
	public T addNewDataSetIfNew(T dataset) {

		// get an existing data set
		T testdataset = getExistingDataSet(dataset);
		if (testdataset != null)
			return testdataset;

		// create a new one, if it is not in the list
		return addNewDataSet(dataset);
	}

	/**
	 * get an existing data set
	 * 
	 * @param dataset
	 *            Search for this data set
	 * @return The data set that was found, or null
	 */
	public T getExistingDataSet(T dataset) {

		// Search the list for an existing data set and return it
		ArrayList<T> undeletedDatasets = getActiveDatasets();
		for (T testdataset : undeletedDatasets) {
			if (((UniDataSet) testdataset).isTheSameAs((UniDataSet) dataset)) { return testdataset; }
		}

		// nothing found
		return null;
	}

	/**
	 * Tests if there is an entry with the same value
	 * 
	 * @param key
	 *            Search the values from this key.
	 * @param value
	 *            The value to test
	 * @return TRUE, if there is an entry with the same value
	 */
	public boolean isExistingDataSet(String key, String value) {

		// Search the list for an existing data set with the specified value
		ArrayList<T> undeletedDatasets = getActiveDatasets();
		for (T testdataset : undeletedDatasets) {
			if (((UniDataSet) testdataset).getStringValueByKey(key).equalsIgnoreCase(value)) { return true; }
		}

		// nothing found
		return false;
	}

	/**
	 * Tests if there is an entry with the same value
	 * 
	 * @param key
	 *            Search the values from this key.
	 * @param value
	 *            The value to test
	 * @return TRUE, if there is an entry with the same value
	 */
	public boolean isExistingDataSet(UniDataSet uds, String key, String value) {

		// Search the list for an existing data set with the specified value
		ArrayList<T> undeletedDatasets = getActiveDatasets();
		for (T testdataset : undeletedDatasets) {
			// Do only test other datasets
			if (uds.getIntValueByKey("id") != ((UniDataSet) testdataset).getIntValueByKey("id"))
				if (((UniDataSet) testdataset).getStringValueByKey(key).equalsIgnoreCase(value)) { return true; }
		}

		// nothing found
		return false;
	}

	/**
	 * Test, if the data set is a new data set
	 * 
	 * @param dataset
	 *            Test this data set
	 * @return True, if it is new and not in the list
	 */
	public boolean isNew(T dataset) {
		return (getExistingDataSet(dataset) == null);
	}

	/**
	 * Update the data set in the data base
	 * 
	 * @param dataset
	 *            Data set to update
	 */
	public void updateDataSet(T dataset) {
		if (this.db != null)
			db.updateUniDataSet((UniDataSet) dataset);
	}

	/**
	 * Get the List of all data sets
	 * 
	 * @return all data sets
	 */
	public ArrayList<T> getDatasets() {
		return datasets;
	}

	/**
	 * Get a data set by its ID
	 * 
	 * @param id
	 *            ID of the data set
	 * @return The data set
	 */
	public T getDatasetById(int id) {
		try {
			return datasets.get(id);
		}
		catch (Exception e) {
			Logger.logError(e, "Fatal Error: ID " + Integer.toString(id) + " not in Dataset");
			// Return index 0 is not correct, but if index 0 exists, the system
			// is at least stable.
			// And if there is no data set - no chance, return a null
			if (datasets.size() > 0)
				return datasets.get(0);
			else
				return null;
		}
	}

	/**
	 * Get an array of strings with all undeleted data sets Return only those
	 * elements that are in the specified category and those where no category
	 * is set.
	 * 
	 * @param key
	 *            Key of the UniData value
	 * @param category
	 *            The preferred category. If it's empty, return all.
	 * @return Array of strings
	 */
	public String[] getStrings(String key, String category) {

		// get all undeleted data sets
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<T> undeletedDatasets = getActiveDatasetsPrefereCategory(category);

		// collect all Strings in a list ..
		for (T dataset : undeletedDatasets) {
			UniDataSet uds = (UniDataSet) dataset;
			list.add(uds.getStringValueByKey(key));
		}

		// .. and convert this list to an array
		return list.toArray(new String[0]);
	}

	/**
	 * Get an array of strings with all undeleted data sets
	 * 
	 * @param key
	 *            Key of the UniData value
	 * @return Array of strings
	 */
	public String[] getStrings(String key) {
		return getStrings(key, "");
	}

	/**
	 * Get an array of strings with all undeleted data sets from a given
	 * category
	 * 
	 * @param key
	 *            Key of the UniData value
	 * @param category
	 *            Only entries with this category will be returned
	 * @return Array of strings
	 */
	public String[] getStringsInCategory(String key, String category) {

		// get all undeleted data sets
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<T> undeletedDatasets = getActiveDatasets();

		// collect all Strings in a list ..
		for (T dataset : undeletedDatasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (uds.getStringValueByKey("category").equalsIgnoreCase(category)) {
				String s = uds.getStringValueByKey(key);
				if (!s.isEmpty())
					list.add(s);
			}
		}

		// Sort the list alphabetically
		Collections.sort(list);
		
		// .. and convert this list to an array
		return list.toArray(new String[0]);
	}

	/**
	 * Get an array of strings of all category strings.
	 * 
	 * If this ArraySet is an set of documents, then only the categories of the
	 * document types are returned, that are in use. e.g. If there is an type
	 * "invoice", the categories "invoice/paid" and "invoice/unpaid" are
	 * returned.
	 * 
	 * @return Array of all category strings
	 */
	public ArrayList<String> getCategoryStrings() {
		Properties props = new Properties();
		oldProps = new Properties();
		String category;
		ArrayList<T> undeletedDatasets = getActiveDatasets();

		// Remember, which document types are used
		boolean usedDocuments[] = { false, false, false, false, false, false, false, false, false, false};

		// It's an ArraySet of documents
		if (udsTemplate instanceof DataSetDocument) {

			// Scan all documents and mark all used document types.
			for (T dataset : undeletedDatasets) {
				DataSetDocument document = (DataSetDocument) dataset;
				int docType = document.getIntValueByKey("category");
				if (docType >= 0 && docType <= (usedDocuments.length-1)) {
					usedDocuments[docType] = true;
					categoryStringsCode |= 1 << docType;
				}
			}

			// Get the category strings of all marked document types.
			return DataSetDocument.getCategoryStrings(usedDocuments);

		}

		// It's not an ArraySet of documents - so collect all category strings
		else {

			// Copy all strings to a Property object.
			// In a property object, there are no duplicate objects
			for (T dataset : undeletedDatasets) {
				UniDataSet uds = (UniDataSet) dataset;
				category = uds.getCategory();
				if (!category.isEmpty()) {
					oldProps.setProperty(category, category);
					props.setProperty(category, category);
				}
			}
		}

		// return the category strings
		return new ArrayList<String>(props.stringPropertyNames());
	}

	/**
	 * Test, if the category strings have changed
	 * 
	 * @return True, if they have changed
	 */
	public boolean getCategoryStringsChanged() {
		Properties props = new Properties();
		String category;

		// If the DataSetArray contains a set of document, 
		// test, if the code of used documents has changed. 
		if (udsTemplate instanceof DataSetDocument) {

			int oldCcategoryStringsCode = categoryStringsCode;
			// generate the new code
			getCategoryStrings();
			// compare the new code with the old one
			return oldCcategoryStringsCode != categoryStringsCode;

		}
		// If it's not a set of documents, compare all category strings
		// This is done by filling a property object with the strings ..
		else {
			ArrayList<T> undeletedDatasets = getActiveDatasets();
			for (T dataset : undeletedDatasets) {
				UniDataSet uds = (UniDataSet) dataset;
				category = uds.getCategory();
				if (!category.isEmpty()) {
					props.setProperty(category, category);
				}
			}
		}

		// .. an testing, if all the entries are in the old Property object ..
		for (Iterator<Object> iterator = props.keySet().iterator(); iterator.hasNext();) {
			if (!oldProps.containsKey(iterator.next()))
				return true;
		}

		// .. and if alle the old entries are in the new one.
		for (Iterator<Object> iterator = oldProps.keySet().iterator(); iterator.hasNext();) {
			if (!props.containsKey(iterator.next()))
				return true;
		}
		return false;
	}

	/**
	 * Resets the memory with the old category strings
	 */
	public void resetCategoryChanged() {

		// reset the categoryStringCode, if this is a set of documents
		categoryStringsCode = 0;

		// reset the old properties for all the rest
		oldProps = new Properties();

	}

	/**
	 * Get a data set by a double value Return only those elements that are in
	 * the specified category and those where no category is set.
	 * 
	 * @param key
	 *            Key to use for the search
	 * @param value
	 *            Double value to search for
	 * @param category
	 *            The preferred category. If it's empty, return all.
	 * @return ID of the first data set with the same value (or -1, if there is
	 *         nothing)
	 */
	public int getDataSetByDoubleValue(String key, Double value, String category) {
		ArrayList<T> undeletedDatasets = getActiveDatasetsPrefereCategory(category);

		for (T dataset : undeletedDatasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (DataUtils.DoublesAreEqual(uds.getDoubleValueByKey(key), value)) {
				int i = ((UniDataSet) dataset).getIntValueByKey("id");
				return i;
			}
		}
		return -1;
	}

	/**
	 * Get a data set by a double value
	 * 
	 * @param key
	 *            Key to use for the search
	 * @param value
	 *            Double value to search for
	 * @return ID of the first data set with the same value (or -1, if there is
	 *         nothing)
	 */
	public int getDataSetByDoubleValue(String key, Double value) {
		return getDataSetByDoubleValue(key, value, "");
	}

	/**
	 * Get the ID of a data set by a string value Return only those elements
	 * that are in the specified category and those where no category is set.
	 * 
	 * @param key
	 *            Key to use for the search
	 * @param value
	 *            String value to search for
	 * @param category
	 *            The preferred category. If it's empty, return all.
	 * @return ID of the first data set with the same value (or -1, if there is
	 *         nothing)
	 */
	public int getDataSetIDByStringValue(String key, String value, String category) {
		ArrayList<T> undeletedDatasets = getActiveDatasetsPrefereCategory(category);

		for (T dataset : undeletedDatasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (uds.getStringValueByKey(key).equals(value)) {
				int i = ((UniDataSet) dataset).getIntValueByKey("id");
				return i;
			}
		}
		return -1;
	}

	/**
	 * Get the ID of a data set by a string value
	 * 
	 * @param key
	 *            Key to use for the search
	 * @param value
	 *            String value to search for
	 * @return ID of the first data set with the same value (or -1, if there is
	 *         nothing)
	 */
	public int getDataSetIDByStringValue(String key, String value) {
		return getDataSetIDByStringValue(key, value, "");
	}

	/**
	 * Get a data set by a string value Return only those elements that are in
	 * the specified category and those where no category is set.
	 * 
	 * @param key
	 *            Key to use for the search
	 * @param value
	 *            String value to search for
	 * @param category
	 *            The preferred category. If it's empty, return all.
	 * @return The first data set with the same value (or -null, if there is
	 *         nothing)
	 */
	public T getDataSetByStringValue(String key, String value, String category) {
		ArrayList<T> undeletedDatasets = getActiveDatasetsPrefereCategory(category);

		for (T dataset : undeletedDatasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (uds.getStringValueByKey(key).equals(value)) { return (dataset); }
		}
		return null;
	}

	/**
	 * Get a data set by a string value
	 * 
	 * @param key
	 *            Key to use for the search
	 * @param value
	 *            String value to search for
	 * @return The first data set with the same value (or -null, if there is
	 *         nothing)
	 */
	public T getDataSetByStringValue(String key, String value) {
		return getDataSetByStringValue(key, value, "");
	}

	/**
	 * Get the data sets with a specified category and name
	 * 
	 * @param category
	 *            the category
	 * @param name
	 *            the name
	 * 
	 * @return The specified dataset
	 */
	public T getDatasetByCategoryAndName(String category, String name) {
		ArrayList<T> undeletedDatasets = getActiveDatasets();

		for (T dataset : undeletedDatasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (uds.getStringValueByKey("category").equalsIgnoreCase(category) && uds.getStringValueByKey("name").equalsIgnoreCase(name)) { return (dataset); }
		}
		return null;
	}

	/**
	 * Get the data sets with a specified name
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return The specified dataset
	 */
	public T getDatasetByName(String name) {
		ArrayList<T> undeletedDatasets = getActiveDatasets();

		for (T dataset : undeletedDatasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (uds.getStringValueByKey("name").equalsIgnoreCase(name)) { return (dataset); }
		}
		return null;
	}

	/**
	 * Get all active (undeleted) data sets
	 * 
	 * @return ArrayList with all undeleted data sets
	 */
	public ArrayList<T> getActiveDatasets() {
		ArrayList<T> undeletedDatasets = new ArrayList<T>();
		for (T dataset : datasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (!uds.getBooleanValueByKey("deleted")) {
				undeletedDatasets.add(dataset);
			}
		}
		return undeletedDatasets;
	}

	public ArrayList<T> getAllDatasets() {
		ArrayList<T> undeletedDatasets = new ArrayList<T>();
		for (T dataset : datasets) {
			undeletedDatasets.add(dataset);
		}
		return undeletedDatasets;
	}
	
	/**
	 * Get all active (undeleted) data sets with a specified category
	 * 
	 * @param category
	 *            the category
	 * 
	 * @return ArrayList with all undeleted data sets
	 */
	public ArrayList<T> getActiveDatasetsByCategory(String category) {
		ArrayList<T> filteredDatasets = new ArrayList<T>();
		for (T dataset : datasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (!uds.getBooleanValueByKey("deleted") && uds.getStringValueByKey("category").equalsIgnoreCase(category)) {
				filteredDatasets.add(dataset);
			}
		}
		return filteredDatasets;
	}

	/**
	 * Get all active (undeleted) data sets with a specified category
	 * 
	 * Return only those elements that are in the specified category and those
	 * where no category is set.
	 * 
	 * @param category
	 *            The preferred category. If it's empty, return all.
	 * 
	 * @return ArrayList with all undeleted data sets
	 */
	public ArrayList<T> getActiveDatasetsPrefereCategory(String category) {
		ArrayList<T> filteredDatasets = new ArrayList<T>();
		for (T dataset : datasets) {
			UniDataSet uds = (UniDataSet) dataset;
			if (!uds.getBooleanValueByKey("deleted")) {

				// Use the specified category
				if (category.isEmpty() || uds.getStringValueByKey("category").equals(category) || uds.getStringValueByKey("category").isEmpty())
					filteredDatasets.add(dataset);
			}
		}
		return filteredDatasets;
	}

	/**
	 * Returns the next dataset
	 * 
	 * @param thisDataSet
	 * 		
	 * @return
	 * 		The next dataset in the list
	 */
	public T getNextDataSet(T thisDataSet ) {
		ArrayList<T> activeDatasets = getActiveDatasets();
		
		boolean found = false;
		
		for (T dataset : activeDatasets) {
			if (found)
				return dataset;
			if (dataset.equals(thisDataSet))
				found = true;
		}
		return null;
	}
	/**
	 * Returns the previous dataset
	 * 
	 * @param thisDataSet
	 * 		
	 * @return
	 * 		The previous dataset in the list
	 */
	public T getPreviousDataSet(T thisDataSet ) {
		ArrayList<T> activeDatasets = getActiveDatasets();
		
		T previousDataset = null;
		
		for (T dataset : activeDatasets) {
			if (dataset.equals(thisDataSet))
				return previousDataset;
			previousDataset = dataset;
		}
		return null;
	}
	
	/**
	 * Swap the position of 2 elements
	 * 
	 * @param 
	 * 		element1
	 * @param 
	 * 		element2
	 */
	public void swapPosition(int element1, int element2) {
		T uds1 =  datasets.get(element1);
		datasets.set(element1, datasets.get(element2));
		datasets.set(element2, (T) uds1);
	}
	
}
