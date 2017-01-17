package component.com.model;

/** 机台工位 */
public class ComStation {

	private int id;
	private int workshopId;
	private String code;
	private String name;
	private String qrcode;
	private Boolean isDel;

	private ComWorkshop workshop;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWorkshopId() {
		return workshopId;
	}

	public void setWorkshopId(int workshopId) {
		this.workshopId = workshopId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

	public ComWorkshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(ComWorkshop workshop) {
		this.workshop = workshop;
	}

}
