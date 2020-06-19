package chain.tj.model.domain;

import lombok.Data;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 14:18
 * @description：
 * @modified By：
 * @version:
 */
@Data
public class FileExtInfo {

    /**
     * 文件内容字节数组
     */
    private byte[] fileByte;

    /**
     * 扩展名
     */
    private String extName;

    /**
     * 修改时间
     */
    private String modTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 文件大小（单位字节）
     */
    private Long fileSizes;


}
