package component.process.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.model.ComTeam;
import component.com.model.ComTool;

/** 成品工序 */
public class ProcessProduct {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private int productId;
	@JSONField(ordinal = 3)
	private String name;
	@JSONField(serialize = false)
	private int stationId;
	@JSONField(serialize = false)
	private int seq;

	@JSONField(serialize = false)
	private List<ComTool> tools;
	@JSONField(serialize = false)
	private List<ComTeam> teams;

	public ProcessProduct() {

	}

	public ProcessProduct(int productId, String name, int stationId, int seq) {
		this.productId = productId;
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

}
