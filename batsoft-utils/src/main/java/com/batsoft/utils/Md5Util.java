
package com.batsoft.utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    private static final String TAG = Md5Util.class.getName();
    private static MessageDigest mMessageDigest;
    private static char[] mHexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    static {
        try {
            mMessageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //Log.e("", TAG + " MessageDigest.getInstance() failed :" + e.toString());
        }
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    public static String getFileMD5String(String filePath) throws IOException {
        InputStream fis;
        fis = new FileInputStream(filePath);
        byte[] buf = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buf)) != -1) {
            mMessageDigest.update(buf, 0, numRead);
        }
        fis.close();
        return bufferToHex(mMessageDigest.digest());
    }
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        return resultString;
    }
    
    public static String getFileMD5String(InputStream fis) throws IOException {
       
        byte[] buf = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buf)) != -1) {
            mMessageDigest.update(buf, 0, numRead);
        }
        fis.close();
        return bufferToHex(mMessageDigest.digest());
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringBuffer) {
        char c0 = mHexDigits[(bt & 0xf0) >> 4]; 
        char c1 = mHexDigits[bt & 0xf]; 
        stringBuffer.append(c0);
        stringBuffer.append(c1);
    }

    public static Boolean chkTradingPassword(String inputPassword, String md5Password){
        if (md5Password.equals(inputPassword)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
