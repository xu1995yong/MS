package com.xu.seckill.result;

public class CodeMsg {

    private int code;
    private String msg;

    // 通用的错误码
    public static final CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static final CodeMsg PATH_ERROR = new CodeMsg(404, "路径错误");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg TIME_ERROR = new CodeMsg(500101, "秒杀未开始或已结束：%s");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500104, "访问高峰期，请稍等！");

    // 登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg USER_ERROR = new CodeMsg(500211, "用户错误");
    public static CodeMsg PRIMARY_ERROR = new CodeMsg(500216, "主键冲突");

    // 商品模块 5003XX
    public static final CodeMsg STOCK_ERROR = new CodeMsg(500301, "数据库中商品库存不足！");


    // 订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");

    // 秒杀模块 5005XX
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
    public static CodeMsg REPEATE_SECKILL = new CodeMsg(500501, "不能重复秒杀");

    private CodeMsg() {
    }

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }

}
