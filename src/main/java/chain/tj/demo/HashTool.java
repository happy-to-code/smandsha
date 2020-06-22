package chain.tj.demo;

import chain.tj.common.exception.ServiceException;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static chain.tj.util.FileUtil.readFile;
import static chain.tj.util.GmUtils.*;
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
        // 获取私钥字节数组
        String priPath = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\key\\priv.pem";
        byte[] priKeyBytes = getPriKeyByPath(priPath);
        System.out.println("priKeyBytes 16进制形式 = " + toHexString(priKeyBytes));
        System.out.println("---------------------------------分割线---------------------------------");

        // 获取公钥字节数组
        String pubPath = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\key\\pub.pem";
        byte[] pubKeyBytes = getPubKeyByPath(pubPath);
        System.out.println("pubKeyBytes 16进制形式 = " + toHexString(pubKeyBytes));
        System.out.println("---------------------------------分割线---------------------------------");

        // 获取文件hash
        String filePath = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\test.txt";
        byte[] fileHash = getFileHash(filePath, "");
        System.out.println("fileHash  16进制形式= " + toHexString(fileHash));
        System.out.println("---------------------------------分割线---------------------------------");

        // 获取文件的签名
        byte[] fileSign = getFileSign(fileHash, priKeyBytes);
        System.out.println("fileSign 16进制形式= " + toHexString(fileSign));
        System.out.println("---------------------------------分割线---------------------------------");

        // 验证签名
        Boolean verifyResult = verifyFile(fileSign, fileHash, pubKeyBytes);
        System.out.println("验证结果：" + verifyResult);
        System.out.println("---------------------------------分割线---------------------------------");

        // 保存存证内容
        String response = saveStore(toHexString(fileHash));
        System.out.println("response = " + response);
        System.out.println("---------------------------------分割线---------------------------------");

        // 获取链的高度
        String chainHeight = getChainHeight();
        System.out.println("chainHeight = " + chainHeight);
        System.out.println("---------------------------------分割线---------------------------------");

        // 根据hash值获取区块信息
        String hash = "TZeojuWdhvy1Bh2WEOvPN+ocYkOxnFc2OKr0hpDAv74=";
        String blockByHash = getBlockByHash(hash);
        System.out.println("blockByHash = " + blockByHash);
        System.out.println("---------------------------------分割线---------------------------------");

        // 根据链的高度获取区块信息
        String blockByHeight = getBlockByHeight(12);
        System.out.println("blockByHeight = " + blockByHeight);

    }


    /**
     * 通过路径获取私钥字节数组
     *
     * @param priKeyPath
     * @return
     */
    public static byte[] getPriKeyByPath(String priKeyPath) {
        // 读取公钥
        byte[] priKeyBytes = new byte[0];
        try {
            byte[] pubKeyBytes1 = readKeyFromPem(priKeyPath);
            priKeyBytes = readPrivateKey(pubKeyBytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return priKeyBytes;
    }

    /**
     * 通过路径获取公钥字节数组
     *
     * @param pubKeyPath
     * @return
     */
    public static byte[] getPubKeyByPath(String pubKeyPath) {
        // 读取公钥
        byte[] pubKeyBytes = new byte[0];
        try {
            byte[] pubKeyBytes1 = readKeyFromPem(pubKeyPath);
            pubKeyBytes = readPublicKey(pubKeyBytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubKeyBytes;
    }

    /**
     * 获取文件的hash
     *
     * @param filePath 文件地址路径
     * @param hashType 取hash的加密算法，支持（sm3和sha256,默认是sm3）
     * @return
     */
    public static byte[] getFileHash(String filePath, String hashType) {
        if (StringUtils.isBlank(filePath)) {
            throw new ServiceException("文件路径不可以为空");
        }
        // 根据路径读取文件内容
        String fileContent = readFile(filePath);
        if (StringUtils.isBlank(fileContent)) {
            return null;
        }

        // 获取文件内容hash
        byte[] hashBytes;
        if (StringUtils.isBlank(hashType) || StringUtils.equals(hashType, "sm3")) {
            // 使用sm3加密
            hashBytes = sm3Hash(fileContent.getBytes());
        } else {
            hashBytes = getSHA256Str(fileContent);
        }
        return hashBytes;
    }

    /**
     * 获取文件的签名
     *
     * @param fileHashBytes
     * @param priKeyBytes
     * @return
     */
    public static byte[] getFileSign(byte[] fileHashBytes, byte[] priKeyBytes) {
        byte[] signBytes = new byte[0];
        try {
            signBytes = sm2Sign(priKeyBytes, fileHashBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signBytes;
    }

    /**
     * 验证
     *
     * @param sign
     * @param fileHash
     * @return
     */
    public static Boolean verifyFile(byte[] sign, byte[] fileHash, byte[] pubKeyBytes) {
        boolean verify = sm2Verify(pubKeyBytes, fileHash, sign);
        return verify;
    }

    /**
     * 保存存证信息
     *
     * @param data 要保存的内容
     * @return
     */
    public static String saveStore(String data) {
        String s = doPost(data);
        return s;
    }


    /**
     * 获取链的高度
     *
     * @return
     */
    public static String getChainHeight() {
        String s = doGetInfo("getheight");
        // System.out.println("s = " + s);
        // Map mapType = JSON.parseObject(s, Map.class);
        // System.out.println("mapType = " + mapType);
        return s;
    }

    /**
     * 根据hash值获取区块信息
     *
     * @return
     */
    public static String getBlockByHash(String hash) {
        if (StringUtils.isBlank(hash)) {
            throw new ServiceException("区块哈希值不可以为空");
        }
        String urlEncodeStr = urlEncode(hash);
        String url = "getblockbyhash?hash=" + urlEncodeStr;
        String result = doGetInfo(url);
        return result;
    }

    /**
     * 根据链的高度获取区块信息
     *
     * @return
     */
    public static String getBlockByHeight(Integer height) {
        if (height <= 0) {
            throw new ServiceException("高度应该大于0");
        }
        String url = "getblockbyheight?number=" + height;
        String result = doGetInfo(url);
        return result;
    }

    /**
     * url编码
     *
     * @param param
     * @return
     */
    public static String urlEncode(String param) {
        String encode = null;
        try {
            encode = URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }

    /**
     * post请求保存数据或者获取信息
     *
     * @param data
     * @return
     */
    public static String doPost(String data) {
        String res = "";

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://10.1.5.226:58080/store");

        Map<String, String> map = new HashMap<String, String>(2);
        map.put("Data", data);

        String jsonString = JSON.toJSONString(map);

        StringEntity entity = new StringEntity(jsonString, "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            //
            // System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                // System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                // System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
                res = EntityUtils.toString(responseEntity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * get请求获取信息
     *
     * @param paramUrl
     * @return
     */
    public static String doGetInfo(String paramUrl) {
        // 返回值
        String responseStr = null;
        String url = "http://10.1.5.226:58080/" + paramUrl;

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            // System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                responseStr = EntityUtils.toString(responseEntity);
                System.out.println("responseStr = " + responseStr);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseStr;
    }
}
