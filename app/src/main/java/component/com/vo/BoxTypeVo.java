package component.com.vo;

/** 料框类型 */
public enum BoxTypeVo {

	empty(null, ""), success(1, "合格品"), failure(2, "不合格品");

	private Integer key;
	private String value;

	private BoxTypeVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final BoxTypeVo getInstance(Integer key) {
		for (BoxTypeVo vo : values()) {
			if (key == null) {
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
