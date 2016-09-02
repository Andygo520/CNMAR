package com.example.administrator.cnmar.model;

import com.alibaba.fastjson.annotation.JSONField;

public class ImgModel extends BaseModel {

	@JSONField(ordinal = 91)
	private int imgId;// 图片id
	@JSONField(ordinal = 92)
	private Img img;

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public Img getImg() {
		return img;
	}

	public void setImg(Img img) {
		this.img = img;
	}

}
