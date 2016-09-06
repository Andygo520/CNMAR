package com.example.administrator.cnmar.model;

public enum MixTypeVo {

	EMPTY(null, ""),
	/** 不允许 */
	ZERO(0, "不允许"),
	/** 允许 */
	ONE(1, "允许");

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
				return EMPTY;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
