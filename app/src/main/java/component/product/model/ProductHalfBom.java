package component.product.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

import component.half.model.Half;

/** 成品 - 半成品BOM */
public class ProductHalfBom {

	private int id;
	private int productId; // 成品id
	private int halfId; // 半成品id
	private BigDecimal scale; // 比例

	private Half half;
	private BigDecimal num;

	public ProductHalfBom() {

	}

	public ProductHalfBom(int productId, int halfId, BigDecimal scale) {
		this.productId = productId;
		this.halfId = halfId;
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

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
	}

	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

	public String getScaleStr() {
		return NumberFormat.getInstance().format(scale).replaceAll(",", "");
	}

	public Half getHalf() {
		return half;
	}

	public void setHalf(Half half) {
		this.half = half;
	}

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public String getNumStr() {
		return num == null ? "" : NumberFormat.getInstance().format(num).replaceAll(",", "");
	}

}
