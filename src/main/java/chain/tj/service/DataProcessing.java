package chain.tj.service;

import chain.tj.common.response.RestResponse;
import chain.tj.model.query.FileDto;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 13:23
 * @description：
 * @modified By：
 * @version:
 */
public interface DataProcessing {
    /**
     * 讲文件转成hash值
     *
     * @param fileDto
     * @return
     */
    RestResponse fileToHash(FileDto fileDto);


    /**
     * 对文件进行签名
     *
     * @param fileHash
     * @return
     */
    RestResponse signFile(String fileHash) throws Exception;

    /**
     * 对文件进行验证
     *
     * @param sign
     * @param fileHash
     * @return
     */
    RestResponse verifyFile(String sign, String fileHash) throws Exception;

}
