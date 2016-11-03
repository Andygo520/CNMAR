package component.half.model;

import component.common.model.BaseModel;

/** 半成品单位 */
public class HalfUnit extends BaseModel {

	private String name; // 半成品单位名称
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
