package component.half.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 出库单半成品仓位关系 */
public class HalfOutOrderSpace {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int outOrderId; // 出库单id
	@JSONField(ordinal = 3)
	private int halfId; // 半成品id
	@JSONField(ordinal = 4)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 5)
	private Integer preOutStock; // 待出库数量
	@JSONField(ordinal = 6)
	private int outStock; // 已出库数量
	@JSONField(ordinal = 7)
	private int inOrderSpaceId; // 入库单半成品仓位关系id

	@JSONField(ordinal = 8)
	private HalfSpace space;

	public HalfOutOrderSpace() {

	}

	public HalfOutOrderSpace(int outOrderId, int halfId, int spaceId, Integer preOutStock) {
		this.outOrderId = outOrderId;
		this.halfId = halfId;
		this.spaceId = spaceId;
		this.preOutStock = preOutStock;
	}

	public HalfOutOrderSpace(int outOrderId, int halfId, int spaceId, Integer preOutStock, int inOrderSpaceId) {
		this.outOrderId = outOrderId;
		this.halfId = halfId;
		this.spaceId = spaceId;
		this.preOutStock = preOutStock;
		this.inOrderSpaceId = inOrderSpaceId;
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

	public Integer getPreOutStock() {
		return preOutStock;
	}

	public void setPreOutStock(Integer preOutStock) {
		this.preOutStock = preOutStock;
	}

	public int getOutStock() {
		return outStock;
	}

	public void setOutStock(int outStock) {
		this.outStock = outStock;
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

}
