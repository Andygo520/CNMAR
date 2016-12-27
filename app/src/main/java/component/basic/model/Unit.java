package component.basic.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;

/** 单位 */
public class Unit extends BaseModel {

	@JSONField(ordinal = 1)
	private String name; // 单位名称
	@JSONField(ordinal = 2)
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
