package component.com.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 班组用户关系 */
public class ComTeamUser {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int teamId; // 班组id
	@JSONField(ordinal = 3)
	private int userId; // 用户id

	public ComTeamUser() {

	}

	public ComTeamUser(int teamId, int userId) {
		this.teamId = teamId;
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
