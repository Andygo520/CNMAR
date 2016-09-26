package component.product.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 出库单成品关系 */
public class ProductOutOrderProduct {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int outOrderId; // 出库单id
	@JSONField(ordinal = 3)
	private int productId; // 成品id
	@JSONField(ordinal = 4)
	private int preOutStock; // 待出库数量

	@JSONField(ordinal = 5)
	private int outStock;
	@JSONField(ordinal = 6)
	private Product product;
	@JSONField(ordinal = 7)
	private List<ProductOutOrderSpace> outOrderSpaces;

	public ProductOutOrderProduct() {

	}

	public ProductOutOrderProduct(int outOrderId, int productId, int preOutStock) {
		this.outOrderId = outOrderId;
		this.productId = productId;
		this.preOutStock = preOutStock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(int outOrderId) {
		this.outOrderId = outOrderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getPreOutStock() {
		return preOutStock;
	}

	public void setPreOutStock(int preOutStock) {
		this.preOutStock = preOutStock;
	}

	public int getOutStock() {
		return outStock;
	}

	public void setOutStock(int outStock) {
		this.outStock = outStock;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductOutOrderSpace> getOutOrderSpaces() {
		return outOrderSpaces;
	}

	public void setOutOrderSpaces(List<ProductOutOrderSpace> outOrderSpaces) {
		this.outOrderSpaces = outOrderSpaces;
	}

}
