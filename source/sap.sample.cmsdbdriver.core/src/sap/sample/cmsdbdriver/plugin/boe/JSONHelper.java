package sap.sample.cmsdbdriver.plugin.boe;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sap.connectivity.cs.java.drivers.cms.CMSDBDriverException;

public class JSONHelper {
	
	private IJsonRecord record;
	
	public JSONHelper(IJsonRecord record) {
		this.record = record;
	}

	public void iterate(JSONObject oJSON, boolean useAsFilter) throws JSONException, CMSDBDriverException {
		iterate(0, oJSON, "", "", useAsFilter);
	}
	
	/**
	 * 
	 * @param level
	 * @param oJSON
	 * @param path
	 * @param filterPath contains <no> instead of array index, consumer can use this to filter entries
	 * @param useAsFilter return <no> instead of array index
	 * @throws JSONException
	 * @throws CMSDBDriverException
	 */
	private void iterate(int level, JSONObject oJSON, String path, String filterPath, boolean useAsFilter) throws JSONException, CMSDBDriverException {
		Iterator<String> keysItr = oJSON.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object oEntry = oJSON.get(key);
			//iterateObject(prefix, oEntry, path, key);
			/* */
			if (oEntry instanceof JSONArray) {
				JSONArray entries = (JSONArray)oEntry;
				record.setRow(level, path, key, (useAsFilter ? "" : "" + entries.length()), filterPath);
				for (int i=0; i<entries.length(); i++) {
					Object arEntry = entries.get(i);
					if (arEntry instanceof JSONObject) {
						iterate(level + 1, entries.getJSONObject(i), path + "\\" + key + "\\" + (useAsFilter ? "<no>" : i), path + "\\" + key + "\\<no>", useAsFilter);
					} else {
						record.setRow(level, path, key, "" + arEntry, filterPath);
					}
					if (useAsFilter) i = entries.length();
				}				
			} else if (oEntry instanceof JSONObject) {
				//System.out.println(entry.getClass().toString());
				JSONObject jEntry = (JSONObject)oEntry;
				//System.out.println(jEntry.length() + ": " + jEntry.toString());
				record.setRow(level, path, key, "" + jEntry.length(), filterPath);
				iterate(level + 1, jEntry, path + "\\" + key, filterPath, useAsFilter);
			} else {
				// path += key;
				record.setRow(level, path, key,  "" + oEntry, filterPath);
			}
			/* */
		}
	}

}
