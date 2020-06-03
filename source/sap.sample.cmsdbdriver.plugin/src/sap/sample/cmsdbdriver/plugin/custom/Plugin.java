package sap.sample.cmsdbdriver.plugin.custom;

import java.util.List;

import com.sap.connectivity.cs.java.drivers.cms.CMSDriverConnection;
import com.sap.connectivity.cs.java.drivers.cms.api.IDetailsProvider;
import com.sap.connectivity.cs.java.drivers.cms.api.IPlugin;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.IUnvTable;

import sap.sample.cmsdbdriver.plugin.core.PluginBase;

public class Plugin implements IPlugin {
	
	private final PluginBase pluginBase;

    /**
     * Add virtual Table(s) and Details provider to the Access Driver plugin
     * @param connection 
     */
    public Plugin(final CMSDriverConnection connection) {  
    	pluginBase = new PluginBase(connection);
    	pluginBase.addTable(new TemplateTableResults(pluginBase));
    }

	@Override
	public IDetailsProvider getDetailsProvider() {
		return pluginBase.getDetailsProvider();
	}

	@Override
	public String getId() {
		return Plugin.class.getName();
	}

	@Override
	public List<IUnvTable> getTables() {
		return pluginBase.getTables();
	}

}
