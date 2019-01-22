package com.xu.seckill.result;

public class CodeMsg {

    private int code;
    private String msg;


    public static CodeMsg ACCESS_LIMITED = new CodeMsg(500104, "访问高峰期，请稍等！");
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500, "商品已经秒杀完毕");


    private CodeMsg() {
    }

    public CodeMsg(int code, String msg) {
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
