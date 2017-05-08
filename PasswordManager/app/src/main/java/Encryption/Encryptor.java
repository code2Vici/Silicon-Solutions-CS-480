package Encryption;

//import java.util.Base64;s

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

public class Encryptor {
    private static int workload = 5;

    public Encryptor(){

    }

    public static String hash(String password_plaintext) {
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);

        return(hashed_password);
    }

    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return(password_verified);
    }


    public static String decrypt(String name, String key)
            throws Exception {
        BlowfishEngine engine = new BlowfishEngine();
        PaddedBufferedBlockCipher cipher =
                new PaddedBufferedBlockCipher(engine);
        StringBuffer result = new StringBuffer();
        KeyParameter newKey = new KeyParameter(key.getBytes());
        cipher.init(false, newKey);
        byte out[] = Base64.decode(name);
        byte out2[] = new byte[cipher.getOutputSize(out.length)];
        int len2 = cipher.processBytes(out, 0, out.length, out2, 0);
        cipher.doFinal(out2, len2);
        String s2 = new String(out2);
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (c != 0) {
                result.append(c);
            }
        }

        return result.toString();
    }

    public static String encrypt(String value, String key)
            throws Exception {
        BlowfishEngine engine = new BlowfishEngine();
        PaddedBufferedBlockCipher cipher =
                new PaddedBufferedBlockCipher(engine);
        KeyParameter newKey = new KeyParameter(key.getBytes());
        cipher.init(true, newKey);
        byte in[] = value.getBytes();
        byte out[] = new byte[cipher.getOutputSize(in.length)];
        int len1 = cipher.processBytes(in, 0, in.length, out, 0);
        try {
            cipher.doFinal(out, len1);
        } catch (CryptoException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        String s = new String(Base64.encode(out));
        return s;
    }
}