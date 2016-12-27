package component.material.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 原料仓位库存盘点 */
public class MaterialSpaceStockCheck {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int checkId; // 盘点id
	@JSONField(ordinal = 3)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 4)
	private int inOrderSpaceId; // 入库单原料仓位关系id
	@JSONField(ordinal = 5)
	private int beforeStock; // 盘点前数量
	@JSONField(ordinal = 6)
	private int afterStock; // 盘点后数量

	@JSONField(ordinal = 7)
	private MaterialSpace space;

	public MaterialSpaceStockCheck() {

	}

	public MaterialSpaceStockCheck(int checkId, int spaceId, int inOrderSpaceId, int beforeStock, int afterStock) {
		this.checkId = checkId;
		this.spaceId = spaceId;
		this.inOrderSpaceId = inOrderSpaceId;
		this.beforeStock = beforeStock;
		this.afterStock = afterStock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCheckId() {
		return checkId;
	}

	public void setCheckId(int checkId) {
		this.checkId = checkId;
	}

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public int getInOrderSpaceId() {
		return inOrderSpaceId;
	}

	public void setInOrderSpaceId(int inOrderSpaceId) {
		this.inOrderSpaceId = inOrderSpaceId;
	}

	public int getBeforeStock() {
		return beforeStock;
	}

	public void setBeforeStock(int beforeStock) {
		this.beforeStock = beforeStock;
	}

	public int getAfterStock() {
		return afterStock;
	}

	public void setAfterStock(int afterStock) {
		this.afterStock = afterStock;
	}

	public MaterialSpace getSpace() {
		return space;
	}

	public void setSpace(MaterialSpace space) {
		this.space = space;
	}

}
