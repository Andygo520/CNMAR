package component.system.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

import component.common.model.ImgModel;
import component.common.vo.IsEnableVo;
import component.system.vo.GenderVo;

/** 用户 */
public class SystemUser extends ImgModel {

	@JSONField(ordinal = 1)
	private int deptId;
	@JSONField(ordinal = 2)
	private int jobId;
	@JSONField(ordinal = 3)
	private int dutyId;
	@JSONField(ordinal = 4)
	private String username; // 账号
	@JSONField(ordinal = 5)
	private String password; // 密码
	@JSONField(ordinal = 6)
	private String name; // 姓名
	@JSONField(ordinal = 7)
	private String gender; // 性别 - M男F女
	@JSONField(ordinal = 8, format = "yyyy-MM-dd")
	private Date birthday; // 出生日期
	@JSONField(ordinal = 9)
	private String card;
	@JSONField(ordinal = 10)
	private String phone;
	@JSONField(ordinal = 11)
	private String qq;
	@JSONField(ordinal = 12)
	private String address;
	@JSONField(ordinal = 13)
	private Boolean isSuper; // 是否超级管理员 - 0否1是
	@JSONField(ordinal = 14)
	private Boolean isEnable; // 状态 - 0禁用1启用

	@JSONField(ordinal = 15)
	private List<SystemRole> roles;
	@JSONField(ordinal = 16)
	private List<SystemMenu> menus;
	@JSONField(ordinal = 17)
	private SystemDept dept;
	@JSONField(ordinal = 18)
	private SystemJob job;
	@JSONField(ordinal = 19)
	private SystemDuty duty;
	@JSONField(ordinal = 20)
	private Boolean isOperator; // 是否操作工
	@JSONField(ordinal = 21)
	private Boolean isTest; // 是否检验员

	@JSONField(serialize = false)
	private boolean checked;

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

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getDutyId() {
		return dutyId;
	}

	public void setDutyId(int dutyId) {
		this.dutyId = dutyId;
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

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public SystemDept getDept() {
		return dept;
	}

	public void setDept(SystemDept dept) {
		this.dept = dept;
	}

	public SystemJob getJob() {
		return job;
	}

	public void setJob(SystemJob job) {
		this.job = job;
	}

	public SystemDuty getDuty() {
		return duty;
	}

	public void setDuty(SystemDuty duty) {
		this.duty = duty;
	}

	public Boolean getIsOperator() {
		return isOperator;
	}

	public void setIsOperator(Boolean isOperator) {
		this.isOperator = isOperator;
	}

	public Boolean getIsTest() {
		return isTest;
	}

	public void setIsTest(Boolean isTest) {
		this.isTest = isTest;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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
