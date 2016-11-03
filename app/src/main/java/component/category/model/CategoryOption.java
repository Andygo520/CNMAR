package component.category.model;

import com.alibaba.fastjson.annotation.JSONField;

public class CategoryOption {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int nameId; // 表头id
	@JSONField(ordinal = 3)
	private String content; // 选项内容
	@JSONField(ordinal = 4)
	private int seq; // 顺序号

	public CategoryOption() {

	}

	public CategoryOption(int nameId, String content, int seq) {
		this.nameId = nameId;
		this.content = content;
		this.seq = seq;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNameId() {
		return nameId;
	}

	public void setNameId(int nameId) {
		this.nameId = nameId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
