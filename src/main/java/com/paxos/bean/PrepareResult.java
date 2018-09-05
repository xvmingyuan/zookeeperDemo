package com.paxos.bean;
/**
 * 提案准备的回复结果(第一次)
 * @author xmy
 * @time：2018年8月21日 上午9:55:15
 */
public class PrepareResult {
	
	private boolean isPromised;
	private AcceptorStatus acceptorStatus = AcceptorStatus.NONE;
	private Proposal proposal;
	public boolean isPromised() {
		return isPromised;
	}
	public void setPromised(boolean isPromised) {
		this.isPromised = isPromised;
	}
	public AcceptorStatus getAcceptorStatus() {
		return acceptorStatus;
	}
	public void setAcceptorStatus(AcceptorStatus acceptorStatus) {
		this.acceptorStatus = acceptorStatus;
	}
	public Proposal getProposal() {
		return proposal;
	}
	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}
	@Override
	public String toString() {
		return "PrepareResult [isPromised=" + isPromised + ", acceptorStatus=" + acceptorStatus + ", proposal="
				+ proposal + "]";
	}
	
	
	
}
