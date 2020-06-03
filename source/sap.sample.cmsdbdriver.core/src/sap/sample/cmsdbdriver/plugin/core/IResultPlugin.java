package sap.sample.cmsdbdriver.plugin.core;

import java.util.List;

import com.sap.connectivity.cs.java.drivers.cms.CMSDriverConnection;
import com.sap.connectivity.cs.java.drivers.cms.api.IDetailsProvider;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.IUnvTable;

public interface IResultPlugin {

    public List<IUnvTable> getTables();
    
    public IDetailsProvider getDetailsProvider();
    
    public CMSDriverConnection getConnection();
    
    public String getId();
    
    public IResultTable getResultTable(String name);
    
    public void addTable(IUnvTable table);
    
}
