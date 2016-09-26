package component.product.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 入库单成品关系 */
public class ProductInOrderProduct {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int inOrderId; // 入库单id
	@JSONField(ordinal = 3)
	private int productId; // 成品id
	@JSONField(ordinal = 4)
	private int preInStock; // 待入库数量

	@JSONField(ordinal = 6)
	private int inStock;
	@JSONField(ordinal = 7)
	private Product product;
	@JSONField(ordinal = 9)
	private List<ProductInOrderSpace> inOrderSpaces;

	public ProductInOrderProduct() {

	}

	public ProductInOrderProduct(int inOrderId, int productId, int preInStock) {
		this.inOrderId = inOrderId;
		this.productId = productId;
		this.preInStock = preInStock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(int inOrderId) {
		this.inOrderId = inOrderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getPreInStock() {
		return preInStock;
	}

	public void setPreInStock(int preInStock) {
		this.preInStock = preInStock;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductInOrderSpace> getInOrderSpaces() {
		return inOrderSpaces;
	}

	public void setInOrderSpaces(List<ProductInOrderSpace> inOrderSpaces) {
		this.inOrderSpaces = inOrderSpaces;
	}

}
