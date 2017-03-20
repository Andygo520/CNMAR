package component.process.model;

/** 半成品工序-机台工位 */
public class ProcessHalfStation {

	private int id;
	private int processId;
	private int stationId;

	public ProcessHalfStation() {

	}

	public ProcessHalfStation(int processId, int stationId) {
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
