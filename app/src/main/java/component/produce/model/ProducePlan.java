package component.produce.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import component.com.model.ComBox;
import component.common.model.BaseModel;
import component.process.model.ProcessProduct;
import component.produce.vo.ProduceStatusVo;
import component.product.model.Product;

/** 加工单 */
public class ProducePlan extends BaseModel implements Cloneable {

	@JSONField(ordinal = 2)
	private int productId; // 成品id
	@JSONField(ordinal = 3)
	private String code; // 加工单编号
	@JSONField(ordinal = 4)
	private String name; // 加工单名称
	@JSONField(ordinal = 5)
	private int produceNum; // 计划生产数量
	@JSONField(ordinal = 6, format = "yyyy-MM-dd")
	private Date startDate; // 开始日期
	@JSONField(ordinal = 7, format = "yyyy-MM-dd")
	private Date endDate; // 结束日期
	@JSONField(ordinal = 8)
	private int status; // 状态

	@JSONField(ordinal = 11)
	private Product product;
	@JSONField(ordinal = 12)
	private ProduceReceive receive;
	@JSONField(ordinal = 13)
	private List<ProduceBom> produceBoms;
	@JSONField(ordinal = 14)
	private List<ProduceBom> materialSubs;
	@JSONField(ordinal = 15)
	private List<ProduceBom> halfSubs;
	@JSONField(ordinal = 18)
	private List<ProducePlanBatch> batchs;
	@JSONField(ordinal = 19)
	private ProducePlanBatch batchGroup;

	@JSONField(ordinal = 20)
	private ProcessProduct processProduct;
	@JSONField(ordinal = 21)
	private List<ComBox> lastProcessBoxs;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
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

	public int getProduceNum() {
		return produceNum;
	}

	public void setProduceNum(int produceNum) {
		this.produceNum = produceNum;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProduceReceive getReceive() {
		return receive;
	}

	public void setReceive(ProduceReceive receive) {
		this.receive = receive;
	}

	public List<ProduceBom> getProduceBoms() {
		return produceBoms;
	}

	public void setProduceBoms(List<ProduceBom> produceBoms) {
		this.produceBoms = produceBoms;
	}

	public List<ProduceBom> getMaterialSubs() {
		return materialSubs;
	}

	public void setMaterialSubs(List<ProduceBom> materialSubs) {
		this.materialSubs = materialSubs;
	}

	public List<ProduceBom> getHalfSubs() {
		return halfSubs;
	}

	public void setHalfSubs(List<ProduceBom> halfSubs) {
		this.halfSubs = halfSubs;
	}

	public List<ProducePlanBatch> getBatchs() {
		return batchs;
	}

	public void setBatchs(List<ProducePlanBatch> batchs) {
		this.batchs = batchs;
	}

	public ProducePlanBatch getBatchGroup() {
		return batchGroup;
	}

	public void setBatchGroup(ProducePlanBatch batchGroup) {
		this.batchGroup = batchGroup;
	}

	public ProcessProduct getProcessProduct() {
		return processProduct;
	}

	public void setProcessProduct(ProcessProduct processProduct) {
		this.processProduct = processProduct;
	}

	public List<ComBox> getLastProcessBoxs() {
		return lastProcessBoxs;
	}

	public void setLastProcessBoxs(List<ComBox> lastProcessBoxs) {
		this.lastProcessBoxs = lastProcessBoxs;
	}

	@JSONField(serialize = false)
	public ProduceStatusVo getProduceStatusVo() {
		return ProduceStatusVo.getInstance(status);
	}

	public ProducePlan clone() throws CloneNotSupportedException {
		ProducePlan clone = (ProducePlan) super.clone();
		if (clone.processProduct != null) {
			clone.processProduct = processProduct.clone();
		}
		return clone;
	}

}
