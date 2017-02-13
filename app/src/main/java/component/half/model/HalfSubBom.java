package component.half.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.alibaba.fastjson.annotation.JSONField;

import component.produce.model.ProduceBom;

/** 半成品 - 下级半成品BOM */
public class HalfSubBom {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private int halfId; // 半成品id
	@JSONField(serialize = false)
	private int subId; // 下级半成品id
	@JSONField(serialize = false)
	private BigDecimal scale; // 比例

	@JSONField(ordinal = 7)
	private Half sub;
	@JSONField(serialize = false)
	private BigDecimal num;

	@JSONField(ordinal = 5)
	private ProduceBom produceBom;

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

	@JSONField(serialize = false)
	public String getScaleStr() {
		return NumberFormat.getInstance().format(scale).replaceAll(",", "");
	}

	public Half getSub() {
		return sub;
	}

	public void setSub(Half sub) {
		this.sub = sub;
	}

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	@JSONField(serialize = false)
	public String getNumStr() {
		return num == null ? "" : NumberFormat.getInstance().format(num).replaceAll(",", "");
	}

	public ProduceBom getProduceBom() {
		return produceBom;
	}

	public void setProduceBom(ProduceBom produceBom) {
		this.produceBom = produceBom;
	}

}
