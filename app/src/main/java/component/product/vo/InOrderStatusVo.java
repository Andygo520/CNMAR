package component.product.vo;

/** 成品入库单状态 */
public enum InOrderStatusVo {

	EMPTY(null, ""),
	/** 待打印 */
	PRE_PRINT(1, "待打印"),
	/** 待入库 */
	PRE_IN_STOCK(2, "待入库"),
	/** 已入库 */
	IN_STOCK(3, "已入库");

	private Integer key;
	private String value;

	private InOrderStatusVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final InOrderStatusVo getInstance(Integer key) {
		for (InOrderStatusVo vo : values()) {
			if (key == null) {
				return EMPTY;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
