package fr.petitl.antichamber.util;

import java.security.Permission;

public class StopExitSecurityManager extends SecurityManager {
    private SecurityManager _prevMgr = System.getSecurityManager();

    public void checkPermission(Permission perm) {
    }

    public void checkExit(int status) {
	super.checkExit(status);
	throw new SecurityException(); // This throws an exception if an exit
				       // is called.
    }

    public SecurityManager getPreviousMgr() {
	return _prevMgr;
    }
}