package component.system.model;

import com.alibaba.fastjson.annotation.JSONField;
import component.common.model.BaseModel;

/** 岗位 */
public class SystemJob extends BaseModel {

	@JSONField(ordinal = 1)
	private int deptId;
	@JSONField(ordinal = 2)
	private String name;
	@JSONField(ordinal = 3)
	private int seq;

	@JSONField(ordinal = 4)
	private SystemDept dept;

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public SystemDept getDept() {
		return dept;
	}

	public void setDept(SystemDept dept) {
		this.dept = dept;
	}

}
