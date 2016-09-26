package component.material.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 原料库存 */
public class MaterialStock {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int materialId; // 原料id
	@JSONField(ordinal = 3)
	private int stock; // 库存数量

	@JSONField(ordinal = 4)
	private Material material;
	@JSONField(ordinal = 5)
	List<MaterialSpaceStock> spaceStocks;

	public MaterialStock() {

	}

	public MaterialStock(int materialId, int stock) {
		this.materialId = materialId;
		this.stock = stock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public List<MaterialSpaceStock> getSpaceStocks() {
		return spaceStocks;
	}

	public void setSpaceStocks(List<MaterialSpaceStock> spaceStocks) {
		this.spaceStocks = spaceStocks;
	}

}
