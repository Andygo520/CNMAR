package component.half.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;

/** 半成品仓位 */
public class HalfSpace extends BaseModel {

	@JSONField(ordinal = 1)
	private String code; // 仓位编码
	@JSONField(ordinal = 2)
	private String name; // 仓位名称
	@JSONField(ordinal = 3)
	private int halfId; // 半成品id
	@JSONField(ordinal = 4)
	private int capacity; // 仓位容量

	@JSONField(ordinal = 5)
	private Half half;
	@JSONField(ordinal = 6)
	private HalfSpaceStock spaceStock;

	public HalfSpace() {

	}

	public HalfSpace(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public HalfSpace(int id, int halfId, int capacity) {
		this.id = id;
		this.halfId = halfId;
		this.capacity = capacity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Half getHalf() {
		return half;
	}

	public void setHalf(Half half) {
		this.half = half;
	}

	public HalfSpaceStock getSpaceStock() {
		return spaceStock;
	}

	public void setSpaceStock(HalfSpaceStock spaceStock) {
		this.spaceStock = spaceStock;
	}

}
