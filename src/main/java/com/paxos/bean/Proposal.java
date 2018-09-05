package com.paxos.bean;

/**
 * 提案
 * 
 * @author xmy
 * @time：2018年8月22日 上午9:09:46
 */
public class Proposal {
	int pid;// 提案编号
	String pname;// 提案名称
	String pvalue;// 提案信息

	public Proposal() {
	}

	public Proposal(int pid, String pname, String pvalue) {
		this.pid = pid;
		this.pname = pname;
		this.pvalue = pvalue;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPvalue() {
		return pvalue;
	}

	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}

	public void copyFromInstance(Proposal proposal) {
		this.pid = proposal.pid;
		this.pname = proposal.pname;
		this.pvalue = proposal.pvalue;
	}

	@Override
	public String toString() {
		return "Proposal [pid=" + pid + ", pname=" + pname + ", pvalue=" + pvalue + "]";
	}

}
