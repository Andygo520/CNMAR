package component.system.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import component.common.model.BaseModel;

/** 角色 */
public class SystemRole extends BaseModel {

	@JSONField(ordinal = 1)
	private String name; // 角色名称

	@JSONField(serialize = false)
	private boolean checked;

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

}
