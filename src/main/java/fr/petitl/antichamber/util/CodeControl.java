package fr.petitl.antichamber.util;

public class CodeControl {
    public CodeControl() {
	super();
    }

    public void disableSystemExit() {
	SecurityManager securityManager = new StopExitSecurityManager();
	System.setSecurityManager(securityManager);
    }

    public void enableSystemExit() {
	SecurityManager mgr = System.getSecurityManager();
	if ((mgr != null) && (mgr instanceof StopExitSecurityManager)) {
	    StopExitSecurityManager smgr = (StopExitSecurityManager) mgr;
	    System.setSecurityManager(smgr.getPreviousMgr());
	} else
	    System.setSecurityManager(null);
    }
}