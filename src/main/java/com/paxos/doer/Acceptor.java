package com.paxos.doer;

import com.paxos.bean.AcceptorStatus;
import com.paxos.bean.CommitResult;
import com.paxos.bean.PrepareResult;
import com.paxos.bean.Proposal;
import com.paxos.util.PaxosUtil;

/**
 * 接受者(提议)
 * 
 * @author xmy
 * @time：2018年8月21日 上午9:49:05
 */
public class Acceptor {
	private int acceptor_id;
	private AcceptorStatus status = AcceptorStatus.NONE;
	private Proposal promisedProposal = new Proposal();
	private Proposal acceptedProposal = new Proposal();

	public Acceptor(int acceptor_id) {
		this.acceptor_id = acceptor_id;
	}

	public int getAcceptor_id() {
		return acceptor_id;
	}

	public void setAcceptor_id(int acceptor_id) {
		this.acceptor_id = acceptor_id;
	}

	@Override
	public String toString() {
		return "Acceptor [acceptor_id=" + acceptor_id + ", status=" + status + ", promisedProposal=" + promisedProposal
				+ ", acceptedProposal=" + acceptedProposal + "]";
	}

	// 加锁函数,不允许同时访问,模拟串行访问,准备阶段,返回结果
	public synchronized PrepareResult onPrepare(Proposal szproposal) {
		PrepareResult prepareResult = new PrepareResult();
		// 模拟网络不正常，发生丢包、超时现象
		if (PaxosUtil.isCrashed()) {
			return null;
		}
		switch (status) {
		// NONE表示之前没有承诺过任何提议者
		// 此时，接受提案
		case NONE:
			prepareResult.setAcceptorStatus(AcceptorStatus.NONE);
			prepareResult.setPromised(true);
			prepareResult.setProposal(null);
			// 转换自身的状态，已经承诺了提议者，并记录承诺的提案。
			status = AcceptorStatus.PROMISED;
			promisedProposal.copyFromInstance(szproposal);
			return prepareResult;
		// 已经承诺过任意提议者
		case PROMISED:
			// 判断提案的先后顺序，只承诺相对较新的提案
			if (promisedProposal.getPid() > szproposal.getPid()) {
				prepareResult.setAcceptorStatus(status);
				prepareResult.setPromised(false);
				prepareResult.setProposal(promisedProposal);
				return prepareResult;
			} else {
				promisedProposal.copyFromInstance(szproposal);
				prepareResult.setAcceptorStatus(status);
				prepareResult.setPromised(true);
				prepareResult.setProposal(promisedProposal);
				return prepareResult;
			}
			// 已经批准过提案
		case ACCEPTED:
			// 如果是同一个提案，只是序列号增大
			// 批准提案，更新序列号。
			if (promisedProposal.getPid() < szproposal.getPid()
					&& promisedProposal.getPvalue().equals(szproposal.getPvalue())) {
				promisedProposal.setPid(szproposal.getPid());
				prepareResult.setAcceptorStatus(status);
				prepareResult.setPromised(true);
				prepareResult.setProposal(promisedProposal);
				return prepareResult;
			} else {
				prepareResult.setAcceptorStatus(status);
				prepareResult.setPromised(false);
				prepareResult.setProposal(acceptedProposal);
				return prepareResult;
			}
		default:
		}
		return null;
	}

	// 加锁,模拟串行,确定阶段,返回结果
	public synchronized CommitResult onCommit(Proposal szproposal) {
		CommitResult commitResult = new CommitResult();
		switch (status) {
		// 不可能存在此状态
		case NONE:
			return null;
		// 已经承诺过提案
		case PROMISED:
			// 判断commit提案和承诺提案的序列号大小
			// 大于，接受提案。
			if (szproposal.getPid() >= acceptedProposal.getPid()) {
				promisedProposal.copyFromInstance(szproposal);
				acceptedProposal.copyFromInstance(szproposal);
				status = AcceptorStatus.ACCEPTED;
				
				commitResult.setAccepted(true);
				commitResult.setAcceptorStatus(status);
				commitResult.setProposal(promisedProposal);
				return commitResult;
			} else {// 小于，回绝提案
				commitResult.setAccepted(false);
				commitResult.setAcceptorStatus(status);
				commitResult.setProposal(promisedProposal);
				return commitResult;
			}
			// 已接受过提案
		case ACCEPTED:
			// 同一提案，序列号较大，接受
			if (szproposal.getPid() > acceptedProposal.getPid()
					&& szproposal.getPvalue().equals(acceptedProposal.getPvalue())) {
				acceptedProposal.setPid(szproposal.getPid());
				commitResult.setAccepted(true);
				commitResult.setAcceptorStatus(status);
				commitResult.setProposal(acceptedProposal);
				return commitResult;
			} else {// 否则，回绝提案
				commitResult.setAccepted(false);
				commitResult.setAcceptorStatus(status);
				commitResult.setProposal(acceptedProposal);
				return commitResult;
			}
		default:
		}
		return null;

	}

}
