package component.product.model;

import java.math.BigDecimal;

import component.material.model.Material;

/** 成品物料清单 */
public class ProductBom {

	private int id;
	private int productId; // 成品id
	private int materialId; // 原料id
	private BigDecimal scale; // 比例

	private Material material;

	public ProductBom() {

	}

	public ProductBom(int productId, int materialId, BigDecimal scale) {
		this.productId = productId;
		this.materialId = materialId;
		this.scale = scale;
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

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

}
