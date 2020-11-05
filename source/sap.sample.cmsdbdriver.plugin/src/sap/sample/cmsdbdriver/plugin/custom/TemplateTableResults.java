package sap.sample.cmsdbdriver.plugin.custom;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sap.connectivity.cs.java.drivers.cms.CMSDBDriverException;
import com.sap.connectivity.cs.java.drivers.cms.api.IQueryElement;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.IUnvTable;
import com.sap.connectivity.cs.java.drivers.sdk.datafoundation.UnvTableFieldDef;

import sap.sample.cmsdbdriver.plugin.core.IResultPlugin;
import sap.sample.cmsdbdriver.plugin.core.IResultTable;

public class TemplateTableResults extends IResultTable implements IUnvTable {

	private static final String TABLE_NAME = "Tutorial_Results";

	private static final String NO = "no";
	private static final String TEXT = "text";
	
	final private Map<String, UnvTableFieldDef> columns = new HashMap<String, UnvTableFieldDef>();

	/**
	 * Define the list of Fields for the virtual table
	 */
	public TemplateTableResults(IResultPlugin plugin) {
		super(plugin);
		columns.put(NO, new UnvTableFieldDef(NO, Types.INTEGER));
		columns.put(TEXT, new UnvTableFieldDef(TEXT, Types.VARCHAR));
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
	}

	@Override
	public void setValues(int id) throws CMSDBDriverException {
		setObjectProperty(TABLE_NAME + "." + TemplateTableResults.NO,
				Integer.class.getName(), id);
		setObjectProperty(TABLE_NAME + "." + TemplateTableResults.TEXT,
				String.class.getName(), id + ": text");
		addRow(id);
	}

}

