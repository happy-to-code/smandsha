package chain.tj.service.impl;

import chain.tj.common.StatusCode;
import chain.tj.common.response.RestResponse;
import chain.tj.model.domain.FileExtInfo;
import chain.tj.model.query.FileDto;
import chain.tj.service.DataProcessing;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static chain.tj.util.FileUtil.getFileExtInfo;
import static chain.tj.util.GmUtils.*;
import static chain.tj.util.PeerUtil.hexToByteArray;
import static chain.tj.util.PeerUtil.toHexString;
import static chain.tj.util.ShaUtil.getSHA256Str;
import static chain.tj.util.TjParseEncryptionKey.*;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 13:40
 * @description：
 * @modified By：
 * @version:
 */
@Service
@Slf4j
public class SmDataProcessing implements DataProcessing {

    /**
     * 获取文件hash值
     *
     * @param fileDto
     * @param hashType
     * @return
     */
    @Override
    public RestResponse fileToHash(FileDto fileDto, String hashType) {
        // 组装或者获取FileExtInfo对象
        FileExtInfo fileExtInfo = packageFileExtInfo(fileDto);
        if (null != fileExtInfo) {
            byte[] hashBytes = hashEncryption(fileExtInfo, hashType);
            if (null != hashBytes) {
                // 然后转成16进制字符串
                String str = toHexString(hashBytes);
                log.info("str16--------->" + str);
                return RestResponse.success().setData(str);
            }
        }
        return RestResponse.failure("获取文件哈希值失败！", StatusCode.SERVER_500000.value());
    }


    /**
     * 对文件进行签名
     *
     * @param fileHash
     * @return
     */
    @Override
    public RestResponse signFile(String fileHash) throws Exception {
        if (StringUtils.isBlank(fileHash)) {
            return RestResponse.failure("文件Hash值不可以为空！", StatusCode.SERVER_500003.value());
        }

        // 读取私钥
        byte[] priKeyBytes;
        try {
            byte[] priKeyBytes1 = readKeyFromPem("E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\key\\priv.pem");
            priKeyBytes = readPrivateKey(priKeyBytes1);
        } catch (IOException e) {
            e.printStackTrace();
            return RestResponse.failure("获取私钥文件失败", StatusCode.SERVER_500001.value());
        }

        // 签名  public static byte[] sm2Sign(byte[] privateKey, byte[] sm3bytes)
        byte[] signBytes;
        try {
            signBytes = sm2Sign(priKeyBytes, hexToByteArray(fileHash));
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure("签名失败", StatusCode.SERVER_500002.value());
        }
        return RestResponse.success().setData(toHexString(signBytes));
    }

    /**
     * 对文件进行验证
     *
     * @param sign
     * @param fileHash
     * @return
     */
    @Override
    public RestResponse verifyFile(String sign, String fileHash) throws Exception {
        if (StringUtils.isBlank(sign)) {
            return RestResponse.failure("签名值不可以为空！", StatusCode.SERVER_500004.value());
        }
        if (StringUtils.isBlank(fileHash)) {
            return RestResponse.failure("文件Hash值不可以为空！", StatusCode.SERVER_500003.value());
        }

        // public static boolean sm2Verify(byte[] publicKey, byte[] sourceData, byte[] sign)
        // 读取公钥
        byte[] pubKeyBytes;
        try {
            byte[] pubKeyBytes1 = readKeyFromPem("E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\key\\pub.pem");
            pubKeyBytes = readPublicKey(pubKeyBytes1);
        } catch (IOException e) {
            e.printStackTrace();
            return RestResponse.failure("获取公钥文件失败", StatusCode.SERVER_500001.value());
        }

        boolean verify = sm2Verify(pubKeyBytes, hexToByteArray(fileHash), hexToByteArray(sign));

        return RestResponse.success().setData(verify);
    }

    /**
     * 组装FileExtInfo
     *
     * @param fileDto
     * @return
     */
    private FileExtInfo packageFileExtInfo(FileDto fileDto) {
        FileExtInfo fileExtInfo;
        if (null == fileDto.getFileExtInfo()) {
            fileExtInfo = getFileExtInfo(fileDto.getFilePath());
        } else {
            fileExtInfo = fileDto.getFileExtInfo();
        }
        return fileExtInfo;
    }

    /**
     * 通过sm3对对象进行加密
     *
     * @param fileExtInfo
     * @return
     */
    private byte[] hashEncryption(FileExtInfo fileExtInfo, String hashType) {
        //  文件序列化
        if (null != fileExtInfo && null != fileExtInfo.getFileByte()) {
            // byte[] fileBytes = serialFileDto(fileExtInfo);
            byte[] fileBytes = JSON.toJSONString(fileExtInfo.getFileByte()).getBytes();
            if (StringUtils.isBlank(hashType) || StringUtils.equals(hashType, "sm3")) {
                // 使用sm3加密
                return sm3Hash(fileBytes);
            } else {
                return getSHA256Str(JSON.toJSONString(fileExtInfo.getFileByte()));
            }
        }
        return null;
    }

}
