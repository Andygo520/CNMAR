package component.product.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;
import component.produce.model.ProducePlan;
import component.product.vo.InOrderStatusVo;

/** 成品入库单 */
public class ProductInOrder extends BaseModel {

	@JSONField(ordinal = 1)
	private String code; // 入库单号
	@JSONField(ordinal = 2, format = "yyyyMMddHHmmss")
	private Date inTime; // 入库时间
	@JSONField(ordinal = 5)
	private String remark; // 备注
	@JSONField(ordinal = 6)
	private int status; // 入库单状态 - 1待打印2待入库3已入库4未全部入库

	@JSONField(ordinal = 7)
	private ProducePlan producePlan;
	@JSONField(ordinal = 8)
	private List<ProductInOrderProduct> inOrderProducts;
	@JSONField(serialize = false)
	private boolean showPrint;

	public ProductInOrder() {

	}

	public ProductInOrder(String code, String remark, int status, int cid, Date ctime) {
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

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
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

	public ProducePlan getProducePlan() {
		return producePlan;
	}

	public void setProducePlan(ProducePlan producePlan) {
		this.producePlan = producePlan;
	}

	public List<ProductInOrderProduct> getInOrderProducts() {
		return inOrderProducts;
	}

	public void setInOrderProducts(List<ProductInOrderProduct> inOrderProducts) {
		this.inOrderProducts = inOrderProducts;
	}

	public boolean isShowPrint() {
		return showPrint;
	}

	public void setShowPrint(boolean showPrint) {
		this.showPrint = showPrint;
	}

	@JSONField(serialize = false)
	public InOrderStatusVo getInOrderStatusVo() {
		return InOrderStatusVo.getInstance(status);
	}

}
