package chain.tj.demo;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static chain.tj.util.GmUtils.*;
import static chain.tj.util.PeerUtil.hexToByteArray;
import static chain.tj.util.PeerUtil.toHexString;
import static chain.tj.util.ShaUtil.getSHA256Str;
import static chain.tj.util.TjParseEncryptionKey.*;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 20:29
 * @description：
 * @modified By：
 * @version:
 */
public class HashTool {
    public static void main(String[] args) {
        HashTool hashTool = new HashTool();
        String priPath = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\key\\priv.pem";
        String pubPath = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\key\\pub.pem";
        // 获取公私钥
        Map<String, byte[]> priAndPubKeyBytes = hashTool.getPriAndPubKeyBytes(pubPath, priPath);

        Map<String, String> hashAndSign = hashTool.getHashAndSign("12312", "sm3", priAndPubKeyBytes.get("priKey"));
        System.out.println("hashAndSign = " + hashAndSign);
        // {sign=3045022100b758d7c921c2181b96b2759968038cca437fe26806e5b12ab715706d8f2bc0da022049b642b77f2f8e9cde2aaf7d80256e152e874f14350be8fdeba0d10b260e1680, hash=bb0c13584f3dd789234cd3e6cf8247e0ab8ce8be4574127ecdffffb0eaeb76cc}
        String sign = "3045022100b758d7c921c2181b96b2759968038cca437fe26806e5b12ab715706d8f2bc0da022049b642b77f2f8e9cde2aaf7d80256e152e874f14350be8fdeba0d10b260e1680";
        String data = "bb0c13584f3dd789234cd3e6cf8247e0ab8ce8be4574127ecdffffb0eaeb76cc";
        Boolean pubKey = hashTool.verifyFile(sign, data, priAndPubKeyBytes.get("pubKey"));
        System.out.println("pubKey = " + pubKey);
    }


    /**
     * 获取公私钥
     *
     * @param pubKeyPath
     * @param priKeyPath
     * @return
     */
    public static Map<String, byte[]> getPriAndPubKeyBytes(String pubKeyPath, String priKeyPath) {
        Map<String, byte[]> key = new HashMap<>(2);

        // 读取私钥
        byte[] priKeyBytes = new byte[0];
        try {
            byte[] priKeyBytes1 = readKeyFromPem(priKeyPath);
            priKeyBytes = readPrivateKey(priKeyBytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 读取公钥
        byte[] pubKeyBytes = new byte[0];
        try {
            byte[] pubKeyBytes1 = readKeyFromPem(pubKeyPath);
            pubKeyBytes = readPublicKey(pubKeyBytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        key.put("pubKey", pubKeyBytes);
        key.put("priKey", priKeyBytes);

        return key;
    }

    /**
     * 验证
     *
     * @param sign
     * @param fileHash
     * @return
     */
    public static Boolean verifyFile(String sign, String fileHash, byte[] pubKeyBytes) {
        boolean verify = sm2Verify(pubKeyBytes, hexToByteArray(fileHash), hexToByteArray(sign));
        return verify;
    }


    /**
     * @param data
     * @param hashType
     */
    public static Map<String, String> getHashAndSign(String data, String hashType, byte[] priKeyBytes) {
        Map<String, String> map = new HashMap<>(2);
        // 获取hash
        byte[] hashBytes;
        if (StringUtils.isBlank(hashType) || StringUtils.equals(hashType, "sm3")) {
            // 使用sm3加密
            hashBytes = sm3Hash(data.getBytes());
        } else {
            hashBytes = getSHA256Str(data);
        }
        String hexStrHash = toHexString(hashBytes);

        // 签名  public static byte[] sm2Sign(byte[] privateKey, byte[] sm3bytes)
        byte[] signBytes = new byte[0];
        try {
            signBytes = sm2Sign(priKeyBytes, hashBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("hash", hexStrHash);
        map.put("sign", toHexString(signBytes));
        return map;
    }
}
