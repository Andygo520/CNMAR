package component.produce.vo;

/** 领料单状态 */
public enum ReceiveStatusVo {

	empty(null, ""),
	/** 未领料 */
	no(1, "未领料"),
	/** 已领料 */
	yes(2, "已领料");

	private Integer key;
	private String value;

	private ReceiveStatusVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final ReceiveStatusVo getInstance(Integer key) {
		for (ReceiveStatusVo vo : values()) {
			if (key == null) {
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
