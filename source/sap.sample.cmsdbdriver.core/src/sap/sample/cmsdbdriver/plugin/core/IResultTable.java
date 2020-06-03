package sap.sample.cmsdbdriver.plugin.core;

import java.util.Set;

import com.sap.connectivity.cs.java.drivers.cms.CMSDBDriverException;
import com.sap.connectivity.cs.java.drivers.cms.api.CustomObject;
import com.sap.connectivity.cs.java.drivers.cms.api.IQueryElement;

public abstract class IResultTable {
	
	private IResultPlugin plugin;
    private CustomObject customObject;
    private IQueryElement queryElement;
    private Set<Integer> ids;

	protected IResultTable(IResultPlugin plugin) {
		this.plugin = plugin;
	}
    
	public abstract void initialize();
	
	public abstract void setValues(int id) throws CMSDBDriverException;
	
	public void setObjectProperty(final String id, final String type, final Object value) {
		customObject.setObjectProperty(id, type, value);
	}

	public void addRow(int id) throws CMSDBDriverException {
		((DetailsProvider)plugin.getDetailsProvider()).addRow(id, this);
	}
	
	public void setCustomObject(CustomObject customObject) {
		this.customObject = customObject;
	}

	public CustomObject getCustomObject() {
		return customObject;
	}

	public void initializeDetails(IQueryElement queryElement, Set<Integer> ids) {
		this.queryElement = queryElement;
		this.ids = ids;
		initialize();
	}

	public IQueryElement getQueryElement() {
		return queryElement;
	}

	public Set<Integer> getIds() {
		return ids;
	}
}
