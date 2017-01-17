package component.produce.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.half.model.Half;
import component.half.model.HalfInOrder;
import component.material.model.Material;
import component.system.model.SystemUser;

/** 子加工单 */
public class ProduceBom {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int halfInOrderId; // 半成品入库单id
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
	private int actualNum; // 实际生产数量
	@JSONField(ordinal = 11)
	private int actualId; // 统计员id
	@JSONField(ordinal = 12)
	private int successNum; // 合格品数量
	@JSONField(ordinal = 13)
	private int testId; // 检验员id
	@JSONField(ordinal = 15)
	private Date testTime; // 检验时间

	@JSONField(ordinal = 15)
	private ProducePlan plan;
	@JSONField(ordinal = 16)
	private SystemUser test;
	@JSONField(ordinal = 17)
	private Material material;
	@JSONField(ordinal = 18)
	private Half half;

	@JSONField(ordinal = 19)
	private ProduceReceive receive;
	@JSONField(ordinal = 20)
	private HalfInOrder halfInOrder;
	@JSONField(serialize = false)
	private List<ProduceBom> subs;
	@JSONField(ordinal = 21)
	private List<ProduceBom> materialSubs;
	@JSONField(ordinal = 22)
	private List<ProduceBom> halfSubs;

	public ProduceBom() {

	}

	public ProduceBom(int planId, int materialId, int halfId, int receiveNum, int level) {
		this.planId = planId;
		this.materialId = materialId;
		this.halfId = halfId;
		this.receiveNum = receiveNum;
		this.level = level;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHalfInOrderId() {
		return halfInOrderId;
	}

	public void setHalfInOrderId(int halfInOrderId) {
		this.halfInOrderId = halfInOrderId;
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

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public int getActualId() {
		return actualId;
	}

	public void setActualId(int actualId) {
		this.actualId = actualId;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
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

	public ProducePlan getPlan() {
		return plan;
	}

	public void setPlan(ProducePlan plan) {
		this.plan = plan;
	}

	public SystemUser getTest() {
		return test;
	}

	public void setTest(SystemUser test) {
		this.test = test;
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

	public HalfInOrder getHalfInOrder() {
		return halfInOrder;
	}

	public void setHalfInOrder(HalfInOrder halfInOrder) {
		this.halfInOrder = halfInOrder;
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

}
