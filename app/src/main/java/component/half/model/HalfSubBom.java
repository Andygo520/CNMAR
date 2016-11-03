package component.half.model;

import java.math.BigDecimal;

/** 半成品 - 下级半成品BOM */
public class HalfSubBom {

	private int id;
	private int halfId; // 半成品id
	private int subId; // 下级半成品id
	private BigDecimal scale; // 比例

	private Half sub;

	public HalfSubBom() {

	}

	public HalfSubBom(int halfId, int subId, BigDecimal scale) {
		this.halfId = halfId;
		this.subId = subId;
		this.scale = scale;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

	public Half getSub() {
		return sub;
	}

	public void setSub(Half sub) {
		this.sub = sub;
	}

}
