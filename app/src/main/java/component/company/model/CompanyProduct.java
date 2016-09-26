package component.company.model;

import component.common.model.BaseModel;

/** 产品 */
public class CompanyProduct extends BaseModel {

	private String code; // 产品编码
	private String name; // 产品名称
	private String spec; // 规格
	private String intro; // 简介
	private String qrcode; // 二维码相对路径

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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

}
