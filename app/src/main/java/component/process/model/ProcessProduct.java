package component.process.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.model.ComStation;
import component.com.model.ComTeam;
import component.com.model.ComTool;

/** 成品工序 */
public class ProcessProduct implements Cloneable {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private int productId;
	@JSONField(ordinal = 3)
	private String name;
	@JSONField(serialize = false)
	private int seq;

	@JSONField(serialize = false)
	private List<ComStation> stations;
	@JSONField(ordinal = 5)
	private List<ComTool> tools;
	@JSONField(serialize = false)
	private List<ComTeam> teams;

	@JSONField(ordinal = 7)
	private ComStation station;
	@JSONField(ordinal = 8)
	private ComTeam team;
	@JSONField(ordinal = 9)
	private int successNum; // 合格品数量
	@JSONField(ordinal = 10)
	private int failureNum; // 不合格品数量
	@JSONField(ordinal = 11)
	private ProcessProduct prev; // 上工序
	@JSONField(ordinal = 12)
	private ProcessProduct last; // 末工序

	public ProcessProduct() {

	}

	public ProcessProduct(int productId, String name, int seq) {
		this.productId = productId;
		this.name = name;
		this.seq = seq;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public List<ComStation> getStations() {
		return stations;
	}

	public void setStations(List<ComStation> stations) {
		this.stations = stations;
	}

	public List<ComTool> getTools() {
		return tools;
	}

	public void setTools(List<ComTool> tools) {
		this.tools = tools;
	}

	public List<ComTeam> getTeams() {
		return teams;
	}

	public void setTeams(List<ComTeam> teams) {
		this.teams = teams;
	}

	public ComStation getStation() {
		return station;
	}

	public void setStation(ComStation station) {
		this.station = station;
	}

	public ComTeam getTeam() {
		return team;
	}

	public void setTeam(ComTeam team) {
		this.team = team;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public int getFailureNum() {
		return failureNum;
	}

	public void setFailureNum(int failureNum) {
		this.failureNum = failureNum;
	}

	public ProcessProduct getPrev() {
		return prev;
	}

	public void setPrev(ProcessProduct prev) {
		this.prev = prev;
	}

	public ProcessProduct getLast() {
		return last;
	}

	public void setLast(ProcessProduct last) {
		this.last = last;
	}

	public ProcessProduct clone() throws CloneNotSupportedException {
		return (ProcessProduct) super.clone();
	}

}
