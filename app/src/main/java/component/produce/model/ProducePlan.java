package component.produce.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

import component.common.model.BaseModel;
import component.product.model.Product;
import component.product.model.ProductInOrder;
import component.system.model.SystemUser;

/** 加工单 */
public class ProducePlan extends BaseModel {

	@JSONField(ordinal = 1)
	private int productInOrderId; // 成品入库单id
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
	private int actualNum; // 实际生产数量
	@JSONField(ordinal = 9)
	private int actualId; // 统计员id
	@JSONField(ordinal = 10)
	private int successNum; // 合格品数量
	@JSONField(ordinal = 11)
	private int testId;
	@JSONField(ordinal = 12, format = "yyyy-MM-dd HH:mm:ss")
	private Date testTime;

	@JSONField(ordinal = 13)
	private Product product;
	@JSONField(ordinal = 14)
	private ProduceReceive receive;
	@JSONField(ordinal = 15)
	private SystemUser test;
	@JSONField(ordinal = 16)
	private ProductInOrder productInOrder;
	@JSONField(ordinal = 17)
	private List<ProduceBom> produceBoms;
	@JSONField(ordinal = 18)
	private List<ProduceBom> materialSubs;
	@JSONField(ordinal = 19)
	private List<ProduceBom> halfSubs;

	public int getProductInOrderId() {
		return productInOrderId;
	}

	public void setProductInOrderId(int productInOrderId) {
		this.productInOrderId = productInOrderId;
	}

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

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public int getActualId() {
		return actualId;
	}

	public void setActualId(int actualId) {
		this.actualId = actualId;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public Date getTestTime() {
		return testTime;
	}

	public void setTestTime(Date testTime) {
		this.testTime = testTime;
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

	public SystemUser getTest() {
		return test;
	}

	public void setTest(SystemUser test) {
		this.test = test;
	}

	public ProductInOrder getProductInOrder() {
		return productInOrder;
	}

	public void setProductInOrder(ProductInOrder productInOrder) {
		this.productInOrder = productInOrder;
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

}
