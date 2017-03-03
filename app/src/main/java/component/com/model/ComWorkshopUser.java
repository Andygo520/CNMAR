package component.com.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 车间检验员关系 */
public class ComWorkshopUser {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int workshopId; // 车间id
	@JSONField(ordinal = 3)
	private int userId; // 用户id

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
