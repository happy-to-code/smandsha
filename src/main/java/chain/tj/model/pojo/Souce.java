package chain.tj.model.pojo;

import lombok.Data;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 11:03
 * @description：提交者信息。
 * @modified By：
 * @version: 1$
 */
@Data
public class Souce {
    /**
     * 法院编号
     */
    private Integer nFybh;

    /**
     * 司法链平台分配的appid
     */
    private String cFcgptId;

    /**
     * 业务id，唯一（如审判系统、电子卷宗等等）。
     */
    private String cIdentityId;

    /**
     * 业务编号，有案件标识传案件标识，其次传文书主键。如果该字段有值，取证查询时将根据文件Hash和该字段一起取值(非必填字段)
     */
    private String cYwbh;

    /**
     * 电子卷宗专用(非必填字段)
     */
    private String cYwzj;

    /**
     * 验签时间戳
     * （毫秒值），
     * 当前时刻，
     * 误差五分钟以内（现场部署时注意核对两个系统的时间）。
     */
    private String cTimestamp;

    /**
     * 验签数据
     * sign(司法链平台id+司法链平台secret+ CTimestamp)
     * sign方法、司法链平台id、司法链平台secret参考附录
     */
    private String cSignedData;

}
