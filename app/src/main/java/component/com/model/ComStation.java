package component.com.model;

import com.alibaba.fastjson.annotation.JSONField;

/** 机台工位 */
public class ComStation {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(serialize = false)
	private int workshopId; // 车间id
	@JSONField(ordinal = 2)
	private String code; // 编码
	@JSONField(ordinal = 3)
	private String name; // 名称
	@JSONField(serialize = false)
	private String qrcode; // 二维码相对路径
	@JSONField(serialize = false)
	private Boolean isDel;

	@JSONField(ordinal = 5)
	private ComWorkshop workshop;
	@JSONField(ordinal = 6)
	private int successNum; // 合格品数量
	@JSONField(ordinal = 7)
	private int failureNum; // 不合格品数量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWorkshopId() {
		return workshopId;
	}

	public void setWorkshopId(int workshopId) {
		this.workshopId = workshopId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

	public ComWorkshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(ComWorkshop workshop) {
		this.workshop = workshop;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public int getFailureNum() {
		return failureNum;
	}

	public void setFailureNum(int failureNum) {
		this.failureNum = failureNum;
	}

}
