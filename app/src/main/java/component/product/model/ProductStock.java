package component.product.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 成品库存 */
public class ProductStock {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int productId; // 成品id
	@JSONField(ordinal = 3)
	private int stock; // 库存数量

	@JSONField(ordinal = 4)
	private Product product;
	@JSONField(ordinal = 5)
	List<ProductSpaceStock> spaceStocks;

	public ProductStock() {

	}

	public ProductStock(int productId, int stock) {
		this.productId = productId;
		this.stock = stock;
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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductSpaceStock> getSpaceStocks() {
		return spaceStocks;
	}

	public void setSpaceStocks(List<ProductSpaceStock> spaceStocks) {
		this.spaceStocks = spaceStocks;
	}

}
