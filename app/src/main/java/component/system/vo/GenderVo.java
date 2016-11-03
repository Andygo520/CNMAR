package component.system.vo;

import org.apache.commons.lang.StringUtils;

public enum GenderVo {

	empty(null, ""),
	/** 男 */
	M("M", "男"),
	/** 女 */
	F("F", "女");

	private String key;
	private String value;

	private GenderVo(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final GenderVo getInstance(String key) {
		for (GenderVo vo : values()) {
			if (StringUtils.isEmpty(key)) {
				return empty;
			} else if (key.equals(vo.getKey())) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
