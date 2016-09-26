package component.product.model;

import component.common.model.BaseModel;

/** 成品单位 */
public class ProductUnit extends BaseModel {

	private String name; // 成品单位名称
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
