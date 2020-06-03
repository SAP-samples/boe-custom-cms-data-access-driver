package sap.sample.cmsdbdriver.plugin.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.businessobjects.connectionserver.datasources.ddk.DDKException;
import com.sap.connectivity.cs.java.drivers.cms.CMSDBDriverException;
import com.sap.connectivity.cs.java.drivers.cms.api.CustomObject;
import com.sap.connectivity.cs.java.drivers.cms.api.CustomObjectHelper;
import com.sap.connectivity.cs.java.drivers.cms.api.ICustomObject;
import com.sap.connectivity.cs.java.drivers.cms.api.IDetailsProvider;
import com.sap.connectivity.cs.java.drivers.cms.api.IQueryElement;

public class DetailsProvider implements IDetailsProvider {

	final private IResultPlugin plugin;
    final private Map<Integer, List<ICustomObject>> customObjects;

    /**
     * Create DetailsProvider Instance for the Access Driver plugin
     * @param plugin
     */
    public DetailsProvider(IResultPlugin plugin) {
    	this.plugin = plugin;
    	this.customObjects = new HashMap<Integer, List<ICustomObject>>();
    }
    
	@Override
    /**
     * Return the details for the specified id
     * @param id InfoObject identifier SI_ID
     * @return custom objects for the specified id
     * @throws DDKException
     */
    public List<ICustomObject> getDetails(Integer id) throws DDKException {
    	return this.customObjects.get(id);
	}

	@Override
	/**
	 * Prepare the details for the custom objects 
	 * @param qe
	 * @param ids
	 * @throws DDKException
	 */
	public void initializeDetails(IQueryElement queryElement, Set<Integer> ids) throws DDKException {
		// no InfoObject ids 
		if (ids.isEmpty() || queryElement.getColumns().size() == 0) return;
		// initialize
		customObjects.clear();
		IResultTable resultTable = plugin.getResultTable(queryElement.getColumns().get(0).getTableName());
		resultTable.initializeDetails(queryElement, ids);
		for (int id : ids) {
			resultTable.setCustomObject(new CustomObject());
			resultTable.setValues(id);
		}
	}
	
	@Override
	/**
     * Returns the PluginBase identifier
	 * @return PluginBase identifier
	 */
	public String pluginId() {
		return plugin.getId();
	}

	/**
	 * Add the result set as row to the specified InfoObject Id
	 * @param id
	 * @param resultSet
	 * @throws CMSDBDriverException
	 */
	public void addRow(int id, IResultTable resultSet) throws CMSDBDriverException {
		if (resultSet.getCustomObject() == null) return;
		// Filter using condition and store in objects map
		if (CustomObjectHelper.filterCustomObject(resultSet.getCustomObject(), resultSet.getQueryElement().getCondition(), 
				resultSet.getQueryElement().getRestrictions())) {
			List<ICustomObject> currentList = customObjects.get(id);
			if (currentList == null) {
				currentList = new ArrayList<ICustomObject>();
				customObjects.put(id, currentList);
			}
			currentList.add(resultSet.getCustomObject());
		}
		resultSet.setCustomObject(null);
	}

}
