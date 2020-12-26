package com.lyq.framework.common.response;


public enum ResultCodeEnum {
    SUCCESS(20000,"成功"),
    ERROR(300,"未知错误"),
    PARAM_ERROR(301,"参数错误");

	// 响应状态码
    private Integer status;
    // 响应信息
    private String message;

    ResultCodeEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
    
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}