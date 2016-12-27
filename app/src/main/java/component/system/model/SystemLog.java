package component.system.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import component.system.vo.LogModuleVo;
import component.system.vo.LogOperateVo;

public class SystemLog {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private String name;
	@JSONField(ordinal = 3)
	private String operate;
	@JSONField(ordinal = 4)
	private String module;
	@JSONField(ordinal = 5)
	private String content;
	@JSONField(ordinal = 6, format = "yyyy-MM-dd HH:mm:ss")
	private Date ctime;

	public SystemLog() {

	}

	public SystemLog(String name, String operate, String module, String content, Date ctime) {
		this.name = name;
		this.operate = operate;
		this.module = module;
		this.content = content;
		this.ctime = ctime;
	}

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

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public LogOperateVo getLogOperateVo() {
		return LogOperateVo.getInstance(operate);
	}

	public LogModuleVo getLogModuleVo() {
		return LogModuleVo.getInstance(module);
	}

}
