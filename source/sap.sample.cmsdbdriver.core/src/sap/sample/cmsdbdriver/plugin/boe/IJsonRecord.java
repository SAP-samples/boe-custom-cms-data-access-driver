package sap.sample.cmsdbdriver.plugin.boe;

import com.sap.connectivity.cs.java.drivers.cms.CMSDBDriverException;

public interface IJsonRecord {

	public void setRow(int level, String path, String key, String value, String filterPath) throws CMSDBDriverException;
}
