package component.product.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;
import component.custom.model.CustomDeliverPlan;
import component.product.vo.OutOrderStatusVo;

/** 成品出库单 */
public class ProductOutOrder extends BaseModel {

	@JSONField(ordinal = 1)
	private String code; // 出库单号
	@JSONField(ordinal = 2, format = "yyyyMMddHHmmss")
	private Date outTime; // 出库时间
	@JSONField(ordinal = 3)
	private String remark; // 备注
	@JSONField(ordinal = 4)
	private int status; // 出库单状态 - 2待出库3已出库4未全部出库

	@JSONField(ordinal = 5)
	private CustomDeliverPlan deliverPlan;
	@JSONField(ordinal = 6)
	private List<ProductOutOrderProduct> outOrderProducts;

	public ProductOutOrder() {

	}

	public ProductOutOrder(String code, String remark, int status, int cid, Date ctime) {
		this.code = code;
		this.remark = remark;
		this.status = status;
		this.cid = cid;
		this.ctime = ctime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public CustomDeliverPlan getDeliverPlan() {
		return deliverPlan;
	}

	public void setDeliverPlan(CustomDeliverPlan deliverPlan) {
		this.deliverPlan = deliverPlan;
	}

	public List<ProductOutOrderProduct> getOutOrderProducts() {
		return outOrderProducts;
	}

	public void setOutOrderProducts(List<ProductOutOrderProduct> outOrderProducts) {
		this.outOrderProducts = outOrderProducts;
	}

	@JSONField(serialize = false)
	public OutOrderStatusVo getOutOrderStatusVo() {
		return OutOrderStatusVo.getInstance(status);
	}

}
