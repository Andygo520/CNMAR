package component.system.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;

/** 角色 */
public class SystemRole extends BaseModel {

	@JSONField(ordinal = 1)
	private String name; // 角色名称

	@JSONField(serialize = false)
	private boolean checked;
	@JSONField(serialize = false)
	private List<SystemAuth> auths;

	public SystemRole() {

	}

	public SystemRole(String name, int cid, Date ctime) {
		this.name = name;
		this.cid = cid;
		this.ctime = ctime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<SystemAuth> getAuths() {
		return auths;
	}

	public void setAuths(List<SystemAuth> auths) {
		this.auths = auths;
	}

}
