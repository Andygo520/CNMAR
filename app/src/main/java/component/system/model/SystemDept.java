package component.system.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;

/** 部门 */
public class SystemDept extends BaseModel {

	@JSONField(ordinal = 1)
	private String name;
	@JSONField(ordinal = 2)
	private int seq;

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
