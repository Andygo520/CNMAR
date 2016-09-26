package component.system.model;

/** 用户角色关系 */
public class SystemUserRole {

	private int id;
	private int userId; // 用户id
	private int roleId; // 角色id

	public SystemUserRole() {

	}

	public SystemUserRole(int userId, int roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
