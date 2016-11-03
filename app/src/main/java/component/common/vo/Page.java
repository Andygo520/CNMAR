package component.common.vo;

import com.alibaba.fastjson.annotation.JSONField;

/** 分页 */
public class Page {

	@JSONField(serialize = false)
	private int per = 10; // 每页多少条数据
	@JSONField(ordinal = 2)
	private int num = 1; // 第几页
	@JSONField(ordinal = 3)
	private int total; // 总页数
	@JSONField(serialize = false)
	private int start; // 起始行数
	@JSONField(ordinal = 5)
	private int count; // 数据总条数

	public int getPer() {
		return per;
	}

	public void setPer(int per) {
		this.per = per;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		if (count > 0) {
			total = (count - 1) / per + 1;
			if (num < 1) {
				num = 1;
			} else if (num > total) {
				num = total;
			}
			start = (num - 1) * per;
		}
	}

}
