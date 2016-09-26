package component.material.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 原料仓位库存 */
public class MaterialSpaceStock {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int materialId; // 原料id
	@JSONField(ordinal = 3)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 4)
	private int stock; // 库存数量
	@JSONField(ordinal = 5)
	private int inOrderSpaceId; // 入库单原料仓位关系id

	@JSONField(ordinal = 6)
	private MaterialSpace space;
	@JSONField(ordinal = 7)
	private String inTime = "/"; // 入库批次号

	public MaterialSpaceStock() {

	}

	public MaterialSpaceStock(int materialId, int spaceId, int stock, int inOrderSpaceId) {
		this.materialId = materialId;
		this.spaceId = spaceId;
		this.stock = stock;
		this.inOrderSpaceId = inOrderSpaceId;
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

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getInOrderSpaceId() {
		return inOrderSpaceId;
	}

	public void setInOrderSpaceId(int inOrderSpaceId) {
		this.inOrderSpaceId = inOrderSpaceId;
	}

	public MaterialSpace getSpace() {
		return space;
	}

	public void setSpace(MaterialSpace space) {
		this.space = space;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

}
