package com.example.administrator.cnmar.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.administrator.cnmar.vo.MsgCode;


public class Response<T> {

	@JSONField(ordinal = 1)
	private boolean status = true;
	@JSONField(ordinal = 2)
	private String msg = MsgCode.success;

	@JSONField(ordinal = 3)
	private T data;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
