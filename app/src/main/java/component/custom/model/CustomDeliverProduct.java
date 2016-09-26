package component.custom.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.product.model.Product;

/** 交付计划成品关系 */
public class CustomDeliverProduct {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int deliverId; // 交付计划id
	@JSONField(ordinal = 3)
	private int productId; // 成品id
	@JSONField(ordinal = 4)
	private int deliverNum; // 交付数量

	@JSONField(ordinal = 5)
	private Product product;

	public CustomDeliverProduct() {

	}

	public CustomDeliverProduct(int deliverId, int productId, int deliverNum) {
		this.deliverId = deliverId;
		this.productId = productId;
		this.deliverNum = deliverNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDeliverId() {
		return deliverId;
	}

	public void setDeliverId(int deliverId) {
		this.deliverId = deliverId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getDeliverNum() {
		return deliverNum;
	}

	public void setDeliverNum(int deliverNum) {
		this.deliverNum = deliverNum;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
