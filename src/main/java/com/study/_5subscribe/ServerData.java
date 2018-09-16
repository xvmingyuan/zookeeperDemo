package com.study._5subscribe;
/**
 * 记录WordServer的基本信息
 * @author xmy
 * @time：2018年9月13日 下午8:34:57
 */
public class ServerData {
	private Integer id;
	private String name;
	private String adress;
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getAdress() {
		return adress;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	@Override
	public String toString() {
		return "ServerData [id=" + id + ", name=" + name + ", adress=" + adress + "]";
	}
	
}
