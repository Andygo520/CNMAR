package component.system.vo;

public enum AuthValueVo {

	/** 查询 */
	LIST("list", "查询"),
	/** 新增 */
	ADD("add", "新增"),
	/** 编辑 */
	EDIT("edit", "编辑"),
	/** 删除 */
	DELETE("delete", "删除"),
	/** 其它 */
	OTHER("other", "其它");

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
