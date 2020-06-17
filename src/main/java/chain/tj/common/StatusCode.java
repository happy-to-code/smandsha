package chain.tj.common;

/**
 * @author zhangyifei
 * @date 2020/5/915:23
 **/
public enum StatusCode {
    /**
     * 列表
     */
    RESULT_OK(0, "请求成功"),

    SEND_MESSAGE_OK(100000, "消息发送成功"),


    CLIENT_400001(400001, "您尚未授权此地址权限"),

    CLIENT_400005(400005, "请求方法不允许"),

    CLIENT_400009(400009, "参数类型错误"),


    CLIENT_410001(410001, "字段不能为空"),
    CLIENT_410002(410002, "参数格式错误"),
    CLIENT_410003(410003, "超过最大长度"),
    CLIENT_4100301(41000301, "数据应在正确范围"),
    CLIENT_410004(410004, "非法字符"),
    CLIENT_410005(410005, "验证错误"),
    CLIENT_410009(410009, "JSON转换异常"),
    CLIENT_410010(410010, "上传文件过大"),

    CLIENT_410014(410014, "数据不存在"),
    CLIENT_410015(410015, "数据严重偏离实际"),
    CLIENT_410016(410016, "字段应该为空"),


    SERVER_500000(500000, "未知异常"),
    SERVER_500001(500001, "获取私钥文件失败"),
    SERVER_500002(500002, "签名失败"),
    SERVER_500003(500003, "文件Hash值不可以为空"),
    SERVER_500004(500004, "签名Hash值不可以为空"),
    // 暂留

    SERVER_520000(520000, "业务逻辑异常");


    private final Integer code;

    private final String message;

    StatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public int value() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
