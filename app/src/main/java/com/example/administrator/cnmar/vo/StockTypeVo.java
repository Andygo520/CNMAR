package com.example.administrator.cnmar.vo;

public enum StockTypeVo {

	EMPTY(null, ""),
	/** 扫描二维码 */
	ZERO(0, "扫描二维码"),
	/** 输入数量 */
	ONE(1, "输入数量");

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
				return EMPTY;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
