package component.half.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 入库单半成品关系 */
public class HalfInOrderHalf {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int inOrderId; // 入库单id
	@JSONField(ordinal = 3)
	private int halfId; // 半成品id
	@JSONField(ordinal = 4)
	private int preInStock; // 待入库数量

	@JSONField(ordinal = 6)
	private int inStock;
	@JSONField(ordinal = 7)
	private Half half;
	@JSONField(ordinal = 9)
	private List<HalfInOrderSpace> inOrderSpaces;

	public HalfInOrderHalf() {

	}

	public HalfInOrderHalf(int inOrderId, int halfId, int preInStock) {
		this.inOrderId = inOrderId;
		this.halfId = halfId;
		this.preInStock = preInStock;
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

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
	}

	public int getPreInStock() {
		return preInStock;
	}

	public void setPreInStock(int preInStock) {
		this.preInStock = preInStock;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public Half getHalf() {
		return half;
	}

	public void setHalf(Half half) {
		this.half = half;
	}

	public List<HalfInOrderSpace> getInOrderSpaces() {
		return inOrderSpaces;
	}

	public void setInOrderSpaces(List<HalfInOrderSpace> inOrderSpaces) {
		this.inOrderSpaces = inOrderSpaces;
	}

}
