package component.material.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 入库单原料关系 */
public class MaterialInOrderMaterial {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int inOrderId; // 入库单id
	@JSONField(ordinal = 3)
	private int materialId; // 原料id
	@JSONField(ordinal = 4)
	private int preInStock; // 待入库数量
	@JSONField(ordinal = 5)
	private int failNum; // 不合格数量

	@JSONField(ordinal = 6)
	private int inStock;
	@JSONField(ordinal = 7)
	private Material material;
	@JSONField(ordinal = 8)
	private MaterialSampleStandard standard;
	@JSONField(ordinal = 9)
	private List<MaterialInOrderSpace> inOrderSpaces;

	public MaterialInOrderMaterial() {

	}

	public MaterialInOrderMaterial(int inOrderId, int materialId, int preInStock, int failNum) {
		this.inOrderId = inOrderId;
		this.materialId = materialId;
		this.preInStock = preInStock;
		this.failNum = failNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(int inOrderId) {
		this.inOrderId = inOrderId;
	}

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public int getPreInStock() {
		return preInStock;
	}

	public void setPreInStock(int preInStock) {
		this.preInStock = preInStock;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public MaterialSampleStandard getStandard() {
		return standard;
	}

	public void setStandard(MaterialSampleStandard standard) {
		this.standard = standard;
	}

	public List<MaterialInOrderSpace> getInOrderSpaces() {
		return inOrderSpaces;
	}

	public void setInOrderSpaces(List<MaterialInOrderSpace> inOrderSpaces) {
		this.inOrderSpaces = inOrderSpaces;
	}

}
