package chain.tj.util;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 18:41
 * @description：
 * @modified By：
 * @version:
 */
public class ShaUtil {
    public static void main(String[] args) throws Exception {
        // byte[] ssss1 = getSHA256Str("ssss1");
        // System.out.println("ssss1 = " + toHexString(ssss1));


        byte[] bytes = sha1Encode("123".getBytes());
        System.out.println("bytes = " + bytes);

    }

    /**
     * 利用Apache的工具类实现SHA-256加密
     *
     * @param str 加密后的报文
     * @return
     */
    public static byte[] getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr.getBytes();
    }


    // ===========================计算hash测试================================

    /**
     * sha256计算hash
     *
     * @param str
     * @return
     */
    public static byte[] getSHA256Str(byte[] str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str);
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encdeStr.getBytes();
    }

    /**
     * @return
     * @Comment SHA1实现
     * @Author zhangyifei
     * @Date 2017年9月13日 下午3:30:36
     */
    public static byte[] sha1Encode(byte[] inStr) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return null;
        }

        // byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(inStr);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().getBytes();
    }

    /**
     * MD5
     *
     * @param bytes
     * @return
     */
    public static byte[] md5Encode(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes).getBytes();
    }

    /**
     * sm3计算hash
     *
     * @param srcData
     * @return
     */
    public static byte[] sm3CountHash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    // ===========================计算hash测试================================


    private static final char[] HEX = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
