package component.product.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 成品仓位库存 */
public class ProductSpaceStock {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int productId; // 成品id
	@JSONField(ordinal = 3)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 4)
	private int stock; // 库存数量
	@JSONField(ordinal = 5)
	private int inOrderSpaceId; // 入库单成品仓位关系id

	@JSONField(ordinal = 6)
	private ProductSpace space;
	@JSONField(ordinal = 7)
	private String inTime = "/"; // 入库批次号

	public ProductSpaceStock() {

	}

	public ProductSpaceStock(int productId, int spaceId, int stock, int inOrderSpaceId) {
		this.productId = productId;
		this.spaceId = spaceId;
		this.stock = stock;
		this.inOrderSpaceId = inOrderSpaceId;
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

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getInOrderSpaceId() {
		return inOrderSpaceId;
	}

	public void setInOrderSpaceId(int inOrderSpaceId) {
		this.inOrderSpaceId = inOrderSpaceId;
	}

	public ProductSpace getSpace() {
		return space;
	}

	public void setSpace(ProductSpace space) {
		this.space = space;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

}
