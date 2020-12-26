package com.lyq.framework.common.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer status;

    private String message;

    private T data;

    // 构造器私有
    private R(){}

    public Integer getStatus() {
		return status;
	}

	private void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	private void setData(T data) {
		this.data = data;
	}

	// 通用返回成功
    public static R ok() {
        R r = new R();
        r.setStatus(ResultCodeEnum.SUCCESS.getStatus());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    // 通用返回失败，未知错误
    public static R error() {
        R r = new R();
        r.setStatus(ResultCodeEnum.ERROR.getStatus());
        r.setMessage(ResultCodeEnum.ERROR.getMessage());
        return r;
    }

    // 设置结果，形参为结果枚举
    public static R setResult(ResultCodeEnum result) {
        R r = new R();
        r.setStatus(result.getStatus());
        r.setMessage(result.getMessage());
        return r;
    }

    /**------------使用链式编程，返回类本身-----------**/

    // 自定义返回数据
    public R data(T data) {
        this.setData(data);
        return this;
    }

    // 自定义状态信息
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    // 自定义状态码
    public R status(Integer status) {
        this.setStatus(status);
        return this;
    }
}