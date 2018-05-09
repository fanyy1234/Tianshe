package com.sunday.member.entity;

/**
 * 品牌DTO
 * @author sunday
 *
 */
public class Brand {

	private String logo;//品牌logo 阿里云oss文件id
	private String name; //品牌名称
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
