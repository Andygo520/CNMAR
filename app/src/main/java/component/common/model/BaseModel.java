package component.common.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class BaseModel {

	@JSONField(ordinal = 0)
	protected int id;

	@JSONField(serialize = false)
	protected int cid; // 创建人id
	@JSONField(serialize = false)
	protected Date ctime;// 创建时间
	@JSONField(serialize = false)
	protected int uid;// 更新人id
	@JSONField(serialize = false)
	protected Date utime;// 更新时间
	@JSONField(serialize = false)
	protected Boolean isDel;// 是否删除 - 0否1是

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

}
