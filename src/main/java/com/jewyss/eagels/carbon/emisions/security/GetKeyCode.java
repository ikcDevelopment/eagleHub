package com.jewyss.eagels.carbon.emisions.security;

/**
 * @project Eagle hub
 * @coder estuardo.wyss
 * @date
 */
public class GetKeyCode {
    private KeyCode objKeyCode=null;
    private String kcKeySession="";
    private SecurityService secService=null;

    /**
     * return of the key is lenght 15
     * @return
     */
    public synchronized  String get_keyCode()
    { //generates a unique key for this servlet session.
        objKeyCode = new KeyCode(15);
        secService = SecurityService.getInstance();
        kcKeySession = objKeyCode.getKeyCode();
        return secService.encrypt(kcKeySession);
    }

    /**
     * return of the key is variable lenght depends on lenght parameter
     * @param lenght
     * @return
     */
    public synchronized  String get_keyCode(int lenght) {
        objKeyCode = new KeyCode(lenght);
        secService = SecurityService.getInstance();
        kcKeySession = objKeyCode.getKeyCode();
        return secService.encrypt(kcKeySession);
    }
}
