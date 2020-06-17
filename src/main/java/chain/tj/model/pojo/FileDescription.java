package chain.tj.model.pojo;

import lombok.Data;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 11:11
 * @description：存证文件信息。
 * @modified By：
 * @version:
 */
@Data
public class FileDescription {

    /**
     * 目标文件修改前的SHA256码（业务系统自行调用SHA256生成组件生成
     * 注：文件状态为1（业务系统传递）或为2（新增）时，COldSign和CNewSign都给当前文件的哈希值即可。
     */
    private String cOldSign;

    /**
     * 目标文件修改后的SHA256码
     */
    private String cNewSign;

    /**
     * 文书种类，参考附录[文书类型]，给文书类型对应的英文。
     * 注：无法区分类型时，请给“其他”对应的英文。
     */
    private String cWslx;

    /**
     * 文书来源，文书进入本业务系统前来自什么业务系统，在“文件状态”为1（业务系统传递）时候必须有值。字符串类型
     */
    private String cWsly;

    /**
     * 文件状态（1：业务系统传递；2：新增；3：修改）
     */
    private Integer NFileStatus;

    /**
     * 文件名称
     */
    private String cFileName;

    /**
     * 文件大小（单位：KB）  （非必填字段）
     */
    private String cFileSize;

    /**
     * 提交人名称   （非必填字段）
     */
    private String cFileCreator;

    /**
     * 文件最新修改时间（格式：yyyy-MM-dd HH:mm:ss.SSS）  （非必填字段）
     */
    private String cFileModiTime;

}
