package component.material.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 原料库存盘点 */
public class MaterialStockCheck {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int materialId; // 原料id
	@JSONField(ordinal = 3)
	private int beforeStock; // 盘点前数量
	@JSONField(ordinal = 4)
	private int afterStock; // 盘点后数量
	@JSONField(ordinal = 5)
	private int cid;
	@JSONField(ordinal = 6)
	private Date ctime;

	@JSONField(ordinal = 7)
	private Material material;
	@JSONField(ordinal = 8)
	private List<MaterialSpaceStockCheck> spaceChecks;

	public MaterialStockCheck() {

	}

	public MaterialStockCheck(int materialId, int beforeStock, int afterStock, int cid, Date ctime) {
		this.materialId = materialId;
		this.beforeStock = beforeStock;
		this.afterStock = afterStock;
		this.cid = cid;
		this.ctime = ctime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
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

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public List<MaterialSpaceStockCheck> getSpaceChecks() {
		return spaceChecks;
	}

	public void setSpaceChecks(List<MaterialSpaceStockCheck> spaceChecks) {
		this.spaceChecks = spaceChecks;
	}

}
