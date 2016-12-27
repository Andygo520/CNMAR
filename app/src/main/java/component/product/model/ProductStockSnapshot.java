package component.product.model;

import java.util.Date;

/** 成品库存镜像 */
public class ProductStockSnapshot {

	private int id;
	private String code; // 成品编码
	private String name; // 成品名称
	private String spec; // 规格
	private String unit; // 单位
	private String stock; // 库存数量
	private Date ctime; // 创建时间

	public ProductStockSnapshot() {

	}

	public ProductStockSnapshot(String code, String name, String spec, String unit, String stock, Date ctime) {
		this.code = code;
		this.name = name;
		this.spec = spec;
		this.unit = unit;
		this.stock = stock;
		this.ctime = ctime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
