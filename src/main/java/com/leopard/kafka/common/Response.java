package com.leopard.kafka.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装返回消息数据格式
 *
 * @author
 */
@Data
public class Response<T> implements Serializable {

    public static final String SUCCESS = "0";

    public static final String FAIL = "-1";

    /**
     * 状态码
     */
    private String code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 请求标识
     */
    private String requestId;
    /**
     * 结果DATA
     */
    private T data;

    /**
     * 实例化
     */
    public Response() {
    }

    /**
     * 实例化
     *
     * @param code    状态码
     * @param message 响应消息
     */
    private Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 实例化
     *
     * @param code    状态码
     * @param message 响应消息
     * @param data    结果DATA
     */
    private Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 实例化
     *
     * @param code      状态码
     * @param message   响应消息
     * @param data      结果DATA
     * @param requestId 请求唯一标识
     */
    private Response(String code, String message, T data, String requestId) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.requestId = requestId;
    }

    /**
     * 实例化-成功响应
     *
     * @return 成功响应默认结构
     */
    public static Response success() {
        return new Response(SUCCESS, "请求成功");
    }

    /**
     * 实例化-成功响应
     *
     * @param data 结果DATA
     * @return 成功响应默认结构
     */
    public static Response success(Object data) {
        return new Response(SUCCESS, "请求成功", data);
    }


    /**
     * 实例化-失败响应
     *
     * @param code 失败响应状态码
     * @param desc 失败响应内容
     * @return 失败响应默认结构
     */
    public static Response error(String code, String desc) {
        return new Response(code, desc);
    }

    /**
     * 实例化-失败响应
     *
     * @param desc 失败响应内容
     * @return 失败响应默认结构
     */
    public static Response error(String desc) {
        return new Response(FAIL, desc);
    }

    /**
     * 实例化-失败响应
     *
     * @param desc 失败响应内容
     * @return 失败响应默认结构
     */
    public static Response storageError(String desc) {
        return new Response(FAIL, desc);
    }

    /**
     * 验证请求结果
     *
     * @return 响应默认结构
     */
    public Boolean completed() {
        return SUCCESS.equals(this.code);
    }

}
