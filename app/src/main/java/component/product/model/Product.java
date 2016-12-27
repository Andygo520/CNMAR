package component.product.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.basic.model.Unit;
import component.basic.vo.MixTypeVo;
import component.basic.vo.PackTypeVo;
import component.basic.vo.StockTypeVo;
import component.category.model.Category;
import component.common.model.BaseModel;

/** 成品 */
public class Product extends BaseModel {

	@JSONField(ordinal = 1)
	private int categoryId; // BOM属性id
	@JSONField(ordinal = 2)
	private int unitId; // 单位id
	@JSONField(ordinal = 3)
	private String code; // 成品编码
	@JSONField(ordinal = 4)
	private String name; // 成品名称
	@JSONField(ordinal = 5)
	private String spec; // 规格
	@JSONField(ordinal = 6)
	private String remark; // 备注
	@JSONField(ordinal = 7)
	private int stockType; // 出入库操作 - 1扫描二维码2输入数量
	@JSONField(ordinal = 8)
	private int mixType; // 不同批次混仓 - 1不允许2允许
	@JSONField(ordinal = 9)
	private int packType; // 包装类型 - 1袋装2装箱3捆包4捆扎5料框
	@JSONField(ordinal = 10)
	private int packNum; // 包装数量
	@JSONField(ordinal = 11)
	private int minStock; // 预警最小库存
	@JSONField(ordinal = 12)
	private int maxStock; // 预警最大库存

	@JSONField(ordinal = 13)
	private Unit unit;
	@JSONField(ordinal = 14)
	private Category category;
	@JSONField(ordinal = 15)
	private ProductStock stock;
	@JSONField(serialize = false)
	private List<ProductSpace> spaces;
	@JSONField(serialize = false)
	private List<ProductBom> boms;
	@JSONField(serialize = false)
	private List<ProductHalfBom> halfBoms;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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

	public ProductStock getStock() {
		return stock;
	}

	public void setStock(ProductStock stock) {
		this.stock = stock;
	}

	public List<ProductSpace> getSpaces() {
		return spaces;
	}

	public void setSpaces(List<ProductSpace> spaces) {
		this.spaces = spaces;
	}

	public List<ProductBom> getBoms() {
		return boms;
	}

	public void setBoms(List<ProductBom> boms) {
		this.boms = boms;
	}

	public List<ProductHalfBom> getHalfBoms() {
		return halfBoms;
	}

	public void setHalfBoms(List<ProductHalfBom> halfBoms) {
		this.halfBoms = halfBoms;
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
