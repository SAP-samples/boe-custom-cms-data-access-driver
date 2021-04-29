package Test;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.CePropertyID;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.occa.infostore.IProcessingInfo;
import com.crystaldecisions.sdk.occa.infostore.ISchedulingInfo;
import com.crystaldecisions.sdk.plugin.authentication.enterprise.IsecEnterpriseBase;
import com.crystaldecisions.sdk.properties.IProperties;
import com.crystaldecisions.sdk.properties.IProperty;

class test {

    private static String USER = "administrator";
    private static String PASSWORD = "Password1";
    private static String CMS = "localhost";
    //private static String CMS = "AURORA42-CONS";

	private static ISessionMgr sessionMgr = null;
    private static IEnterpriseSession eSession = null;
    private IInfoStore infoStore;

	@Test
	void test() throws SDKException {
		sessionMgr = CrystalEnterprise.getSessionMgr();
		eSession = sessionMgr.logon(USER, PASSWORD, CMS, IsecEnterpriseBase.PROGID);
		infoStore = (IInfoStore)eSession.getService("InfoStore");
		query(8555, "si_scheduleinfo.si_destinations  si_scheduleInfo.si_name  si_processinfo");
		eSession.logoff();
	}
	
	private void workOn(IProperties properties, int level, String path) {
		String s = "";
		Set<Integer> keys = properties.keySet();
		for (int key : keys) {
			IProperty prop = properties.getProperty(key);
			// System.out.println(prop.isContainer() + ": " + prop.toString());
			if (prop.isContainer()) {
				System.out.println(level + " - true - " + path + " - " + CePropertyID.idToName(prop.getID()));
				workOn((IProperties)prop.getValue(), level + 1, path + (path.length() > 0 ? "\\" : "") + CePropertyID.idToName(prop.getID()));
			} else {
				System.out.println(level + " - false - " + path + " - " + CePropertyID.idToName(prop.getID()) + ": " + prop.getValue());
			}
		}

/*		for (int i=0; i<properties.size(); i++) {
			s = CePropertyID.idToName(prop.getID()) + " : " + prop.isContainer();
		} 
*/	}

	private void query(int id, String field) throws SDKException {
		
		String[] condFields = field.split(" ");
		field = field.trim().replaceAll("'", "").replaceAll("\\s+", ",");
		
		//IInfoObjects infoObjects = infoStore.query("select si_id, si_scheduleinfo.si_destinations  from ci_infoobjects where si_id = " + id);
		IInfoObjects infoObjects = infoStore.query("select si_id, " + field + " from ci_infoobjects where si_id = " + id);
		// should not be the case
		if (infoObjects == null) return;
		if (infoObjects.size() == 0) return;
		IInfoObject infoObject = (IInfoObject)infoObjects.get(0);
		//IProperties properties = infoObject.properties().getProperties(CePropertyID.SI_DESTINATIONS);
		
		//properties = properties.getProperties("si_destinations");
		String s = "";
		outProperties("query", infoObject.properties());
		IProperty prop = infoObject.properties().getProperty("si_scheduleInfo");

		String fields[] = field.split(",");
		for (int i=0; i<fields.length; i++) {
			fields[i] = fields[i].trim();
			String[] subFields = fields[i].split("\\.");
			if (subFields[0].equalsIgnoreCase("si_scheduleinfo")) {
				ISchedulingInfo sInfo = infoObject.getSchedulingInfo();
				if (subFields.length == 1) {
					outProperties("SchedulingInfo", sInfo.properties());
				} else {
					prop = sInfo.properties().getProperty(subFields[1]);
					if (prop.isContainer()) {
						outProperties("ScheduleInfo." + CePropertyID.idToName(prop.getID()), (IProperties)prop.getValue());
					} else {
						System.out.println(CePropertyID.idToName(prop.getID()) + " = " + prop.getValue());
					}
				}
			} else if (subFields[0].equalsIgnoreCase("si_processinfo")) {
				IProcessingInfo pInfo = infoObject.getProcessingInfo();
				if (subFields.length == 1) {
					outProperties("ProcessingInfo", pInfo.properties());
				} else {
					prop = pInfo.properties().getProperty(subFields[1]);
					if (prop.isContainer()) {
						outProperties("ProcessingInfo." + CePropertyID.idToName(prop.getID()), (IProperties)prop.getValue());
					} else {
						System.out.println(CePropertyID.idToName(prop.getID()) + " = " + prop.getValue());
					}
				}
				
			}
		}
	}

	
	private void outProperties(String text, IProperties properties) {
		System.out.println("---> " + text);
		Set<Integer> keys = properties.keySet();
		for (int key : keys) {
			System.out.println(CePropertyID.idToName(key));
		}
		
	}
}
