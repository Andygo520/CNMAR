package component.com.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.produce.model.ProduceBom;
import component.produce.model.ProducePlan;

/** 机台工位 */
public class ComStation implements Cloneable {

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
	private ProducePlan plan;
	@JSONField(ordinal = 7)
	private ProduceBom bom;

	@JSONField(serialize = false)
	private boolean checked;

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

	public ProducePlan getPlan() {
		return plan;
	}

	public void setPlan(ProducePlan plan) {
		this.plan = plan;
	}

	public ProduceBom getBom() {
		return bom;
	}

	public void setBom(ProduceBom bom) {
		this.bom = bom;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public ComStation clone() throws CloneNotSupportedException {
		ComStation clone = (ComStation) super.clone();
		if (clone.plan != null) {
			clone.plan = plan.clone();
		}
		if (clone.bom != null) {
			clone.bom = bom.clone();
		}
		return clone;
	}

}
