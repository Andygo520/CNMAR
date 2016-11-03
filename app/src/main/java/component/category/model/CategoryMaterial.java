package component.category.model;

public class CategoryMaterial {

	private int id;
	private int materialId; // 原料id
	private int nameId; // 表头id
	private int optionId; // 选项id
	private String content; // 输入项内容

	private CategoryOption option;

	public CategoryMaterial() {

	}

	public CategoryMaterial(int materialId, int nameId, int optionId, String content) {
		this.materialId = materialId;
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

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
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
