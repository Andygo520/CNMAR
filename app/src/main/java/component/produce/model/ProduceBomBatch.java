package component.produce.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import component.half.model.HalfInOrder;
import component.system.model.SystemUser;

/** 子加工单分批入库 */
public class ProduceBomBatch {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int bomId; // 子加工单id
	@JSONField(ordinal = 3)
	private int halfInOrderId; // 半成品入库单id
	@JSONField(ordinal = 4)
	private int actualNum; // 实际生产数量
	@JSONField(ordinal = 5)
	private int actualId; // 统计员id
	@JSONField(ordinal = 6)
	private int successNum; // 合格品数量
	@JSONField(ordinal = 7)
	private int testId; // 检验员id
	@JSONField(ordinal = 8, format = "yyyy-MM-dd HH:mm:ss")
	private Date testTime; // 检验时间
	@JSONField(ordinal = 8)
	private String testRemark; // 检验备注

	@JSONField(ordinal = 9)
	private ProduceBom bom;
	@JSONField(ordinal = 10)
	private HalfInOrder halfInOrder;
	@JSONField(ordinal = 11)
	private SystemUser actual;
	@JSONField(ordinal = 12)
	private SystemUser test;

	public ProduceBomBatch(int bomId, int actualNum, int actualId) {
		this.bomId = bomId;
		this.actualNum = actualNum;
		this.actualId = actualId;
	}

	public ProduceBomBatch() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBomId() {
		return bomId;
	}

	public void setBomId(int bomId) {
		this.bomId = bomId;
	}

	public int getHalfInOrderId() {
		return halfInOrderId;
	}

	public void setHalfInOrderId(int halfInOrderId) {
		this.halfInOrderId = halfInOrderId;
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

	public String getTestRemark() {
		return testRemark;
	}

	public void setTestRemark(String testRemark) {
		this.testRemark = testRemark;
	}

	public ProduceBom getBom() {
		return bom;
	}

	public void setBom(ProduceBom bom) {
		this.bom = bom;
	}

	public HalfInOrder getHalfInOrder() {
		return halfInOrder;
	}

	public void setHalfInOrder(HalfInOrder halfInOrder) {
		this.halfInOrder = halfInOrder;
	}

	public SystemUser getActual() {
		return actual;
	}

	public void setActual(SystemUser actual) {
		this.actual = actual;
	}

	public SystemUser getTest() {
		return test;
	}

	public void setTest(SystemUser test) {
		this.test = test;
	}

}
