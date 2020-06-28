package chain.tj.demo;

import chain.tj.common.exception.ServiceException;
import chain.tj.model.domain.court.Check;
import chain.tj.model.domain.court.OperationLog;
import chain.tj.model.domain.court.Header;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    public static void main(String[] args) throws InterruptedException {
        // **在chain.tj.demo.MyGmUtil中有一个main方法，运行后会在当前文件夹中生成一对公私钥
        // **如果不想生成，可以直接用chain.tj.file.key中的密钥对

        // 获取私钥字节数组
        String priPath = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\key\\priv.pem";
        byte[] priKeyBytes = getPriKeyByPath(priPath);
        System.out.println("priKeyBytes 16进制形式 = " + toHexString(priKeyBytes));
        System.out.println("---------------------------------分割线0---------------------------------");

        // 获取公钥字节数组
        String pubPath = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\key\\pub.pem";
        byte[] pubKeyBytes = getPubKeyByPath(pubPath);
        System.out.println("pubKeyBytes 16进制形式 = " + toHexString(pubKeyBytes));
        System.out.println("---------------------------------分割线1---------------------------------");


        // 创建法院行为存证数据对象
        OperationLog operationLog = new OperationLog();

        Header header = new Header();
        header.setCourtName("相城法院");
        header.setCourtId(123L);
        header.setBizSystemId("通达海-电子送达系统");
        header.setCaseId("XC1001-001");
        header.setCategory("电子送达");
        header.setSubCategory("送达签收");
        header.setTimestamp(System.currentTimeMillis());

        Map<String, Object> data = new HashMap<>(8);
        data.put("sender", "XX法院");
        data.put("people", "张三");
        data.put("idcard", "3456784567845678");
        data.put("method", "短信");
        data.put("address", "13821324323");
        data.put("sendTime", "2020-06-10 15:32:23");
        data.put("signoffTime", "2020-06-11 10:32:19");

        // 将header和data转为Jason串
        String headerStr = JSON.toJSONString(header);
        String dataStr = JSON.toJSONString(data);
        // 拼接字符串
        String headerAndDataStr = headerStr + dataStr;
        // 字符串经过加密 生成数据摘要
        byte[] digestStr = getDigestStr(headerAndDataStr, "sm3");

        // 获取签名
        byte[] digestStrSign = getSign(digestStr, priKeyBytes);

        // 验证签名,验证通过：true    验证不通过：false
        // fileHash = new byte[]{1, 3};
        Boolean verifyResult = verify(digestStrSign, digestStr, pubKeyBytes);
        System.out.println("验证结果：" + verifyResult);

        Check check = new Check();
        // 转换成16进制然后赋值
        check.setDigest(toHexString(digestStr));
        check.setHashAlgo("sm3");
        check.setSign(toHexString(digestStrSign));

        operationLog.setHeader(header);
        operationLog.setData(data);
        operationLog.setCheck(check);

        // 保存存证内容
        Map responseMap = saveStore(operationLog);
        // 此处会返回交易hash,交易hash的值存在：responseMap.get("Data").get("Figure") 中
        System.out.println("responseMap = " + responseMap);
        System.out.println("---------------------------------分割线2---------------------------------");

        // 根据交易hash获取当前区块的高度
        Map map = (Map) responseMap.get("Data");
        String figure = (String) map.get("Figure");
        System.out.println("figure = " + figure);
        // 同步节点之间数据
        Thread.sleep(5000);
        Map heightByTxHash = getHeightByTxHash(figure);
        // 区块的高度存放在 heightByTxHash.get("Data") 中
        System.out.println("heightByTxHash = " + heightByTxHash);
        System.out.println("---------------------------------分割线3---------------------------------");

        // 根据链的高度获取区块信息
        Integer height = (Integer) heightByTxHash.get("Data");
        Map blockByHeight = getBlockByHeight(height);
        // 此处会返回 区块hash的值，位置：blockByHeight.get("Data").get("header").get("blockHash")
        System.out.println("blockByHeight = " + blockByHeight);
        System.out.println("---------------------------------分割线4---------------------------------");

        // 根据hash值获取区块信息
        Map data1 = (Map) blockByHeight.get("Data");
        Map header1 = (Map) data1.get("header");
        String hash = (String) header1.get("blockHash");
        String blockByHash = getBlockByHash(hash);
        System.out.println("blockByHash = " + blockByHash);
    }


    /**
     * 校验数据
     *
     * @param operationLog
     */
    private static void checkData(OperationLog operationLog) {
        Header header = operationLog.getHeader();
        Map<String, Object> data = operationLog.getData();
        Check check = operationLog.getCheck();
        if (header == null || data == null || check == null) {
            throw new ServiceException("数据报文头信息header、自定义的业务行为详细数据data、数据校验信息check不可以为空");
        }
        if (StringUtils.isBlank(header.getCourtName())) {
            throw new ServiceException("法院名称courtName不可以为空");
        }
        if (header.getCourtId() == null || header.getCourtId() < 0) {
            throw new ServiceException("法院编号courtId不可以为空并且必须大于0");
        }
        if (StringUtils.isBlank(header.getBizSystemId())) {
            throw new ServiceException("业务系统身份bizSystemId不可以为空");
        }
        if (StringUtils.isBlank(header.getCaseId())) {
            throw new ServiceException("案件编号caseId不可以为空");
        }
        if (StringUtils.isBlank(header.getCategory())) {
            throw new ServiceException("业务行为主分类category不可以为空");
        }
        if (header.getTimestamp() == null || header.getTimestamp() <= 0) {
            throw new ServiceException("时间戳timestamp非法或者为空");
        }

        if (data.isEmpty()) {
            throw new ServiceException("没有任何自定义的业务行为数据");
        }

        if (StringUtils.isBlank(check.getDigest())) {
            throw new ServiceException("数据摘要digest不可以为空");
        }
        if (StringUtils.isBlank(check.getHashAlgo())) {
            throw new ServiceException("哈希算法hashAlgo不可以为空");
        }
        if (StringUtils.isBlank(check.getSign())) {
            throw new ServiceException("签名值sign不可以为空");
        }

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
     * 获取hash值
     *
     * @param content
     * @param hashType 求hash值的算法
     * @return
     */
    public static byte[] getDigestStr(String content, String hashType) {
        if (StringUtils.isBlank(content)) {
            throw new ServiceException("内容不可以为空！");
        }
        // 获取文件内容hash
        byte[] hashBytes;
        if (StringUtils.isBlank(hashType) || StringUtils.equals(hashType, "sm3")) {
            // 使用sm3加密
            hashBytes = sm3Hash(content.getBytes());
        } else {
            hashBytes = getSHA256Str(content);
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
    public static byte[] getSign(byte[] fileHashBytes, byte[] priKeyBytes) {
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
    public static Boolean verify(byte[] sign, byte[] fileHash, byte[] pubKeyBytes) {
        boolean verifyResult = sm2Verify(pubKeyBytes, fileHash, sign);
        return verifyResult;
    }

    /**
     * 保存存证信息
     *
     * @param operationLog 要保存的内容
     * @return
     */
    public static Map saveStore(OperationLog operationLog) {
        // 校验数据
        checkData(operationLog);
        // 将对象转为json串
        String data = JSON.toJSONString(operationLog);
        String s = doPost(data);
        // 将json串转为Map
        Map map = (Map) JSONObject.parse(s);
        // Map map = JSON.parseObject(s, Map.class);
        return map;
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
     * 根据交易hash获取区块的高度
     *
     * @param txHash
     * @return
     */
    public static Map getHeightByTxHash(String txHash) {
        if (StringUtils.isBlank(txHash)) {
            throw new ServiceException("交易哈希值不可以为空");
        }
        String urlEncodeStr = urlEncode(txHash);
        String url = "gettransactionblock?hashData=" + urlEncodeStr;
        String result = doGetInfo(url);

        return JSON.parseObject(result, Map.class);
    }

    /**
     * 根据链的高度获取区块信息
     *
     * @return
     */
    public static Map getBlockByHeight(Integer height) {
        if (height <= 0) {
            throw new ServiceException("高度应该大于0");
        }
        String url = "getblockbyheight?number=" + height;
        String result = doGetInfo(url);
        return JSON.parseObject(result, Map.class);
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
        CloseableHttpClient httpClientPost = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://10.1.5.226:58080/store");

        Map<String, String> map = new HashMap<>(2);
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
            response = httpClientPost.execute(httpPost);
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
                if (httpClientPost != null) {
                    httpClientPost.close();
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
