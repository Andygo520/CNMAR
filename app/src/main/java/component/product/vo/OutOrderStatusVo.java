package component.product.vo;

/** 成品出库单状态 */
public enum OutOrderStatusVo {

	EMPTY(null, ""),
	/** 待出库 */
	PRE_OUT_STOCK(2, "待出库"),
	/** 已入库 */
	OUT_STOCK(3, "已出库");

	private Integer key;
	private String value;

	private OutOrderStatusVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final OutOrderStatusVo getInstance(Integer key) {
		for (OutOrderStatusVo vo : values()) {
			if (key == null) {
				return EMPTY;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
