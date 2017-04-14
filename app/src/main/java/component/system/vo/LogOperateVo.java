package component.system.vo;

import org.apache.commons.lang.StringUtils;

public enum LogOperateVo {

	empty(null, ""),
	/** 登录 */
	login("login", "登录"),
	/** 注销 */
	logout("logout", "注销"),
	/** 新增 */
	add("add", "新增"),
	/** 编辑 */
	edit("edit", "编辑"),
	/** 删除 */
	delete("delete", "删除"),
	/** APP登录 */
	app_login("app_login", "APP登录");

	private String key;
	private String value;

	private LogOperateVo(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final LogOperateVo getInstance(String key) {
		for (LogOperateVo vo : values()) {
			if (StringUtils.isEmpty(key)) {
				return empty;
			} else if (key.equals(vo.getKey())) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
