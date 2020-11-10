package com.sap.sample.boe.services;

import com.businessobjects.foundation.logging.ILogger;
import com.businessobjects.foundation.logging.LoggerManager;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;

public class SessionHandler {

	private static final ILogger LOG = LoggerManager.getLogger(SessionHandler.class);
	
	private String serializedSession;
	private final ISessionMgr sessionMgr;
	private IEnterpriseSession eSession;

	public SessionHandler(String serializedSession, ISessionMgr sessionMgr) {
		this.serializedSession = serializedSession;
		this.sessionMgr = sessionMgr;
		this.eSession = null;
	}

	public SessionHandler(String serializedSession) {
		this(serializedSession, null);
	}
	
	/**
	 * Get the serialized session
	 * A new session is created, if this is used in a plugin of the CMS Data Access Driver (in the current version the additional session is not closed)
	 * @return
	 */
	public String getSerializedSession() {
		if (sessionMgr == null) return serializedSession;
		if (eSession == null) {
	    	try {
				eSession = sessionMgr.getSession(serializedSession);
		    	eSession = sessionMgr.logonWithToken(eSession.getLogonTokenMgr().getDefaultToken());
		    	this.serializedSession = eSession.getSerializedSession();
		    } catch (SDKException e) {
				LOG.error(e);
			}
		}
		return this.serializedSession;
	}
}
