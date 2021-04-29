package sap.sample.cmsdbdriver.plugin.custom;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crystaldecisions.sdk.occa.infostore.CePropertyID;
import com.crystaldecisions.sdk.occa.infostore.IDestination;
import com.crystaldecisions.sdk.occa.infostore.IDestinations;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IProcessingInfo;
import com.crystaldecisions.sdk.occa.infostore.ISchedulingInfo;
import com.crystaldecisions.sdk.properties.IProperties;
import com.crystaldecisions.sdk.properties.IProperty;
import com.sap.connectivity.cs.java.drivers.cms.CMSDBDriverException;
import com.sap.connectivity.cs.java.drivers.cms.api.CustomObject;
import com.sap.connectivity.cs.java.drivers.cms.api.IQueryElement;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.IUnvTable;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.UnvTableFieldDef;
import com.sap.connectivity.cs.sqlparser.Condition.Operand;

import sap.sample.cmsdbdriver.plugin.core.IResultPlugin;
import sap.sample.cmsdbdriver.plugin.core.IResultTable;
import sap.sample.cmsdbdriver.plugin.core.PluginBase;

public class ScheduleInfoTableResults extends IResultTable implements IUnvTable {

	private static final String TABLE_NAME = "ScheduleInfo";

	private static final String NO = "no";
	private static final String DETAILS = "Details";
	private static final String VALUE = "PropertyValue";
	private static final String LEVEL = "PropertyLevel";
	private static final String PATH = "PropertyPath";
	private static final String NAME = "PropertyName";
	private static final String IS_CONTAINER = "PropertyIsContainer";
	
	final private PluginBase pluginBase;
	
	final private Map<String, UnvTableFieldDef> columns = new HashMap<String, UnvTableFieldDef>();
	private String queryFields;

	/**
	 * Define the list of Fields for the virtual table
	 */
	public ScheduleInfoTableResults(IResultPlugin plugin) {
		super(plugin);
		columns.put(NO, new UnvTableFieldDef(NO, Types.INTEGER));
		columns.put(DETAILS, new UnvTableFieldDef(DETAILS, Types.VARCHAR));
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
		queryFields = "";
		for (int i=0; i<queryElement.getCondition().operands().size(); i++) {
			if (queryElement.getCondition().operands().get(i).toString().contentEquals(TABLE_NAME + "." + DETAILS)) {
				queryFields = queryElement.getCondition().operands().get(i + 1).toString();
				queryFields = queryFields.trim().replaceAll("'", "").replaceAll("\\s+", ",");
			}
		}
	}

	
	@Override
	public void setValues(int id) throws CMSDBDriverException {
		IInfoObjects infoObjects = pluginBase.getConnection().queryCMS("select si_id, " + queryFields + "  from ci_infoobjects where si_id = " + id);
		// should not be the case
		if (infoObjects == null) return;
		if (infoObjects.size() == 0) return;
		IInfoObject infoObject = (IInfoObject)infoObjects.get(0);
		//IProperties properties = infoObject.properties().getProperties(CePropertyID.SI_DESTINATIONS);
		
		String fields[] = queryFields.split(",");
		for (int i=0; i<fields.length; i++) {
			fields[i] = fields[i].trim();
			String[] subFields = fields[i].split("\\.");
			if (subFields[0].equalsIgnoreCase("si_scheduleinfo")) {
				ISchedulingInfo sInfo = infoObject.getSchedulingInfo();
				if (subFields.length == 1) {
					setValues(id, "SchedulingInfo", sInfo.properties(), 0, "");
				} else {
					IProperty prop = sInfo.properties().getProperty(subFields[1]);
					if (prop != null) {
						if (prop.isContainer()) {
							setValues(id, "SchedulingInfo", (IProperties)prop.getValue(), 1, CePropertyID.idToName(prop.getID()));
						} else {
							if (getCustomObject() == null) setCustomObject(new CustomObject());
							setObjectProperties("SchedulingInfo", 1, "", prop);
							addRow(id);
						}
					}
				}
			} else if (subFields[0].equalsIgnoreCase("si_processinfo")) {
				IProcessingInfo pInfo = infoObject.getProcessingInfo();
				if (subFields.length == 1) {
					setValues(id, "ProcessingInfo", pInfo.properties(), 0, "");
				} else {
					IProperty prop = pInfo.properties().getProperty(subFields[1]);
					if (prop != null) {
						if (prop.isContainer()) {
							setValues(id, "ProcessingInfo", (IProperties)prop.getValue(), 1, CePropertyID.idToName(prop.getID()));
						} else {
							if (getCustomObject() == null) setCustomObject(new CustomObject());
							setObjectProperties("ProcessingInfo", 1, "", prop);
							addRow(id);
						}
					}
				}
			}
		}
	}
	
	private void setValues(int id, String text, IProperties properties, int level, String path) throws CMSDBDriverException {
		String s = "";
		Set<Integer> keys = properties.keySet();
		for (int key : keys) {
			if (getCustomObject() == null) setCustomObject(new CustomObject());
			IProperty prop = properties.getProperty(key);
			setObjectProperties(text, level, path, prop);
			if (prop.isContainer()) {
				setValues(id, text, (IProperties)prop.getValue(), level + 1, path + (path.length() > 0 ? "." : "") + CePropertyID.idToName(prop.getID()));
			} else {
				addRow(id);
			}
		}
	}
	
	private void setObjectProperties(String text, int level, String path, IProperty prop) {
		setObjectProperty(TABLE_NAME + "." + ScheduleInfoTableResults.DETAILS,
				String.class.getName(), text);
		setObjectProperty(TABLE_NAME + "." + ScheduleInfoTableResults.LEVEL,
				Integer.class.getName(), level);
		setObjectProperty(TABLE_NAME + "." + ScheduleInfoTableResults.PATH,
				String.class.getName(), path);
		setObjectProperty(TABLE_NAME + "." + ScheduleInfoTableResults.NAME,
				String.class.getName(), CePropertyID.idToName(prop.getID()));
		setObjectProperty(TABLE_NAME + "." + ScheduleInfoTableResults.IS_CONTAINER,
				Boolean.class.getName(), prop.isContainer());
		if (prop.isContainer()) return;
		setObjectProperty(TABLE_NAME + "." + ScheduleInfoTableResults.VALUE,
				String.class.getName(), "" + prop.getValue());
		
	}
}

