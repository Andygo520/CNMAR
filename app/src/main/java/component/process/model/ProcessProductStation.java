package component.process.model;

/** 成品工序-机台工位 */
public class ProcessProductStation {

	private int id;
	private int processId;
	private int stationId;

	public ProcessProductStation() {

	}

	public ProcessProductStation(int processId, int stationId) {
		this.processId = processId;
		this.stationId = stationId;
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

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

}
