package component.material.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;

/** 原料仓位 */
public class MaterialSpace extends BaseModel implements Cloneable {

	@JSONField(ordinal = 1)
	private String code; // 仓位编码
	@JSONField(ordinal = 3)
	private int materialId; // 原料id
	@JSONField(ordinal = 4)
	private int capacity; // 仓位容量

	@JSONField(ordinal = 5)
	private Material material;
	@JSONField(ordinal = 6)
	private MaterialSpaceStock spaceStock;

	public MaterialSpace() {

	}

	public MaterialSpace(String code) {
		this.code = code;
	}

	public MaterialSpace(int id, int materialId, int capacity) {
		this.id = id;
		this.materialId = materialId;
		this.capacity = capacity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public MaterialSpaceStock getSpaceStock() {
		return spaceStock;
	}

	public void setSpaceStock(MaterialSpaceStock spaceStock) {
		this.spaceStock = spaceStock;
	}

	public MaterialSpace clone() throws CloneNotSupportedException {
		return (MaterialSpace) super.clone();
	}

}
