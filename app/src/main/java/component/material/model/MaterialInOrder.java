package component.material.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;
import component.material.vo.InOrderStatusVo;

/** 原料入库单 */
public class MaterialInOrder extends BaseModel {

	@JSONField(ordinal = 1)
	private String code; // 入库单号
	@JSONField(ordinal = 2, format = "yyyyMMddHHmmss")
	private Date inTime; // 入库时间
	@JSONField(ordinal = 3)
	private String purchaseOrder; // 采购订单号
	@JSONField(ordinal = 4, format = "yyyy-MM-dd")
	private Date arrivalDate; // 到货日期
	@JSONField(ordinal = 5)
	private String remark; // 备注
	@JSONField(ordinal = 6)
	private int status; // 入库单状态 - 1待打印2待入库3已入库4待检验5检验不合格

	@JSONField(ordinal = 7)
	private List<MaterialInOrderMaterial> inOrderMaterials;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
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

	public List<MaterialInOrderMaterial> getInOrderMaterials() {
		return inOrderMaterials;
	}

	public void setInOrderMaterials(List<MaterialInOrderMaterial> inOrderMaterials) {
		this.inOrderMaterials = inOrderMaterials;
	}

	@JSONField(serialize = false)
	public InOrderStatusVo getInOrderStatusVo() {
		return InOrderStatusVo.getInstance(status);
	}

}
