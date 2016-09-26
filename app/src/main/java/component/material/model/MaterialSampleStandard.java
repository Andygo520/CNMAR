package component.material.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import component.common.model.BaseModel;

/** 原料抽样标准 */
public class MaterialSampleStandard extends BaseModel {

	@JSONField(ordinal = 1)
	private int minNum; // 最小批量
	@JSONField(ordinal = 2)
	private int maxNum; // 最大批量
	@JSONField(ordinal = 3)
	private int sampleNum; // 样本量
	@JSONField(ordinal = 4)
	private int acNum; // 接收数
	@JSONField(ordinal = 5)
	private int reNum; // 拒收数

	public MaterialSampleStandard() {

	}

	public MaterialSampleStandard(int minNum, int maxNum, int sampleNum, int acNum, int reNum, int cid, Date ctime) {
		this.minNum = minNum;
		this.maxNum = maxNum;
		this.sampleNum = sampleNum;
		this.acNum = acNum;
		this.reNum = reNum;
		this.cid = cid;
		this.ctime = ctime;
	}

	public int getMinNum() {
		return minNum;
	}

	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getSampleNum() {
		return sampleNum;
	}

	public void setSampleNum(int sampleNum) {
		this.sampleNum = sampleNum;
	}

	public int getAcNum() {
		return acNum;
	}

	public void setAcNum(int acNum) {
		this.acNum = acNum;
	}

	public int getReNum() {
		return reNum;
	}

	public void setReNum(int reNum) {
		this.reNum = reNum;
	}

}
