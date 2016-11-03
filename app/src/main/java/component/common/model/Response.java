package component.common.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.vo.MsgCode;
import component.common.vo.Page;

public class Response<T> {

	@JSONField(ordinal = 1)
	private boolean status = true;
	@JSONField(ordinal = 2)
	private String msg = MsgCode.success;
	@JSONField(ordinal = 3)
	private Page page;
	@JSONField(ordinal = 4)
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

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
