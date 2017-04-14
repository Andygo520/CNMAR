package component.com.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.vo.ToolTypeVo;

/** 工装 */
public class ComTool {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private String name; // 名称
	@JSONField(ordinal = 3)
	private int type; // 类型
	@JSONField(serialize = false)
	private Boolean isDel;

	@JSONField(serialize = false)
	private boolean checked;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public ToolTypeVo getToolTypeVo() {
		return ToolTypeVo.getInstance(type);
	}

}
