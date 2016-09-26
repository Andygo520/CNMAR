package component.material.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 出库单原料关系 */
public class MaterialOutOrderMaterial {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int outOrderId; // 出库单id
	@JSONField(ordinal = 3)
	private int materialId; // 原料id
	@JSONField(ordinal = 4)
	private int preOutStock; // 待出库数量

	@JSONField(ordinal = 5)
	private int outStock;
	@JSONField(ordinal = 6)
	private Material material;
	@JSONField(ordinal = 7)
	private List<MaterialOutOrderSpace> outOrderSpaces;

	public MaterialOutOrderMaterial() {

	}

	public MaterialOutOrderMaterial(int outOrderId, int materialId, int preOutStock) {
		this.outOrderId = outOrderId;
		this.materialId = materialId;
		this.preOutStock = preOutStock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(int outOrderId) {
		this.outOrderId = outOrderId;
	}

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public int getPreOutStock() {
		return preOutStock;
	}

	public void setPreOutStock(int preOutStock) {
		this.preOutStock = preOutStock;
	}

	public int getOutStock() {
		return outStock;
	}

	public void setOutStock(int outStock) {
		this.outStock = outStock;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public List<MaterialOutOrderSpace> getOutOrderSpaces() {
		return outOrderSpaces;
	}

	public void setOutOrderSpaces(List<MaterialOutOrderSpace> outOrderSpaces) {
		this.outOrderSpaces = outOrderSpaces;
	}

}
