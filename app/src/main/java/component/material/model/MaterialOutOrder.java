package component.material.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

import component.common.model.BaseModel;
import component.material.vo.OutOrderStatusVo;
import component.produce.model.ProduceReceive;

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
	private ProduceReceive produceReceive;
	@JSONField(ordinal = 6)
	private List<MaterialOutOrderMaterial> outOrderMaterials;
	@JSONField(ordinal = 7)
	private boolean showPrint;

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

	public ProduceReceive getProduceReceive() {
		return produceReceive;
	}

	public void setProduceReceive(ProduceReceive produceReceive) {
		this.produceReceive = produceReceive;
	}

	public List<MaterialOutOrderMaterial> getOutOrderMaterials() {
		return outOrderMaterials;
	}

	public void setOutOrderMaterials(List<MaterialOutOrderMaterial> outOrderMaterials) {
		this.outOrderMaterials = outOrderMaterials;
	}

	public boolean isShowPrint() {
		return showPrint;
	}

	public void setShowPrint(boolean showPrint) {
		this.showPrint = showPrint;
	}

	@JSONField(serialize = false)
	public OutOrderStatusVo getOutOrderStatusVo() {
		return OutOrderStatusVo.getInstance(status);
	}

}
