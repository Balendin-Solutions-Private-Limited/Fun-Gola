package com.thambola.fungola.Utils;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RijndaelCrypt {

    public static final String TAG = "rrrrrrrrrrr";

    //private static String TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private static String TRANSFORMATION = "AES/CBC/NoPadding";
    private static String ALGORITHM = "AES";
    private static String DIGEST = "MD5";

    private static Cipher _cipher;
    private static SecretKey _password;
    private static IvParameterSpec _IVParamSpec;

    //16-byte private key
    private static byte[] IV = "NdRgUkXp2s5v8x/A".getBytes();


    /**
     Constructor
     @password Public key

     */
    public RijndaelCrypt(String password) {

        try {

            //Encode digest
            MessageDigest digest;
            digest = MessageDigest.getInstance(DIGEST);
            _password = new SecretKeySpec(digest.digest(password.getBytes()), ALGORITHM);

            //Initialize objects
            _cipher = Cipher.getInstance(TRANSFORMATION);
        //    _cipher = Cipher.getInstance(ALGORITHM);

            _IVParamSpec = new IvParameterSpec(IV);

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm " + ALGORITHM, e);
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, "No such padding PKCS7", e);
        }
    }




    /**
     Encryptor.

     @text String to be encrypted
     @return Base64 encrypted text

     */
    public String encrypt(byte[] text) {

        byte[] encryptedData;

        try {

            _cipher.init(Cipher.ENCRYPT_MODE, _password, _IVParamSpec);
            encryptedData = _cipher.doFinal(text);

        } catch (InvalidKeyException e) {
            Log.e(TAG, "Invalid key  (invalid encoding, wrong length, uninitialized, etc).", e);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, "Invalid or inappropriate algorithm parameters for " + ALGORITHM, e);
            return null;
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, "The length of data provided to a block cipher is incorrect", e);
            return null;
        } catch (BadPaddingException e) {
            Log.e(TAG, "The input data but the data is not padded properly.", e);
            return null;
        }

        return Base64.encodeToString(encryptedData, Base64.DEFAULT);

    }

    /**
     Decryptor.

     @text Base64 string to be decrypted
     @return decrypted text

     */

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public String decrypt(String text) {

        try {
            _cipher.init(Cipher.DECRYPT_MODE, _password, _IVParamSpec);

           // byte[] decodedValue = Base64.decode(text.getBytes(), Base64.NO_WRAP);
            byte[] decodedValue = Base64.decode(text.getBytes(), Base64.DEFAULT);
            byte[] decryptedVal = _cipher.doFinal(decodedValue);
          //  byte[] decryptedVal = _cipher.doFinal(fromHexString(text));

//            String decryptString = null;
//            try {
//                decryptString = new String(_cipher.doFinal(decodedValue), "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                Log.e(TAG, "rrrrrrrrrrrr ", e);
//            }
//            return decryptString;
            return new String(decryptedVal);


        } catch (InvalidKeyException e) {
            Log.e(TAG, "Invalid key  (invalid encoding, wrong length, uninitialized, etc).", e);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, "Invalid or inappropriate algorithm parameters for " + ALGORITHM, e);
            return null;
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, "The length of data provided to a block cipher is incorrect", e);
            return null;
        } catch (BadPaddingException e) {
            Log.e(TAG, "The input data but the data is not padded properly.", e);
            return null;
        }

    }

}