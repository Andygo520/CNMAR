package component.produce.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.model.ComStation;
import component.system.model.SystemUser;

/** 生产检验流水 */
public class ProduceTest {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int receiveId; // 领料单id
	@JSONField(ordinal = 3)
	private int processId; // 工序id
	@JSONField(ordinal = 4)
	private int stationId; // 机台工位id
	@JSONField(ordinal = 5)
	private int testNum; // 检验数量
	@JSONField(ordinal = 6)
	private int failNum; // 不合格品数量
	@JSONField(ordinal = 7)
	private String reason; // 不合格原因
	@JSONField(ordinal = 8)
	private int testId; // 检验员id
	@JSONField(ordinal = 9, format = "yyyy-MM-dd HH:mm:ss")
	private Date testTime; // 检验时间

	@JSONField(ordinal = 10)
	private ProduceReceive receive;
	@JSONField(ordinal = 11)
	private ComStation station;
	@JSONField(ordinal = 12)
	private SystemUser test;

	public ProduceTest() {

	}

	public ProduceTest(int receiveId, int processId, int stationId, int testNum, int failNum, String reason, int testId, Date testTime) {
		this.receiveId = receiveId;
		this.processId = processId;
		this.stationId = stationId;
		this.testNum = testNum;
		this.failNum = failNum;
		this.reason = reason;
		this.testId = testId;
		this.testTime = testTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public ProduceReceive getReceive() {
		return receive;
	}

	public void setReceive(ProduceReceive receive) {
		this.receive = receive;
	}

	public ComStation getStation() {
		return station;
	}

	public void setStation(ComStation station) {
		this.station = station;
	}

	public SystemUser getTest() {
		return test;
	}

	public void setTest(SystemUser test) {
		this.test = test;
	}

}
