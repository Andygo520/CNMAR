package com.example.administrator.cnmar.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 原料单位 */
public class MaterialUnit extends BaseModel {

	private String name; // 原料单位名称
	@JSONField(serialize = false)
	private int seq; // 顺序号

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
