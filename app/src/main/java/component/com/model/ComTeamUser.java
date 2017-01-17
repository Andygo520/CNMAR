package component.com.model;

/** 班组用户关系 */
public class ComTeamUser {

	private int id;
	private int teamId;
	private int userId;

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
