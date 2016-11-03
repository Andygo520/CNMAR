package component.basic.vo;

public enum MixTypeVo {

	empty(null, ""),
	/** 不允许 */
	no(1, "不允许"),
	/** 允许 */
	yes(2, "允许");

	private Integer key;
	private String value;

	private MixTypeVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final MixTypeVo getInstance(Integer key) {
		for (MixTypeVo vo : values()) {
			if (key == null) {
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
