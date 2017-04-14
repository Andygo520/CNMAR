package component.com.model;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.vo.BoxTypeVo;
import component.produce.model.ProduceBom;
import component.produce.model.ProducePlan;
import component.system.model.SystemUser;

/** 料框 */
public class ComBox {

	@JSONField(ordinal = 1)
	private int id;
	@JSONField(ordinal = 2)
	private String code; // 编码
	@JSONField(ordinal = 3)
	private int type; // 类型
	@JSONField(serialize = false)
	private String qrcode; // 二维码相对路径
	@JSONField(ordinal = 4)
	private int planId; // 加工单id
	@JSONField(ordinal = 5)
	private int bomId; // 子加工单id
	@JSONField(ordinal = 6)
	private int processId; // 工序id
	@JSONField(ordinal = 7)
	private int stationId; // 机台工位id
	@JSONField(ordinal = 8)
	private int num; // 现存数量
	@JSONField(ordinal = 9)
	private int userId; // 操作工id
	@JSONField(serialize = false)
	private Boolean isDel;

	@JSONField(ordinal = 11)
	private ComBox testBox; // 待检验料框
	@JSONField(ordinal = 12)
	private ProducePlan plan;
	@JSONField(ordinal = 13)
	private ProduceBom bom;
	@JSONField(ordinal = 17)
	private SystemUser user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public int getBomId() {
		return bomId;
	}

	public void setBomId(int bomId) {
		this.bomId = bomId;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

	public ComBox getTestBox() {
		return testBox;
	}

	public void setTestBox(ComBox testBox) {
		this.testBox = testBox;
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

	public SystemUser getUser() {
		return user;
	}

	public void setUser(SystemUser user) {
		this.user = user;
	}

	@JSONField(serialize = false)
	public BoxTypeVo getBoxTypeVo() {
		return BoxTypeVo.getInstance(type);
	}

}
