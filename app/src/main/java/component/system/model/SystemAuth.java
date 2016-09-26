package component.system.model;

import java.util.Date;
import java.util.List;

import component.common.model.TreeModel;
import component.system.vo.AuthValueVo;

/** 权限 */
public class SystemAuth extends TreeModel {

	private String name; // 权限名称
	private String value; // 权限值

	private boolean checked;
	private List<SystemAuth> subList;

	public SystemAuth() {

	}

	public SystemAuth(String name, String value, int pid, int seq, Boolean isLeaf, int cid, Date ctime) {
		this.name = name;
		this.value = value;
		this.pid = pid;
		this.seq = seq;
		this.isLeaf = isLeaf;
		this.cid = cid;
		this.ctime = ctime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<SystemAuth> getSubList() {
		return subList;
	}

	public void setSubList(List<SystemAuth> subList) {
		this.subList = subList;
	}

	public AuthValueVo getAuthValueVo() {
		return AuthValueVo.getInstance(value);
	}

}
