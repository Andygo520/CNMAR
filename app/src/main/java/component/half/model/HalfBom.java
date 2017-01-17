package component.half.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.text.NumberFormat;

import component.material.model.Material;
import component.produce.model.ProduceBom;

/** 半成品物料清单 */
public class HalfBom {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private int halfId; // 半成品id
	@JSONField(serialize = false)
	private int materialId; // 原料id
	@JSONField(serialize = false)
	private BigDecimal scale; // 比例

	@JSONField(ordinal = 7)
	private Material material;
	@JSONField(serialize = false)
	private BigDecimal num;

	@JSONField(ordinal = 5)
	private ProduceBom produceBom;

	public HalfBom() {

	}

	public HalfBom(int halfId, int materialId, BigDecimal scale) {
		this.halfId = halfId;
		this.materialId = materialId;
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

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
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

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
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
