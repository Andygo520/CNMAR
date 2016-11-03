package component.half.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 半成品仓位库存 */
public class HalfSpaceStock {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int halfId; // 半成品id
	@JSONField(ordinal = 3)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 4)
	private int stock; // 库存数量
	@JSONField(ordinal = 5)
	private int inOrderSpaceId; // 入库单半成品仓位关系id

	@JSONField(ordinal = 6)
	private HalfSpace space;
	@JSONField(ordinal = 7)
	private String inTime = "/"; // 入库批次号

	public HalfSpaceStock() {

	}

	public HalfSpaceStock(int halfId, int spaceId, int stock, int inOrderSpaceId) {
		this.halfId = halfId;
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

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
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

	public HalfSpace getSpace() {
		return space;
	}

	public void setSpace(HalfSpace space) {
		this.space = space;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

}
