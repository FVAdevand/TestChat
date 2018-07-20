package ua.fvadevand.testchat.utilities;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public final class CryptUtils {

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private String userPkStr;
    private KeyPair mUserKeyPair;
    private byte[] mDerivedKey;

    public CryptUtils() {
        createKeyPair();
    }

    public String getUserPkStr() {
        return userPkStr;
    }

    private void createKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(256);
            mUserKeyPair = kpg.generateKeyPair();
            userPkStr = toHex(mUserKeyPair.getPublic().getEncoded());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public final void generateSecret(String companionPkStr) {
        try {
            KeyFactory kf = KeyFactory.getInstance("EC");
            byte[] companionPk = fromHex(companionPkStr);
            EncodedKeySpec pkSpec = new X509EncodedKeySpec(companionPk);
            PublicKey companionPk1 = kf.generatePublic(pkSpec);
            KeyAgreement ka = KeyAgreement.getInstance("ECDH");
            ka.init(mUserKeyPair.getPrivate());
            ka.doPhase(companionPk1, true);
            byte[] sharedSecret = ka.generateSecret();
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            hash.update(sharedSecret);
            List<ByteBuffer> keys = Arrays.asList(ByteBuffer.wrap(mUserKeyPair.getPublic().getEncoded()),
                    ByteBuffer.wrap(companionPk));
            Collections.sort(keys);
            hash.update(keys.get(0));
            hash.update(keys.get(1));

            mDerivedKey = hash.digest();

        } catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String toHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private byte[] fromHex(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String encript(String plainText)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(mDerivedKey, "AES");
        c.init(Cipher.ENCRYPT_MODE, k);
        byte[] dataToEncrypt = plainText.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(c.doFinal(dataToEncrypt), Base64.DEFAULT);
    }

    public String decript(String cipherText)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(mDerivedKey, "AES");
        c.init(Cipher.DECRYPT_MODE, k);
        byte[] encryptedData = Base64.decode(cipherText, Base64.DEFAULT);
        return new String(c.doFinal(encryptedData), "UTF-8");
    }
}
