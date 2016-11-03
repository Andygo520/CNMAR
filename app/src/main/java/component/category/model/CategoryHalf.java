package component.category.model;

public class CategoryHalf {

	private int id;
	private int halfId; // 半成品id
	private int nameId; // 表头id
	private int optionId; // 选项id
	private String content; // 输入项内容

	private CategoryOption option;

	public CategoryHalf() {

	}

	public CategoryHalf(int halfId, int nameId, int optionId, String content) {
		this.halfId = halfId;
		this.nameId = nameId;
		this.optionId = optionId;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHalfId() {
		return halfId;
	}

	public void setHalfId(int halfId) {
		this.halfId = halfId;
	}

	public int getNameId() {
		return nameId;
	}

	public void setNameId(int nameId) {
		this.nameId = nameId;
	}

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CategoryOption getOption() {
		return option;
	}

	public void setOption(CategoryOption option) {
		this.option = option;
	}

}
