package component.com.model;

/** 车间检验员关系 */
public class ComWorkshopUser {

	private int id;
	private int workshopId;
	private int userId;

	public ComWorkshopUser() {

	}

	public ComWorkshopUser(int workshopId, int userId) {
		this.workshopId = workshopId;
		this.userId = userId;
	}

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
