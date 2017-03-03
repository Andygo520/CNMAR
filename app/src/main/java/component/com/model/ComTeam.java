package component.com.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.system.model.SystemUser;

/** 班组 */
@SuppressWarnings("serial")
public class ComTeam implements Serializable {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private String name; // 名称
	@JSONField(serialize = false)
	private Boolean isDel;

	@JSONField(serialize = false)
	private List<SystemUser> users;

	@JSONField(serialize = false)
	private boolean checked;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

	public List<SystemUser> getUsers() {
		return users;
	}

	public void setUsers(List<SystemUser> users) {
		this.users = users;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
