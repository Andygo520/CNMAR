package component.product.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;

/** 成品仓位 */
public class ProductSpace extends BaseModel implements Cloneable {

	@JSONField(ordinal = 1)
	private String code; // 仓位编码
	@JSONField(ordinal = 3)
	private int productId; // 成品id
	@JSONField(ordinal = 4)
	private int capacity; // 仓位容量

	@JSONField(ordinal = 5)
	private Product product;
	@JSONField(ordinal = 6)
	private ProductSpaceStock spaceStock;

	public ProductSpace() {

	}

	public ProductSpace(String code) {
		this.code = code;
	}

	public ProductSpace(int id, int productId, int capacity) {
		this.id = id;
		this.productId = productId;
		this.capacity = capacity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductSpaceStock getSpaceStock() {
		return spaceStock;
	}

	public void setSpaceStock(ProductSpaceStock spaceStock) {
		this.spaceStock = spaceStock;
	}

	public ProductSpace clone() throws CloneNotSupportedException {
		return (ProductSpace) super.clone();
	}

}
