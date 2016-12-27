package component.product.vo;

/** 成品入库单状态 */
public enum InOrderStatusVo {

	empty(null, ""),
	/** 待打印 */
	pre_print(1, "待打印"),
	/** 待入库 */
	pre_in_stock(2, "待入库"),
	/** 已入库 */
	in_stock(3, "已入库"),
	/** 部分入库 */
	not_all(4, "部分入库");

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
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
