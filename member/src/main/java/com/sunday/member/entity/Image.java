package com.sunday.member.entity;

import com.google.gson.annotations.SerializedName;

public class Image {
	protected int width;
	protected int height;
	@SerializedName(value = "viewUrl",alternate = {"viewPath"})
	protected String viewUrl;
	protected String saveUrl;
	protected String name;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
