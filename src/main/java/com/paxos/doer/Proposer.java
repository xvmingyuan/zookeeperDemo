package com.paxos.doer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.paxos.bean.AcceptorStatus;
import com.paxos.bean.CommitResult;
import com.paxos.bean.PrepareResult;
import com.paxos.bean.Proposal;
import com.paxos.main.Client;
import com.paxos.util.PaxosUtil;
import com.paxos.util.PerformanceRecord;

/**
 * 提议者
 * 
 * @author xmy
 * @time：2018年8月21日 上午9:47:53
 */

public class Proposer implements Runnable {
	int myID = 0;
	private String name;
	// 提议者的提案
	private Proposal proposal;
	// 是否已经有提案通过大多数的acceptor的确认
	private boolean voted;
	// 大多数决策者的最小个数
	private int halfCount;
	// 提议者总数
	private int proposerCount;

	private int numCycle = 0;
	// 决策者集合
	private List<Acceptor> acceptors;

	public Proposer(int myID, String name, int proposecount, List<Acceptor> acceptors) {
		this.myID = myID;
		this.name = name;
		this.proposerCount = proposecount;
		this.acceptors = acceptors;
		numCycle = 0;
		voted = false;
		halfCount = acceptors.size() / 2 + 1; // 多数派
		this.proposal = new Proposal(PaxosUtil.generateId(myID, numCycle, proposecount), myID + "的Proposal",
				myID + ":leader");
		numCycle++;
	}

	// 准备阶段
	public synchronized boolean prepare() {
//		System.out.println(proposal.getPid() + "提案准备阶段开始");
		PrepareResult prepareResult = null;
		boolean isContinue = true;
		// 已获得承诺个数
		int promisedCount = 0;
		do {
			// 决策者已承诺的提案集合
			List<Proposal> promisedProposals = new ArrayList<Proposal>();
			// 决策者已接受(确定)的提案集合
			List<Proposal> acceptedProposals = new ArrayList<Proposal>();
			// 承诺数量
			promisedCount = 0;
			for (Acceptor acceptor : acceptors) {
				// 发送准备提案给决策者
				prepareResult = acceptor.onPrepare(proposal);
				// 随机休眠一段时间，模拟网络延迟。
				PaxosUtil.sleepRandom();
				// 模拟网络异常
				if (null == prepareResult) {
					continue;
				}
				// 获得承诺
				if (prepareResult.isPromised()) {
					promisedCount++;
				} else {
					// 决策者已经给了更高id题案的承诺
					if (prepareResult.getAcceptorStatus() == AcceptorStatus.PROMISED) {
						promisedProposals.add(prepareResult.getProposal());
					}
					// 决策者已经通过了一个题案
					if (prepareResult.getAcceptorStatus() == AcceptorStatus.ACCEPTED) {
						acceptedProposals.add(prepareResult.getProposal());
					}
				}

			}
			// 获得多数决策者的承诺
			// 可以进行第二阶段：题案提交
			if (promisedCount >= halfCount) {
				break;
			}
			Proposal votedProposal = voteEnd(acceptedProposals);
			// 决策者已经半数通过题案
			if (votedProposal != null) {
				System.out.println(myID + " : 已经投票结束:" + votedProposal);
				return true;
			}

			Proposal maxIdAcceptedProposal = getMaxIdProposal(acceptedProposals);
//			System.out.println("准备阶段者" + proposal);
			// 在已经被决策者通过题案中选择序列号最大的决策,作为自己的决策
			if (maxIdAcceptedProposal != null) {
				proposal.setPid(PaxosUtil.generateId(myID, numCycle, proposerCount));
				proposal.setPvalue(maxIdAcceptedProposal.getPvalue());
			} else {
				proposal.setPid(PaxosUtil.generateId(myID, numCycle, proposerCount));
			}

			numCycle++;
		} while (isContinue);
		return false;

	}

	// 确认阶段
	public synchronized boolean commit() {
//		System.out.println(proposal.getPid() + "提案确认阶段开始");
		boolean isContinue = true;
		// 已获得接受该提案的决策者个数
		int acceptedCount = 0;
		do {
			// 决策者已接受(确定)的提案集合
			List<Proposal> acceptedProposals = new ArrayList<Proposal>();
			// 接受该提案的决策者个数
			acceptedCount = 0;

			for (Acceptor acceptor : acceptors) {
				CommitResult commitResult = acceptor.onCommit(proposal);
				// 模拟网络延迟
				PaxosUtil.sleepRandom();
				if (null == commitResult) {
					continue;
				}
				if (commitResult.isAccepted()) {// 题案被决策者接受。
					acceptedCount++;
				} else {// 将未接受的提案 放入集合中
					acceptedProposals.add(commitResult.getProposal());
				}
			}

			// 题案被半数以上决策者接受，说明题案已经被选出来。
			if (acceptedCount >= halfCount) {
				System.out.println(myID + " : 题案已经投票选出:" + proposal);
				String idStr = proposal.getPvalue().split(":")[0];
				if (Integer.parseInt(idStr) == myID) {
					System.out.println("保存状态为:leader");
				} else {
					System.out.println("保存状态为:follower");
				}
				return true;
			} else {
//				System.out.println("确认阶段者:" + proposal);
				Proposal maxIdAcceptedProposal = getMaxIdProposal(acceptedProposals);
				if (maxIdAcceptedProposal != null) {
					proposal.setPid(PaxosUtil.generateId(myID, numCycle, proposerCount));
					proposal.setPvalue(maxIdAcceptedProposal.getPvalue());
				} else {
					proposal.setPid(PaxosUtil.generateId(myID, numCycle, proposerCount));
				}

				numCycle++;
				if (prepare()) {
					return true;
				}
			}

		} while (isContinue);
		return true;

	}

	// 获取序列号最大的提案
	private Proposal getMaxIdProposal(List<Proposal> acceptedProposal) {
		// System.out.println("getMaxIdProposal"+acceptedProposal);
		if (acceptedProposal.size() > 0) {
			Proposal retProposal = acceptedProposal.get(0);
			for (Proposal proposal : acceptedProposal) {
				if (proposal.getPid() > retProposal.getPid())
					retProposal = proposal;
			}

			return retProposal;
		}
		return null;
	}

	// 是否已经有提案被大多数决策者确然
	private Proposal voteEnd(List<Proposal> acceptedProposal) {

		Map<Proposal, Integer> proposalCount = countAcceptedProposalCount(acceptedProposal);
		for (Entry<Proposal, Integer> entry : proposalCount.entrySet()) {
			if (entry.getValue() >= halfCount) {
				voted = true;
				return entry.getKey();
			}
		}
		return null;

	}

	// 计算决策者回复的每个已经被接受的提案计数
	private Map<Proposal, Integer> countAcceptedProposalCount(List<Proposal> acceptedProposal) {
		Map<Proposal, Integer> proposalCount = new HashMap<>();
		for (Proposal proposal : acceptedProposal) {
			// 决策者没有回复，或者网络异常
			if (null == proposal)
				continue;
			int count = 1;
			if (proposalCount.containsKey(proposal)) {
				count = proposalCount.get(proposal) + 1;
			}
			proposalCount.put(proposal, count);

		}
		return proposalCount;

	}

	// 执行投票活动
	@Override
	public void run() {
		Client.latch.countDown();
		try {
			Client.latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PerformanceRecord.getInstance().start("Proposer" + myID, myID);
		prepare();
		commit();
		PerformanceRecord.getInstance().end(myID);
	}
}
