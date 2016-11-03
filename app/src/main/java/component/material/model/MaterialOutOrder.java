package component.material.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;
import component.material.vo.OutOrderStatusVo;
import component.produce.model.ProducePlan;
import component.system.model.SystemUser;

/** 原料出库单 */
public class MaterialOutOrder extends BaseModel {

	@JSONField(ordinal = 1)
	private String code; // 出库单号
	@JSONField(ordinal = 2, format = "yyyyMMddHHmmss")
	private Date outTime; // 出库时间
	@JSONField(ordinal = 3)
	private String remark; // 备注
	@JSONField(ordinal = 4)
	private int status; // 出库单状态 - 2待出库3已出库4未全部出库
	@JSONField(ordinal = 5)
	private int receiveId; // 领料人id

	@JSONField(ordinal = 6)
	private SystemUser receive;
	@JSONField(ordinal = 7)
	private ProducePlan producePlan;
	@JSONField(ordinal = 8)
	private List<MaterialOutOrderMaterial> outOrderMaterials;

	public MaterialOutOrder() {

	}

	public MaterialOutOrder(String code, String remark, int status, int cid, Date ctime) {
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

	public int getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
	}

	public SystemUser getReceive() {
		return receive;
	}

	public void setReceive(SystemUser receive) {
		this.receive = receive;
	}

	public ProducePlan getProducePlan() {
		return producePlan;
	}

	public void setProducePlan(ProducePlan producePlan) {
		this.producePlan = producePlan;
	}

	public List<MaterialOutOrderMaterial> getOutOrderMaterials() {
		return outOrderMaterials;
	}

	public void setOutOrderMaterials(List<MaterialOutOrderMaterial> outOrderMaterials) {
		this.outOrderMaterials = outOrderMaterials;
	}

	@JSONField(serialize = false)
	public OutOrderStatusVo getOutOrderStatusVo() {
		return OutOrderStatusVo.getInstance(status);
	}

}
