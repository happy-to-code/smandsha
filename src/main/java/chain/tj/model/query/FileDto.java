package chain.tj.model.query;

import chain.tj.model.domain.FileExtInfo;
import lombok.Data;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 13:35
 * @description：
 * @modified By：
 * @version:
 */
@Data
public class FileDto {

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件byte数组
     */
    private FileExtInfo fileExtInfo;
}
