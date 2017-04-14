package component.process.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.model.ComStation;
import component.com.model.ComTeam;
import component.com.model.ComTool;

/** 半成品工序 */
public class ProcessHalf implements Cloneable {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private int halfId;
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
	private ProcessHalf prev; // 上工序
	@JSONField(ordinal = 12)
	private ProcessHalf last; // 末工序

	public ProcessHalf() {

	}

	public ProcessHalf(int halfId, String name, int seq) {
		this.halfId = halfId;
		this.name = name;
		this.seq = seq;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
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

	public ProcessHalf getPrev() {
		return prev;
	}

	public void setPrev(ProcessHalf prev) {
		this.prev = prev;
	}

	public ProcessHalf getLast() {
		return last;
	}

	public void setLast(ProcessHalf last) {
		this.last = last;
	}

	public ProcessHalf clone() throws CloneNotSupportedException {
		return (ProcessHalf) super.clone();
	}

}
