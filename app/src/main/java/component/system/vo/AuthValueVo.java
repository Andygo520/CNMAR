package component.system.vo;

public enum AuthValueVo {

	/** 查询 */
	list("list", "查询"),
	/** 新增 */
	add("add", "新增"),
	/** 编辑 */
	edit("edit", "编辑"),
	/** 删除 */
	delete("delete", "删除"),
	/** 其它 */
	other("other", "其它");

	private String key;
	private String value;

	private AuthValueVo(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final AuthValueVo getInstance(String key) {
		for (AuthValueVo vo : values()) {
			if (vo.getValue().equals(key)) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
