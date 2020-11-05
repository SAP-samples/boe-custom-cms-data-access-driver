package sap.sample.cmsdbdriver.plugin.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.businessobjects.foundation.logging.ILogger;
import com.businessobjects.foundation.logging.LoggerManager;
import com.sap.connectivity.cs.java.drivers.cms.CMSDriverConnection;
import com.sap.connectivity.cs.java.drivers.cms.api.IDetailsProvider;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.IUnvTable;
import com.sap.sample.boe.services.RESTRequest;

public class PluginBase implements IResultPlugin {

	private static final ILogger LOG = LoggerManager.getLogger(PluginBase.class);
	
    private final List<IUnvTable> unvTables = new ArrayList<IUnvTable>();
    private final Map<String, IResultTable> resultTableMap = new HashMap<String, IResultTable>();
    private final IDetailsProvider detailsProvider;
    private CMSDriverConnection connection;
    private RESTRequest request;

    /**
     * Add Details provider to the Access Driver plugin
     * @param connection 
     */
    public PluginBase(final CMSDriverConnection connection) {  
        this.detailsProvider = new DetailsProvider(this);
        this.connection = connection;
    }

    @Override
    /**
     * Returns the list of virtual tables defined in the plugin
	 * @return list of virtual tables
     */
    public List<IUnvTable> getTables() {
        return unvTables;
    }

    @Override
    /**
     * Returns the Details provider for the virtual table(s)
	 * @return Details provider
     */
    public IDetailsProvider getDetailsProvider() {
        return detailsProvider;
    }    

    /**
     * Get ResultTable for the UnvTable
     * @param name
     * @return
     */
    public IResultTable getResultTable(String name) {
    	return this.resultTableMap.get(name);
    }
    
    /**
     * Add UnvTable and ResultTable to the List/Map
     * @param table
     */
    public void addTable(IUnvTable table) {
    	this.resultTableMap.put(table.getName(), (IResultTable)table);
        this.unvTables.add(table);
    }

	@Override
	/**
	 * Must be implemented by the plugin which is using this base class
	 */
	public String getId() {
		return null;
	}

	@Override
	public CMSDriverConnection getConnection() {
		return connection;
	}

	/**
	 * Get the existing Request Object or create the Request Object and create a new Session
	 * @return
	 */
	public RESTRequest getRequest() {
		if (request != null) return request;
    	try {
        	this.request = new RESTRequest();
			this.request.connectNewSession(connection.queryCMS(RESTRequest.BASE_URL), connection.getSerializedSession());
		} catch (Exception e) {
			LOG.error(e);
		}
		return request;
	}
}
