package component.custom.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;
import component.product.model.ProductOutOrder;

/** 交付计划 */
public class CustomDeliverPlan extends BaseModel {

	@JSONField(ordinal = 2)
	private int productOutOrderId; // 成品出库单号
	@JSONField(ordinal = 3)
	private String code; // 交付计划编号
	@JSONField(ordinal = 4)
	private String deliverOrder; // 交付订单号
	@JSONField(ordinal = 5, format = "yyyy-MM-dd")
	private Date deliverDate; // 交付日期

	@JSONField(ordinal = 6)
	private ProductOutOrder productOutOrder;
	@JSONField(ordinal = 7)
	private List<CustomDeliverProduct> deliverProducts;

	public int getProductOutOrderId() {
		return productOutOrderId;
	}

	public void setProductOutOrderId(int productOutOrderId) {
		this.productOutOrderId = productOutOrderId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDeliverOrder() {
		return deliverOrder;
	}

	public void setDeliverOrder(String deliverOrder) {
		this.deliverOrder = deliverOrder;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public ProductOutOrder getProductOutOrder() {
		return productOutOrder;
	}

	public void setProductOutOrder(ProductOutOrder productOutOrder) {
		this.productOutOrder = productOutOrder;
	}

	public List<CustomDeliverProduct> getDeliverProducts() {
		return deliverProducts;
	}

	public void setDeliverProducts(List<CustomDeliverProduct> deliverProducts) {
		this.deliverProducts = deliverProducts;
	}

}
