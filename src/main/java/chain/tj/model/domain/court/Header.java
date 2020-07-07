package chain.tj.model.domain.court;

import lombok.Data;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/24 14:03
 * @description：数据报文头信息
 * @modified By：
 * @version:
 */
@Data
public class Header {

    /**
     * 法院名称
     */
    private String courtName;

    /**
     * 法院编号(非必须)
     */
    private Long courtId;

    /**
     * 业务系统身份ID
     */
    private String bizSystemId;

    /**
     * 案件编号
     */
    private String caseId;

    /**
     * 业务行为主分类
     */
    private String category;

    /**
     * 业务行为子分类(非必须)
     */
    private String subCategory;

    /**
     * 时间戳，毫秒值
     */
    private Long timestamp;


    // @Override
    // public String toString() {
    //     return "{" +
    //             "caseId:'" + caseId + '\'' +
    //             ",courtId:" + courtId +'\'' +
    //             ",category:'" + category + '\'' +
    //             ",courtName:'" + courtName + '\'' +
    //             ",timestamp:" + timestamp +'\'' +
    //             ",bizSystemId:'" + bizSystemId + '\'' +
    //             ",subCategory:'" + subCategory + '\'' +
    //             '}';
    // }
}
