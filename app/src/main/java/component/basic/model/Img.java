package component.basic.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/** 图片 */
public class Img {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private String module; // 模块
	@JSONField(serialize = false)
	private int dataId; // 数据id
	@JSONField(ordinal = 4)
	private String path; // 相对路径
	@JSONField(serialize = false)
	private String type; // 文件类型
	@JSONField(serialize = false)
	private long num; // 文件大小
	@JSONField(serialize = false)
	private int cid;
	@JSONField(serialize = false)
	private Date ctime;
	@JSONField(serialize = false)
	private Boolean isDel;

	public Img() {

	}

	public Img(String module, int dataId, String path, String type, long num, int cid, Date ctime) {
		this.module = module;
		this.dataId = dataId;
		this.path = path;
		this.type = type;
		this.num = num;
		this.cid = cid;
		this.ctime = ctime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public int getDataId() {
		return dataId;
	}

	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

}
