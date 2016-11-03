package component.material.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;
import component.material.vo.InOrderStatusVo;
import component.system.model.SystemUser;

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
	private int status; // 入库单状态 - 1待打印2待入库3已入库4未全部入库5待检验6检验不合格
	@JSONField(ordinal = 7)
	private int testId;
	@JSONField(ordinal = 8, format = "yyyy-MM-dd HH:mm:ss")
	private Date testTime;

	@JSONField(ordinal = 9)
	private SystemUser test;
	@JSONField(ordinal = 10)
	private List<MaterialInOrderMaterial> inOrderMaterials;
	@JSONField(serialize = false)
	private boolean showPrint;

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

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public Date getTestTime() {
		return testTime;
	}

	public void setTestTime(Date testTime) {
		this.testTime = testTime;
	}

	public SystemUser getTest() {
		return test;
	}

	public void setTest(SystemUser test) {
		this.test = test;
	}

	public List<MaterialInOrderMaterial> getInOrderMaterials() {
		return inOrderMaterials;
	}

	public void setInOrderMaterials(List<MaterialInOrderMaterial> inOrderMaterials) {
		this.inOrderMaterials = inOrderMaterials;
	}

	public boolean isShowPrint() {
		return showPrint;
	}

	public void setShowPrint(boolean showPrint) {
		this.showPrint = showPrint;
	}

	@JSONField(serialize = false)
	public InOrderStatusVo getInOrderStatusVo() {
		return InOrderStatusVo.getInstance(status);
	}

}
