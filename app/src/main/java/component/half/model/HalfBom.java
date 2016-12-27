package component.half.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

import component.material.model.Material;

/** 半成品物料清单 */
public class HalfBom {

	private int id;
	private int halfId; // 半成品id
	private int materialId; // 原料id
	private BigDecimal scale; // 比例

	private Material material;
	private BigDecimal num;

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

	public String getNumStr() {
		return num == null ? "" : NumberFormat.getInstance().format(num).replaceAll(",", "");
	}

}
