package component.half.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 半成品库存 */
public class HalfStock {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int halfId; // 半成品id
	@JSONField(ordinal = 3)
	private int stock; // 库存数量

	@JSONField(ordinal = 4)
	private Half half;
	@JSONField(ordinal = 5)
	List<HalfSpaceStock> spaceStocks;

	public HalfStock() {

	}

	public HalfStock(int halfId, int stock) {
		this.halfId = halfId;
		this.stock = stock;
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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Half getHalf() {
		return half;
	}

	public void setHalf(Half half) {
		this.half = half;
	}

	public List<HalfSpaceStock> getSpaceStocks() {
		return spaceStocks;
	}

	public void setSpaceStocks(List<HalfSpaceStock> spaceStocks) {
		this.spaceStocks = spaceStocks;
	}

}
