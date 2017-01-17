package component.material.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.basic.model.Unit;
import component.basic.vo.MixTypeVo;
import component.basic.vo.PackTypeVo;
import component.basic.vo.StockTypeVo;
import component.category.model.Category;
import component.common.model.BaseModel;
import component.supply.model.Supply;

/** 原料 */
public class Material extends BaseModel {

	@JSONField(ordinal = 1)
	private int categoryId; // BOM属性id
	@JSONField(ordinal = 2)
	private int supplyId; // 供应商id
	@JSONField(ordinal = 3)
	private int unitId; // 单位id
	@JSONField(ordinal = 4)
	private String code; // 原料编码
	@JSONField(ordinal = 5)
	private String name; // 原料名称
	@JSONField(ordinal = 6)
	private String spec; // 规格
	@JSONField(ordinal = 7)
	private String remark; // 备注
	@JSONField(ordinal = 8)
	private int stockType; // 出入库操作 - 1扫描二维码2输入数量
	@JSONField(ordinal = 9)
	private int mixType; // 不同批次混仓 - 1不允许2允许
	@JSONField(ordinal = 10)
	private int packType; // 包装类型 - 1袋装2装箱3捆包4捆扎5料框
	@JSONField(ordinal = 11)
	private int packNum; // 包装数量
	@JSONField(ordinal = 12)
	private int minStock; // 预警最小库存
	@JSONField(ordinal = 13)
	private int maxStock; // 预警最大库存

	@JSONField(ordinal = 14)
	private Supply supply;
	@JSONField(ordinal = 15)
	private Unit unit;
	@JSONField(ordinal = 16)
	private Category category;
	@JSONField(ordinal = 17)
	private MaterialStock stock;
	@JSONField(serialize = false)
	private List<MaterialSpace> spaces;
	@JSONField(serialize = false)
	private int level;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStockType() {
		return stockType;
	}

	public void setStockType(int stockType) {
		this.stockType = stockType;
	}

	public int getMixType() {
		return mixType;
	}

	public void setMixType(int mixType) {
		this.mixType = mixType;
	}

	public int getPackType() {
		return packType;
	}

	public void setPackType(int packType) {
		this.packType = packType;
	}

	public int getPackNum() {
		return packNum;
	}

	public void setPackNum(int packNum) {
		this.packNum = packNum;
	}

	public int getMinStock() {
		return minStock;
	}

	public void setMinStock(int minStock) {
		this.minStock = minStock;
	}

	public int getMaxStock() {
		return maxStock;
	}

	public void setMaxStock(int maxStock) {
		this.maxStock = maxStock;
	}

	public Supply getSupply() {
		return supply;
	}

	public void setSupply(Supply supply) {
		this.supply = supply;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public MaterialStock getStock() {
		return stock;
	}

	public void setStock(MaterialStock stock) {
		this.stock = stock;
	}

	public List<MaterialSpace> getSpaces() {
		return spaces;
	}

	public void setSpaces(List<MaterialSpace> spaces) {
		this.spaces = spaces;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@JSONField(serialize = false)
	public StockTypeVo getStockTypeVo() {
		return StockTypeVo.getInstance(stockType);
	}

	@JSONField(serialize = false)
	public MixTypeVo getMixTypeVo() {
		return MixTypeVo.getInstance(mixType);
	}

	@JSONField(serialize = false)
	public PackTypeVo getPackTypeVo() {
		return PackTypeVo.getInstance(packType);
	}

}
