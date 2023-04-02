package com.jewyss.eagels.carbon.emisions.security;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
public class KeyCode {
    private String keyCodeReturn=null;
    private int keyLength=0;

    public  KeyCode(int length)
    {
        keyLength=length;
        keyCodeReturn=randomKey();
    }
    private synchronized String randomKey()
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String pass = "";

        try{
            int ii=0;
            for (int x=0;x<keyLength;x++)
            {
                double i = Math.floor(Math.random() * 62);

                ii = (int) i;

                pass += chars.charAt(ii);

            }
        }catch  (NumberFormatException numE)
        {
            pass=numE.getMessage();
        }
        return pass;
    }
    public String getKeyCode()
    {
        return keyCodeReturn;
    }

    public String generateKeyCode(){
        return randomKey();
    }
}
