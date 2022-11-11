package core.solver.algorithm.blinded;

import core.problem.Problem;
import core.problem.State;
import core.solver.algorithm.AbstractSearcher;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.Deque;
import java.util.Set;

/**
 * 改进的BFS和DFS的通用类，根据frontier的不同，分别对应BFS和DFS
 *
 * 请同学们参考。完成本项目用不到。
 *
 */
public final class BlindSearch extends AbstractSearcher {

	/**
	 * 构造函数
	 * 
	 * @param explored 具体的状态类的Set hashSet; 已生成的结点被看作explored.
	 * @param frontier Node对象的一个优先队列
	 */
	public BlindSearch(Set<State> explored, Frontier frontier) {
		super(explored, frontier);
	}

	@Override
	public Deque<Node> search(Problem problem) {
		// 先判断问题是否可解，无解时直接返回解路径为null
		if (!problem.solvable()) {
			return null;
		}

		// 清空上次搜索的Frontier和Explored，重新开始搜索
		frontier.clear();
		explored.clear();

		// 起始节点root
		Node root = problem.root();
		frontier.offer(root);
		while (true) {
			if (frontier.isEmpty())
				return null; // 失败
			Node node = frontier.poll(); // choose the lowest-cost node in frontier
			explored.add(root.getState());
			for (Node child : problem.childNodes(node)) {
				if (problem.goal(node.getState())) {
					return generatePath(node);
				}
				// 如果新生成的结点，是未访问过的，未在explored和frontier中出现过
				if (!visited(child)) {
					frontier.offer(child);
				}
			}
		}
	}

	private boolean visited(Node child) {
		return explored.contains(child.getState()) || frontier.contains(child);
	}
}