package component.category.model;

import java.util.List;

public class Category {

	private int id;
	private String name; // BOM属性名称
	private int seq; // 顺序号

	private List<CategoryName> names;

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

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public List<CategoryName> getNames() {
		return names;
	}

	public void setNames(List<CategoryName> names) {
		this.names = names;
	}

}
