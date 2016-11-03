package component.half.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 半成品库存盘点 */
public class HalfStockCheck {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int halfId; // 半成品id
	@JSONField(ordinal = 3)
	private int beforeStock; // 盘点前数量
	@JSONField(ordinal = 4)
	private int afterStock; // 盘点后数量
	@JSONField(ordinal = 5)
	private int cid;
	@JSONField(ordinal = 6)
	private Date ctime;

	@JSONField(ordinal = 7)
	private Half half;
	@JSONField(ordinal = 8)
	private List<HalfSpaceStockCheck> spaceChecks;

	public HalfStockCheck() {

	}

	public HalfStockCheck(int halfId, int beforeStock, int afterStock, int cid, Date ctime) {
		this.halfId = halfId;
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

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
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

	public Half getHalf() {
		return half;
	}

	public void setHalf(Half half) {
		this.half = half;
	}

	public List<HalfSpaceStockCheck> getSpaceChecks() {
		return spaceChecks;
	}

	public void setSpaceChecks(List<HalfSpaceStockCheck> spaceChecks) {
		this.spaceChecks = spaceChecks;
	}

}
