package component.product.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.basic.vo.MixTypeVo;
import component.basic.vo.StockTypeVo;
import component.common.model.BaseModel;

/** 成品 */
public class Product extends BaseModel {

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
	private int minStock; // 预警最小库存
	@JSONField(ordinal = 10)
	private int maxStock; // 预警最大库存

	@JSONField(ordinal = 12)
	private ProductUnit unit;
	@JSONField(serialize = false)
	private ProductStock stock;

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

	public ProductUnit getUnit() {
		return unit;
	}

	public void setUnit(ProductUnit unit) {
		this.unit = unit;
	}

	public ProductStock getStock() {
		return stock;
	}

	public void setStock(ProductStock stock) {
		this.stock = stock;
	}

	@JSONField(serialize = false)
	public StockTypeVo getStockTypeVo() {
		return StockTypeVo.getInstance(stockType);
	}

	@JSONField(serialize = false)
	public MixTypeVo getMixTypeVo() {
		return MixTypeVo.getInstance(mixType);
	}

}
