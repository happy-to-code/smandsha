package chain.tj.model.domain.court;

import lombok.Data;

import java.util.Map;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/24 14:14
 * @description：法院行为存证数据对象
 * @modified By：
 * @version:
 */
@Data
public class CourtFileInfo {
    /**
     * 数据报文头信息
     */
    private Header header;

    /**
     * 业务方自定义的业务行为详细数据
     */
    private Map<String, Object> data;

    /**
     * 数据校验，数据完整性、正确性保证
     */
    private Check check;

}
