package component.produce.vo;

import component.produce.vo.ProduceStatusVo;

/** 生产状态 */
public enum ProduceStatusVo {

	empty(null, ""),
	/** 未生产 */
	no(1, "未生产"),
	/** 已生产 */
	yes(2, "已生产");

	private Integer key;
	private String value;

	private ProduceStatusVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final ProduceStatusVo getInstance(Integer key) {
		for (ProduceStatusVo vo : values()) {
			if (key == null) {
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
