package component.system.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.TreeModel;

/** 菜单 */
public class SystemMenu extends TreeModel {

	@JSONField(ordinal = 1)
	private String name; // 菜单名称
	@JSONField(ordinal = 2)
	private String url; // URL
	@JSONField(serialize = false)
	private String style; // CSS样式

	@JSONField(ordinal = 3)
	private List<SystemMenu> subList;
	@JSONField(serialize = false)
	private SystemMenu topMenu;
	@JSONField(serialize = false)
	private List<SystemAuth> auths;

	public SystemMenu() {

	}

	public SystemMenu(String name, String url, String style, int pid, int seq, Boolean isLeaf, int cid, Date ctime) {
		this.name = name;
		this.url = url;
		this.style = style;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public List<SystemMenu> getSubList() {
		return subList;
	}

	public void setSubList(List<SystemMenu> subList) {
		this.subList = subList;
	}

	public SystemMenu getTopMenu() {
		return topMenu;
	}

	public void setTopMenu(SystemMenu topMenu) {
		this.topMenu = topMenu;
	}

	public List<SystemAuth> getAuths() {
		return auths;
	}

	public void setAuths(List<SystemAuth> auths) {
		this.auths = auths;
	}

}
