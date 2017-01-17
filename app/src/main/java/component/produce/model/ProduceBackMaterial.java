package component.produce.model;

import com.alibaba.fastjson.annotation.JSONField;
import component.material.model.Material;
import component.material.model.MaterialSpace;

/** 领料单退原料 */
public class ProduceBackMaterial {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int outOrderId; // 原料出库单id
	@JSONField(ordinal = 3)
	private int materialId; // 原料id
	@JSONField(ordinal = 4)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 5)
	private int backNum; // 回仓数量
	@JSONField(ordinal = 6)
	private int inOrderSpaceId; // 入库单原料仓位关系id

	@JSONField(ordinal = 7)
	private Material material;
	@JSONField(ordinal = 8)
	private MaterialSpace space;

	public ProduceBackMaterial() {

	}

	public ProduceBackMaterial(int outOrderId, int materialId, int spaceId, int backNum, int inOrderSpaceId) {
		this.outOrderId = outOrderId;
		this.materialId = materialId;
		this.spaceId = spaceId;
		this.backNum = backNum;
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

	public int getBackNum() {
		return backNum;
	}

	public void setBackNum(int backNum) {
		this.backNum = backNum;
	}

	public int getInOrderSpaceId() {
		return inOrderSpaceId;
	}

	public void setInOrderSpaceId(int inOrderSpaceId) {
		this.inOrderSpaceId = inOrderSpaceId;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public MaterialSpace getSpace() {
		return space;
	}

	public void setSpace(MaterialSpace space) {
		this.space = space;
	}

}
