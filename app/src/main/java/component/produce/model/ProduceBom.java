package component.produce.model;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

import component.material.model.Material;

/** 加工单物料清单 */
public class ProduceBom {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int produceId; // 加工单id
	@JSONField(ordinal = 3)
	private int materialId; // 原料id
	@JSONField(ordinal = 4)
	private BigDecimal scale; // bom比例
	@JSONField(ordinal = 5)
	private int receiveNum; // 领料数量

	@JSONField(ordinal = 6)
	private Material material;

	public ProduceBom() {

	}

	public ProduceBom(int produceId, int materialId, BigDecimal scale, int receiveNum) {
		this.produceId = produceId;
		this.materialId = materialId;
		this.scale = scale;
		this.receiveNum = receiveNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProduceId() {
		return produceId;
	}

	public void setProduceId(int produceId) {
		this.produceId = produceId;
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

	public int getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(int receiveNum) {
		this.receiveNum = receiveNum;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

}
