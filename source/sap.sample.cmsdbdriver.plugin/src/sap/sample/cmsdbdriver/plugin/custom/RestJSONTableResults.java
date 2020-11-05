package sap.sample.cmsdbdriver.plugin.custom;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.businessobjects.foundation.logging.ILogger;
import com.businessobjects.foundation.logging.LoggerManager;
import com.sap.connectivity.cs.java.drivers.cms.CMSDBDriverException;
import com.sap.connectivity.cs.java.drivers.cms.api.CustomObject;
import com.sap.connectivity.cs.java.drivers.cms.api.IQueryElement;
import com.sap.connectivity.cs.java.drivers.cms.sqlparser.ColumnDefinition;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.IUnvTable;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.UnvTableFieldDef;
import com.sap.sample.boe.services.RESTRequest;

import sap.sample.cmsdbdriver.plugin.boe.IJsonRecord;
import sap.sample.cmsdbdriver.plugin.boe.JSONHelper;
import sap.sample.cmsdbdriver.plugin.core.IResultPlugin;
import sap.sample.cmsdbdriver.plugin.core.IResultTable;
import sap.sample.cmsdbdriver.plugin.core.PluginBase;

public class RestJSONTableResults extends IResultTable implements IUnvTable {

	private static final ILogger LOG = LoggerManager.getLogger(RestJSONTableResults.class);
	
	private static final String TABLE_NAME = "Rest_JSON";

	private static final String LEVEL = "Level";
	private static final String PATH = "Path";
	private static final String NAME = "Name";
	private static final String VALUE = "Value";
	
	final private Map<String, UnvTableFieldDef> columns = new HashMap<String, UnvTableFieldDef>();
	final private PluginBase pluginBase;
	private RESTRequest request;
	
	private String rest_call;

	/**
	 * Define the list of Fields for the virtual table
	 */
	public RestJSONTableResults(IResultPlugin plugin) {
		super(plugin);
		columns.put(LEVEL, new UnvTableFieldDef(LEVEL, Types.INTEGER));
		columns.put(PATH, new UnvTableFieldDef(PATH, Types.VARCHAR));
		columns.put(NAME, new UnvTableFieldDef(NAME, Types.VARCHAR));
		columns.put(VALUE, new UnvTableFieldDef(VALUE, Types.VARCHAR));
		pluginBase = (PluginBase)plugin;
	}
	
	/**
	 * Returns the name of the virtual table
	 * @return virtual table name
	 */
	@Override
	public String getName() {
		return TABLE_NAME;
	}

	@Override
	/**
	 * Returns the fields defined for the virtual table
	 * @return list of table fields
	 */
	public Map<String, UnvTableFieldDef> getTableFields() {
		return this.columns;
	}

	@Override
	/**
	 * use the getQueryElement() or getIds() to prepare data for setValues()
	 */
	public void initialize(IQueryElement queryElement, Set<Integer> ids) {
		// nothing to initialize/prepare
		//int i = queryElement.getColumns().size();
		for (int i=0; i<queryElement.getColumns().size(); i++) {
			ColumnDefinition cDef = queryElement.getColumns().get(i);
			String s = cDef.getColumnName();  // REST_DP
		}
		rest_call = queryElement.getCondition().operands().get(1).toString();
		rest_call = rest_call.substring(1, rest_call.length() - 1);
	}

	@Override
	public void setValues(int id) throws CMSDBDriverException {
		// get the request instance and create a session
		if (request == null) {
			request = pluginBase.getRequest();
		}
		try {
			JSONObject json = request.sendRequestJSON(request.getWEBI_RWS() + "/documents/" + id + rest_call, "GET", null);

			JSONHelper jh = new JSONHelper(new IJsonRecord() {
				
				@Override
				public void setRow(int level, String path, String property, String value, String filterPath) throws CMSDBDriverException {
					if (getCustomObject() == null) setCustomObject(new CustomObject());
					setObjectProperty(TABLE_NAME + "." + RestJSONTableResults.LEVEL,
							Integer.class.getName(), level);
					setObjectProperty(TABLE_NAME + "." + RestJSONTableResults.PATH,
							String.class.getName(), path);
					setObjectProperty(TABLE_NAME + "." + RestJSONTableResults.NAME,
							String.class.getName(), property);
					setObjectProperty(TABLE_NAME + "." + RestJSONTableResults.VALUE,
							String.class.getName(), value);
					addRow(id);
					
				}
			});
			jh.iterate(json, false);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

}

