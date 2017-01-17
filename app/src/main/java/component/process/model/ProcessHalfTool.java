package component.process.model;

/** 半成品工序-工装 */
public class ProcessHalfTool {

	private int id;
	private int processId;
	private int toolId;

	public ProcessHalfTool() {

	}

	public ProcessHalfTool(int processId, int toolId) {
		this.processId = processId;
		this.toolId = toolId;
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

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

}
