package com.jewyss.eagels.carbon.emisions.security;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @project Eagle hub
 * @coder estuardo.wyss
 * @date
 */
@Service
public class HashCreator {
    public String createMD5Hash(final String input)
            throws NoSuchAlgorithmException, NoSuchAlgorithmException {

        String hashtext = null;
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Compute message digest of the input
        byte[] messageDigest = md.digest(input.getBytes());

        hashtext = convertToHex(messageDigest);

        return hashtext;
    }

    private String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }
}
