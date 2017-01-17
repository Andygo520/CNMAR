package component.category.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.category.vo.NameTypeVo;

public class CategoryName {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private int categoryId; // BOM属性id
	@JSONField(ordinal = 3)
	private String name; // 表头名称
	@JSONField(ordinal = 4)
	private int type; // 类型 - 1单选2输入项
	@JSONField(ordinal = 5)
	private int seq; // 顺序号

	@JSONField(ordinal = 6)
	private List<CategoryOption> options;
	@JSONField(serialize = false)
	private CategoryMaterial categoryMaterial;
	@JSONField(serialize = false)
	private CategoryProduct categoryProduct;
	@JSONField(serialize = false)
	private CategoryHalf categoryHalf;

	public CategoryName() {

	}

	public CategoryName(int categoryId, String name, int type, int seq) {
		this.categoryId = categoryId;
		this.name = name;
		this.type = type;
		this.seq = seq;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public List<CategoryOption> getOptions() {
		return options;
	}

	public void setOptions(List<CategoryOption> options) {
		this.options = options;
	}

	public CategoryMaterial getCategoryMaterial() {
		return categoryMaterial;
	}

	public void setCategoryMaterial(CategoryMaterial categoryMaterial) {
		this.categoryMaterial = categoryMaterial;
	}

	public CategoryProduct getCategoryProduct() {
		return categoryProduct;
	}

	public void setCategoryProduct(CategoryProduct categoryProduct) {
		this.categoryProduct = categoryProduct;
	}

	public CategoryHalf getCategoryHalf() {
		return categoryHalf;
	}

	public void setCategoryHalf(CategoryHalf categoryHalf) {
		this.categoryHalf = categoryHalf;
	}

	@JSONField(serialize = false)
	public NameTypeVo getNameTypeVo() {
		return NameTypeVo.getInstance(type);
	}

}
