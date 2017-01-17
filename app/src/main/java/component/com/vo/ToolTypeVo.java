package component.com.vo;

/** 工装类型 */
public enum ToolTypeVo {

	empty(null, ""),
	/** 模具 */
	mould(1, "模具"),
	/** 刀具 */
	knife(2, "刀具"),
	/** 夹具/ */
	fixture(3, "夹具"),
	/** 量具 */
	measure(4, "量具"),
	/** 检具 */
	check(5, "检具"),
	/** 工位器具 */
	station(6, "工位器具");

	private Integer key;
	private String value;

	private ToolTypeVo(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final ToolTypeVo getInstance(Integer key) {
		for (ToolTypeVo vo : values()) {
			if (key == null) {
				return empty;
			} else if (key == vo.getKey()) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
