package component.produce.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import component.system.model.SystemUser;

/** 生产检验流水 */
public class ProduceTest {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int planId; // 加工单id
	@JSONField(ordinal = 3)
	private int bomId; // 子加工单id
	@JSONField(ordinal = 4)
	private int processId; // 工序id
	@JSONField(ordinal = 5)
	private int stationId; // 机台工位id
	@JSONField(ordinal = 6)
	private int testNum; // 检验数量
	@JSONField(ordinal = 7)
	private int failNum; // 不合格品数量
	@JSONField(ordinal = 8)
	private String testRemark; // 检验备注
	@JSONField(ordinal = 9)
	private int testId; // 检验员id
	@JSONField(ordinal = 10, format = "yyyy-MM-dd HH:mm:ss")
	private Date testTime; // 检验时间

	@JSONField(ordinal = 11)
	private ProducePlan plan;
	@JSONField(ordinal = 12)
	private ProduceBom bom;
	@JSONField(ordinal = 14)
	private SystemUser test;

	public ProduceTest() {

	}

	public ProduceTest(int planId, int bomId, int processId, int stationId, int testNum, int failNum, String testRemark, int testId, Date testTime) {
		this.planId = planId;
		this.bomId = bomId;
		this.processId = processId;
		this.stationId = stationId;
		this.testNum = testNum;
		this.failNum = failNum;
		this.testRemark = testRemark;
		this.testId = testId;
		this.testTime = testTime;
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

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getTestNum() {
		return testNum;
	}

	public void setTestNum(int testNum) {
		this.testNum = testNum;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public String getTestRemark() {
		return testRemark;
	}

	public void setTestRemark(String testRemark) {
		this.testRemark = testRemark;
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

	public ProduceBom getBom() {
		return bom;
	}

	public void setBom(ProduceBom bom) {
		this.bom = bom;
	}

	public SystemUser getTest() {
		return test;
	}

	public void setTest(SystemUser test) {
		this.test = test;
	}

}
