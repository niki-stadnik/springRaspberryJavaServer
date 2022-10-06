package net.github.nikistadnik.springRaspberryJavaServer;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;


public class Encryption {

    private static String key = "xxxxxxxxxxxxxxxx";

    public static void setKey(String k) {
        key = k;
    }

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static String encrypt(String input) {
        byte[] crypted = null;
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");//PKCS7Padding
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(Hex.encodeHex(crypted));   //.toUpperCase();
    }

    public static String decrypt(String input) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NOPadding");//PKCS7Padding
            cipher.init(Cipher.DECRYPT_MODE, skey);
//            output = cipher.doFinal(Base64.decodeBase64(input));
            output = cipher.doFinal(Hex.decodeHex(input.toCharArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String back = null;
        if (output != null) {
            String outputSTR = new String(output);
            int flag = 0;
            for (int i = 0; i < outputSTR.length(); i++) {
                if (outputSTR.charAt(i) == '{') flag++;
                if (outputSTR.charAt(i) == '}') flag--;
                if (flag == 0) {
                    back = outputSTR.substring(0, i + 1);
                    break;
                }
            }
        }
        return back;
    }

}
