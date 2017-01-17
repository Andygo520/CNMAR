package component.category.vo;

/** BOM表头类型 */
public enum NameTypeVo {

	empty(null, ""),
	/** 单选 */
	select(1, "单选"),
	/** 输入项 */
	input(2, "输入项");

	private Integer key;
	private String value;

	private NameTypeVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final NameTypeVo getInstance(Integer key) {
		for (NameTypeVo vo : values()) {
			if (key == null) {
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
