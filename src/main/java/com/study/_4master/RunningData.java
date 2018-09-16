package com.study._4master;

import java.io.Serializable;

/**
 * master选举 描述Worker Server的基本信息
 * @author xmy
 * @time：2018年9月9日 下午3:13:49
 */
public class RunningData implements Serializable{
  
	private static final long serialVersionUID = 2076607750271762939L;
	private Long cid;
	private String name;
	public Long getCid() {
		return cid;
	}
	public String getName() {
		return name;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
