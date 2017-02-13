package component.process.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.model.ComStation;
import component.com.model.ComTeam;
import component.com.model.ComTool;

/** 半成品工序 */
public class ProcessHalf {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private int halfId;
	@JSONField(ordinal = 3)
	private String name;
	@JSONField(serialize = false)
	private int stationId;
	@JSONField(serialize = false)
	private int seq;

	@JSONField(ordinal = 5)
	private List<ComTool> tools;
	@JSONField(serialize = false)
	private List<ComTeam> teams;

	@JSONField(ordinal = 7)
	private ComStation station;

	public ProcessHalf() {

	}

	public ProcessHalf(int halfId, String name, int stationId, int seq) {
		this.halfId = halfId;
		this.name = name;
		this.stationId = stationId;
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

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
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

}
