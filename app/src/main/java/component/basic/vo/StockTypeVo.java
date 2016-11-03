package component.basic.vo;

public enum StockTypeVo {

	empty(null, ""),
	/** 扫描二维码 */
	scan(1, "扫描二维码"),
	/** 输入数量 */
	input(2, "输入数量");

	private Integer key;
	private String value;

	private StockTypeVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final StockTypeVo getInstance(Integer key) {
		for (StockTypeVo vo : values()) {
			if (key == null) {
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
