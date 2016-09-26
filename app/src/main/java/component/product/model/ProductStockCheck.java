package component.product.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 成品库存盘点 */
public class ProductStockCheck {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int productId; // 成品id
	@JSONField(ordinal = 3)
	private int beforeStock; // 盘点前数量
	@JSONField(ordinal = 4)
	private int afterStock; // 盘点后数量
	@JSONField(ordinal = 5)
	private int cid;
	@JSONField(ordinal = 6)
	private Date ctime;

	@JSONField(ordinal = 7)
	private Product product;
	@JSONField(ordinal = 8)
	private List<ProductSpaceStockCheck> spaceChecks;

	public ProductStockCheck() {

	}

	public ProductStockCheck(int productId, int beforeStock, int afterStock, int cid, Date ctime) {
		this.productId = productId;
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

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductSpaceStockCheck> getSpaceChecks() {
		return spaceChecks;
	}

	public void setSpaceChecks(List<ProductSpaceStockCheck> spaceChecks) {
		this.spaceChecks = spaceChecks;
	}

}
