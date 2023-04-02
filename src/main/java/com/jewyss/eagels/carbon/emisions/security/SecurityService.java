package com.jewyss.eagels.carbon.emisions.security;

import java.util.Base64;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
public class SecurityService {
    private static SecurityService instance;
    private SecurityService()
    {
    }
    public synchronized String encrypt(String plaintext)
    {

        // Creating byte array
        String encodedString = Base64.getEncoder().encodeToString(plaintext.getBytes());

        //byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        //String decodedString = new String(decodedBytes);

        return encodedString;
    }

    public static synchronized SecurityService getInstance() //step 1
    {
        if(instance == null)
        {
            instance=new SecurityService();
            return instance;
        }
        else
        {
            return instance;
        }
    }
}
