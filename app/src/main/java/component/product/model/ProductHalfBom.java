package component.product.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.text.NumberFormat;

import component.half.model.Half;
import component.produce.model.ProduceBom;

/** 成品 - 半成品BOM */
public class ProductHalfBom {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private int productId; // 成品id
	@JSONField(serialize = false)
	private int halfId; // 半成品id
	@JSONField(serialize = false)
	private BigDecimal scale; // 比例

	@JSONField(ordinal = 7)
	private Half half;
	@JSONField(serialize = false)
	private BigDecimal num;

	@JSONField(ordinal = 5)
	private ProduceBom produceBom;

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

	@JSONField(serialize = false)
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

	@JSONField(serialize = false)
	public String getNumStr() {
		return num == null ? "" : NumberFormat.getInstance().format(num).replaceAll(",", "");
	}

	public ProduceBom getProduceBom() {
		return produceBom;
	}

	public void setProduceBom(ProduceBom produceBom) {
		this.produceBom = produceBom;
	}

}
