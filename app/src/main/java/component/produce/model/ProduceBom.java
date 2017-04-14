package component.produce.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.model.ComBox;
import component.half.model.Half;
import component.material.model.Material;
import component.process.model.ProcessHalf;
import component.produce.vo.ProduceStatusVo;

/** 子加工单 */
public class ProduceBom implements Cloneable {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 3)
	private int planId; // 加工单id
	@JSONField(ordinal = 4)
	private int pid; // 上级子加工单id
	@JSONField(ordinal = 5)
	private int materialId; // 原料id
	@JSONField(ordinal = 6)
	private int halfId; // 半成品id
	@JSONField(ordinal = 7)
	private int receiveNum; // 领料数量/计划生产数量
	@JSONField(ordinal = 8)
	private int level; // 层级
	@JSONField(ordinal = 9)
	private String code; // 子加工单编号
	@JSONField(ordinal = 10)
	private int status; // 状态

	@JSONField(ordinal = 11)
	private ProducePlan plan;
	@JSONField(ordinal = 12)
	private Material material;
	@JSONField(ordinal = 13)
	private Half half;

	@JSONField(ordinal = 14)
	private ProduceReceive receive;
	@JSONField(serialize = false)
	private List<ProduceBom> subs;
	@JSONField(ordinal = 16)
	private List<ProduceBom> materialSubs;
	@JSONField(ordinal = 17)
	private List<ProduceBom> halfSubs;
	@JSONField(ordinal = 18)
	private List<ProduceBomBatch> batchs;
	@JSONField(ordinal = 19)
	private ProduceBomBatch batchGroup;

	@JSONField(ordinal = 20)
	private ProcessHalf processHalf;
	@JSONField(ordinal = 21)
	private List<ComBox> lastProcessBoxs;

	public ProduceBom() {

	}

	public ProduceBom(int planId, int materialId, int halfId, int receiveNum, int level, int status) {
		this.planId = planId;
		this.materialId = materialId;
		this.halfId = halfId;
		this.receiveNum = receiveNum;
		this.level = level;
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

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
	}

	public int getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(int receiveNum) {
		this.receiveNum = receiveNum;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Half getHalf() {
		return half;
	}

	public void setHalf(Half half) {
		this.half = half;
	}

	public ProduceReceive getReceive() {
		return receive;
	}

	public void setReceive(ProduceReceive receive) {
		this.receive = receive;
	}

	public List<ProduceBom> getSubs() {
		return subs;
	}

	public void setSubs(List<ProduceBom> subs) {
		this.subs = subs;
	}

	public List<ProduceBom> getMaterialSubs() {
		return materialSubs;
	}

	public void setMaterialSubs(List<ProduceBom> materialSubs) {
		this.materialSubs = materialSubs;
	}

	public List<ProduceBom> getHalfSubs() {
		return halfSubs;
	}

	public void setHalfSubs(List<ProduceBom> halfSubs) {
		this.halfSubs = halfSubs;
	}

	public List<ProduceBomBatch> getBatchs() {
		return batchs;
	}

	public void setBatchs(List<ProduceBomBatch> batchs) {
		this.batchs = batchs;
	}

	public ProduceBomBatch getBatchGroup() {
		return batchGroup;
	}

	public void setBatchGroup(ProduceBomBatch batchGroup) {
		this.batchGroup = batchGroup;
	}

	public ProcessHalf getProcessHalf() {
		return processHalf;
	}

	public void setProcessHalf(ProcessHalf processHalf) {
		this.processHalf = processHalf;
	}

	public List<ComBox> getLastProcessBoxs() {
		return lastProcessBoxs;
	}

	public void setLastProcessBoxs(List<ComBox> lastProcessBoxs) {
		this.lastProcessBoxs = lastProcessBoxs;
	}

	@JSONField(serialize = false)
	public ProduceStatusVo getProduceStatusVo() {
		return ProduceStatusVo.getInstance(status);
	}

	public ProduceBom clone() throws CloneNotSupportedException {
		ProduceBom clone = (ProduceBom) super.clone();
		if (clone.processHalf != null) {
			clone.processHalf = processHalf.clone();
		}
		return clone;
	}

}
