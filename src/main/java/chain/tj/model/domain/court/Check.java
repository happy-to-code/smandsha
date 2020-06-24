package chain.tj.model.domain.court;

import lombok.Data;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/24 14:11
 * @description：数据校验，数据完整性、正确性保证
 * @modified By：
 * @version:
 */
@Data
public class Check {
    /**
     * 数据摘要，摘要计算输入: header+data
     */
    private String digest;

    /**
     * 用于计算数据摘要的哈希算法
     */
    private String hashAlgo;

    /**
     * 业务系统对于数据摘要的签名值
     */
    private String sign;
}
