package component.half.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;
import component.half.vo.OutOrderStatusVo;
import component.produce.model.ProduceReceive;

/** 半成品出库单 */
public class HalfOutOrder extends BaseModel {

	@JSONField(ordinal = 1)
	private String code; // 出库单号
	@JSONField(ordinal = 2, format = "yyyyMMddHHmmss")
	private Date outTime; // 出库时间
	@JSONField(ordinal = 3)
	private String remark; // 备注
	@JSONField(ordinal = 4)
	private int status; // 出库单状态 - 2待出库3已出库4未全部出库

	@JSONField(ordinal = 5)
	private ProduceReceive receive;
	@JSONField(ordinal = 6)
	private List<HalfOutOrderHalf> outOrderHalfs;

	public HalfOutOrder() {

	}

	public HalfOutOrder(String code, String remark, int status, int cid, Date ctime) {
		this.code = code;
		this.remark = remark;
		this.status = status;
		this.cid = cid;
		this.ctime = ctime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ProduceReceive getReceive() {
		return receive;
	}

	public void setReceive(ProduceReceive receive) {
		this.receive = receive;
	}

	public List<HalfOutOrderHalf> getOutOrderHalfs() {
		return outOrderHalfs;
	}

	public void setOutOrderHalfs(List<HalfOutOrderHalf> outOrderHalfs) {
		this.outOrderHalfs = outOrderHalfs;
	}

	@JSONField(serialize = false)
	public OutOrderStatusVo getOutOrderStatusVo() {
		return OutOrderStatusVo.getInstance(status);
	}

}
