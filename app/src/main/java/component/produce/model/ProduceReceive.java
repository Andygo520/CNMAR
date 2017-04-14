package component.produce.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.half.model.HalfOutOrder;
import component.material.model.MaterialOutOrder;
import component.produce.vo.ReceiveStatusVo;
import component.system.model.SystemUser;

/** 领料单 */
public class ProduceReceive {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int planId; // 加工单id
	@JSONField(ordinal = 3)
	private int bomId; // 子加工单id
	@JSONField(ordinal = 4)
	private int materialOutOrderId; // 原料出库单id
	@JSONField(ordinal = 5)
	private int halfOutOrderId; // 半成品出库单id
	@JSONField(ordinal = 6)
	private String code; // 领料单编号
	@JSONField(ordinal = 7)
	private int receiveId; // 领料人id
	@JSONField(ordinal = 8, format = "yyyy-MM-dd HH:mm:ss")
	private Date receiveTime; // 领料时间
	@JSONField(ordinal = 9)
	private int backId; // 退料人id
	@JSONField(ordinal = 10, format = "yyyy-MM-dd HH:mm:ss")
	private Date backTime; // 退料时间
	@JSONField(ordinal = 11)
	private int status; // 领料单状态 - 1未领料2已领料

	@JSONField(ordinal = 13)
	private ProducePlan plan;
	@JSONField(ordinal = 14)
	private ProduceBom bom;
	@JSONField(ordinal = 15)
	private MaterialOutOrder materialOutOrder;
	@JSONField(ordinal = 16)
	private HalfOutOrder halfOutOrder;
	@JSONField(ordinal = 17)
	private SystemUser receiveUser;
	@JSONField(serialize = false)
	private SystemUser backUser;
	@JSONField(serialize = false)
	private List<ProduceBackMaterial> backMaterials;
	@JSONField(serialize = false)
	private List<ProduceBackHalf> backHalfs;

	public ProduceReceive() {

	}

	public ProduceReceive(int planId, int bomId, String code, int receiveId, Date receiveTime, int status) {
		this.planId = planId;
		this.bomId = bomId;
		this.code = code;
		this.receiveId = receiveId;
		this.receiveTime = receiveTime;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public int getBomId() {
		return bomId;
	}

	public void setBomId(int bomId) {
		this.bomId = bomId;
	}

	public int getMaterialOutOrderId() {
		return materialOutOrderId;
	}

	public void setMaterialOutOrderId(int materialOutOrderId) {
		this.materialOutOrderId = materialOutOrderId;
	}

	public int getHalfOutOrderId() {
		return halfOutOrderId;
	}

	public void setHalfOutOrderId(int halfOutOrderId) {
		this.halfOutOrderId = halfOutOrderId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public int getBackId() {
		return backId;
	}

	public void setBackId(int backId) {
		this.backId = backId;
	}

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ProducePlan getPlan() {
		return plan;
	}

	public void setPlan(ProducePlan plan) {
		this.plan = plan;
	}

	public ProduceBom getBom() {
		return bom;
	}

	public void setBom(ProduceBom bom) {
		this.bom = bom;
	}

	public MaterialOutOrder getMaterialOutOrder() {
		return materialOutOrder;
	}

	public void setMaterialOutOrder(MaterialOutOrder materialOutOrder) {
		this.materialOutOrder = materialOutOrder;
	}

	public HalfOutOrder getHalfOutOrder() {
		return halfOutOrder;
	}

	public void setHalfOutOrder(HalfOutOrder halfOutOrder) {
		this.halfOutOrder = halfOutOrder;
	}

	public SystemUser getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(SystemUser receiveUser) {
		this.receiveUser = receiveUser;
	}

	public SystemUser getBackUser() {
		return backUser;
	}

	public void setBackUser(SystemUser backUser) {
		this.backUser = backUser;
	}

	public List<ProduceBackMaterial> getBackMaterials() {
		return backMaterials;
	}

	public void setBackMaterials(List<ProduceBackMaterial> backMaterials) {
		this.backMaterials = backMaterials;
	}

	public List<ProduceBackHalf> getBackHalfs() {
		return backHalfs;
	}

	public void setBackHalfs(List<ProduceBackHalf> backHalfs) {
		this.backHalfs = backHalfs;
	}

	@JSONField(serialize = false)
	public ReceiveStatusVo getReceiveStatusVo() {
		return ReceiveStatusVo.getInstance(status);
	}

}
