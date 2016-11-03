package component.basic.vo;

public enum PackTypeVo {

	empty(0, ""),
	/** 袋装 */
	bag(1, "袋装"),
	/** 装箱 */
	box(2, "装箱"),
	/** 捆包 */
	wrap(3, "捆包"),
	/** 捆扎 */
	bind(4, "捆扎"),
	/** 料框 */
	frame(5, "料框");

	private Integer key;
	private String value;

	private PackTypeVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final PackTypeVo getInstance(Integer key) {
		for (PackTypeVo vo : values()) {
			if (key == null) {
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
