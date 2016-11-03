package component.common.model;

import com.alibaba.fastjson.annotation.JSONField;

public class TreeModel extends BaseModel {

	@JSONField(ordinal = 81)
	protected int pid; // 上级id
	@JSONField(ordinal = 82)
	protected int seq; // 顺序号
	@JSONField(ordinal = 83)
	protected Boolean isLeaf;// 叶子节点 - 0否1是

	@JSONField(serialize = false)
	protected int level;
	@JSONField(serialize = false)
	protected boolean first;
	@JSONField(serialize = false)
	protected boolean last;
	@JSONField(serialize = false)
	protected boolean disabled;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
