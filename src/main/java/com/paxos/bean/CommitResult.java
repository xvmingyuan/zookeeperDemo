package com.paxos.bean;
/**
 * 提案确认的回复结果(第二次)
 * @author xmy
 * @time：2018年8月22日 上午9:11:12
 */
public class CommitResult {
	boolean accepted = false;
	AcceptorStatus acceptorStatus = AcceptorStatus.NONE;
	Proposal proposal;
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
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
		return "CommitResult [accepted=" + accepted + ", acceptorStatus=" + acceptorStatus + ", proposal=" + proposal
				+ "]";
	}
	
}
