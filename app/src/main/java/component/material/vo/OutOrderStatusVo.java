package component.material.vo;

/** 原料出库单状态 */
public enum OutOrderStatusVo {

	empty(null, ""),
	/** 待出库 */
	pre_out_stock(2, "待出库"),
	/** 已出库 */
	out_stock(3, "已出库"),
	/** 部分出库 */
	not_all(4, "部分出库");

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
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
