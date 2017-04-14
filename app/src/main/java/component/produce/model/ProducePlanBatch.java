package component.produce.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import component.product.model.ProductInOrder;
import component.system.model.SystemUser;

/** 加工单分批入库 */
public class ProducePlanBatch {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int planId; // 加工单id
	@JSONField(ordinal = 3)
	private int productInOrderId; // 成品入库单id
	@JSONField(ordinal = 4)
	private int actualNum; // 实际生产数量
	@JSONField(ordinal = 5)
	private int actualId; // 统计员id
	@JSONField(ordinal = 6)
	private int successNum; // 合格品数量
	@JSONField(ordinal = 7)
	private int testId;
	@JSONField(ordinal = 8, format = "yyyy-MM-dd HH:mm:ss")
	private Date testTime;
	@JSONField(ordinal = 9)
	private String testRemark; // 检验备注

	@JSONField(ordinal = 10)
	private ProducePlan plan;
	@JSONField(ordinal = 11)
	private ProductInOrder productInOrder;
	@JSONField(ordinal = 12)
	private SystemUser actual;
	@JSONField(ordinal = 13)
	private SystemUser test;

	public ProducePlanBatch(int planId, int actualNum, int actualId) {
		this.planId = planId;
		this.actualNum = actualNum;
		this.actualId = actualId;
	}

	public ProducePlanBatch() {

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

	public int getProductInOrderId() {
		return productInOrderId;
	}

	public void setProductInOrderId(int productInOrderId) {
		this.productInOrderId = productInOrderId;
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

	public ProducePlan getPlan() {
		return plan;
	}

	public void setPlan(ProducePlan plan) {
		this.plan = plan;
	}

	public ProductInOrder getProductInOrder() {
		return productInOrder;
	}

	public void setProductInOrder(ProductInOrder productInOrder) {
		this.productInOrder = productInOrder;
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
