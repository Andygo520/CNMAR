package component.system.model;

/** 菜单权限关系 */
public class SystemMenuAuth {

	private int id;
	private int menuId; // 菜单id
	private int authId; // 权限id

	public SystemMenuAuth() {

	}

	public SystemMenuAuth(int menuId, int authId) {
		this.menuId = menuId;
		this.authId = authId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getAuthId() {
		return authId;
	}

	public void setAuthId(int authId) {
		this.authId = authId;
	}

}
