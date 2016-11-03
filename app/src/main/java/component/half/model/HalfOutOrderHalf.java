package component.half.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 出库单半成品关系 */
public class HalfOutOrderHalf {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int outOrderId; // 出库单id
	@JSONField(ordinal = 3)
	private int halfId; // 半成品id
	@JSONField(ordinal = 4)
	private int preOutStock; // 待出库数量

	@JSONField(ordinal = 5)
	private int outStock;
	@JSONField(ordinal = 6)
	private Half half;
	@JSONField(ordinal = 7)
	private List<HalfOutOrderSpace> outOrderSpaces;

	public HalfOutOrderHalf() {

	}

	public HalfOutOrderHalf(int outOrderId, int halfId, int preOutStock) {
		this.outOrderId = outOrderId;
		this.halfId = halfId;
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

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
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

	public Half getHalf() {
		return half;
	}

	public void setHalf(Half half) {
		this.half = half;
	}

	public List<HalfOutOrderSpace> getOutOrderSpaces() {
		return outOrderSpaces;
	}

	public void setOutOrderSpaces(List<HalfOutOrderSpace> outOrderSpaces) {
		this.outOrderSpaces = outOrderSpaces;
	}

}
