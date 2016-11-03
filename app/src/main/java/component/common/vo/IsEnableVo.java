package component.common.vo;

public enum IsEnableVo {

	empty(null, ""),
	/** 启用 */
	T(true, "启用"),
	/** 禁用 */
	F(false, "禁用");

	private Boolean key;
	private String value;

	private IsEnableVo(Boolean key, String value) {
		this.key = key;
		this.value = value;
	}

	public Boolean getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final IsEnableVo getInstance(Boolean key) {
		for (IsEnableVo vo : values()) {
			if (vo.getKey() == key) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
