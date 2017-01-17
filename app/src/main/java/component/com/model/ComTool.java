package component.com.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.vo.ToolTypeVo;

/** 工装 */
@SuppressWarnings("serial")
public class ComTool implements Serializable {

	private int id;
	private String name;
	private int type;
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
