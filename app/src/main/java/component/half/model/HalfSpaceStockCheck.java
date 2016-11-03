package component.half.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 半成品仓位库存盘点 */
public class HalfSpaceStockCheck {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int checkId; // 盘点id
	@JSONField(ordinal = 3)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 4)
	private int beforeStock; // 盘点前数量
	@JSONField(ordinal = 5)
	private int afterStock; // 盘点后数量

	@JSONField(ordinal = 6)
	private HalfSpace space;

	public HalfSpaceStockCheck() {

	}

	public HalfSpaceStockCheck(int checkId, int spaceId, int beforeStock, int afterStock) {
		this.checkId = checkId;
		this.spaceId = spaceId;
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

	public HalfSpace getSpace() {
		return space;
	}

	public void setSpace(HalfSpace space) {
		this.space = space;
	}

}
