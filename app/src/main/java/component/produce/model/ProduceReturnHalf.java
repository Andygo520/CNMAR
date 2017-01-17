package component.produce.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.half.model.Half;
import component.half.model.HalfSpace;

/** 领料单退半成品 */
public class ProduceReturnHalf {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int receiveId; // 领料单id
	@JSONField(ordinal = 3)
	private int halfId; // 半成品id
	@JSONField(ordinal = 4)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 5)
	private int returnNum; // 回仓数量
	@JSONField(ordinal = 6)
	private int inOrderSpaceId; // 入库单半成品仓位关系id

	@JSONField(ordinal = 7)
	private Half half;
	@JSONField(ordinal = 8)
	private HalfSpace space;

	public ProduceReturnHalf() {

	}

	public ProduceReturnHalf(int receiveId, int halfId, int spaceId, int returnNum, int inOrderSpaceId) {
		this.receiveId = receiveId;
		this.halfId = halfId;
		this.spaceId = spaceId;
		this.returnNum = returnNum;
		this.inOrderSpaceId = inOrderSpaceId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
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

	public int getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(int returnNum) {
		this.returnNum = returnNum;
	}

	public int getInOrderSpaceId() {
		return inOrderSpaceId;
	}

	public void setInOrderSpaceId(int inOrderSpaceId) {
		this.inOrderSpaceId = inOrderSpaceId;
	}

	public Half getHalf() {
		return half;
	}

	public void setHalf(Half half) {
		this.half = half;
	}

	public HalfSpace getSpace() {
		return space;
	}

	public void setSpace(HalfSpace space) {
		this.space = space;
	}

}
