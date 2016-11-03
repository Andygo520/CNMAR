package component.system.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.ImgModel;
import component.common.vo.IsEnableVo;
import component.system.vo.GenderVo;

/** 用户 */
public class SystemUser extends ImgModel {

	@JSONField(ordinal = 1)
	private String username; // 账号
	@JSONField(ordinal = 2)
	private String password; // 密码
	@JSONField(ordinal = 3)
	private String name; // 姓名
	@JSONField(ordinal = 4)
	private String gender; // 性别 - M男F女
	@JSONField(ordinal = 5, format = "yyyy-MM-dd")
	private Date birthday; // 出生日期
	@JSONField(ordinal = 6)
	private Boolean isSuper; // 是否超级管理员 - 0否1是
	@JSONField(ordinal = 7)
	private Boolean isEnable; // 状态 - 0禁用1启用

	@JSONField(ordinal = 8)
	private List<SystemRole> roles;
	@JSONField(ordinal = 9)
	private List<SystemMenu> menus;

	public SystemUser() {

	}

	public SystemUser(String username, String password, String name, String gender, Date birthday, Boolean isSuper, Boolean isEnable, int cid, Date ctime) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.isSuper = isSuper;
		this.isEnable = isEnable;
		this.cid = cid;
		this.ctime = ctime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Boolean getIsSuper() {
		return isSuper;
	}

	public void setIsSuper(Boolean isSuper) {
		this.isSuper = isSuper;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public List<SystemRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SystemRole> roles) {
		this.roles = roles;
	}

	public List<SystemMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<SystemMenu> menus) {
		this.menus = menus;
	}

	@JSONField(serialize = false)
	public GenderVo getGenderVo() {
		return GenderVo.getInstance(gender);
	}

	@JSONField(serialize = false)
	public IsEnableVo getIsEnableVo() {
		return IsEnableVo.getInstance(isEnable);
	}

}
