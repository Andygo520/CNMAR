package component.material.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/** 入库单原料仓位关系 */
public class MaterialInOrderSpace {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int inOrderId; // 入库单id
	@JSONField(ordinal = 3)
	private int materialId; // 原料id
	@JSONField(ordinal = 4)
	private int spaceId; // 仓位id
	@JSONField(ordinal = 5)
	private Integer preInStock; // 待入库数量
	@JSONField(ordinal = 6)
	private int inStock; // 已入库数量
	@JSONField(ordinal = 7)
	private String qrcode; // 二维码相对路径

	@JSONField(ordinal = 8)
	private Material material;
	@JSONField(ordinal = 9)
	private MaterialSpace space;
	@JSONField(ordinal = 10)
	private String inOrOut;

	@JSONField(ordinal = 11)
	private MaterialInOrder inOrder;
	@JSONField(ordinal = 12)
	private List<MaterialOutOrder> outOrders;

	public MaterialInOrderSpace() {

	}

	public MaterialInOrderSpace(int inOrderId, int materialId, int spaceId, Integer preInStock) {
		this.inOrderId = inOrderId;
		this.materialId = materialId;
		this.spaceId = spaceId;
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

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public Integer getPreInStock() {
		return preInStock;
	}

	public void setPreInStock(Integer preInStock) {
		this.preInStock = preInStock;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public MaterialSpace getSpace() {
		return space;
	}

	public void setSpace(MaterialSpace space) {
		this.space = space;
	}

	public String getInOrOut() {
		return inOrOut;
	}

	public void setInOrOut(String inOrOut) {
		this.inOrOut = inOrOut;
	}

	public MaterialInOrder getInOrder() {
		return inOrder;
	}

	public void setInOrder(MaterialInOrder inOrder) {
		this.inOrder = inOrder;
	}

	public List<MaterialOutOrder> getOutOrders() {
		return outOrders;
	}

	public void setOutOrders(List<MaterialOutOrder> outOrders) {
		this.outOrders = outOrders;
	}

}
