package component.process.model;

/** 半成品工序-班组 */
public class ProcessHalfTeam {

	private int id;
	private int processId;
	private int teamId;

	public ProcessHalfTeam() {

	}

	public ProcessHalfTeam(int processId, int teamId) {
		this.processId = processId;
		this.teamId = teamId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

}
