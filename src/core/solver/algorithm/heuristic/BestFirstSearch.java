package core.solver.algorithm.heuristic;

import java.util.*;

import core.problem.Problem;
import core.problem.State;
import core.solver.algorithm.AbstractSearcher;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

/**
 * 最佳优先搜索算法。 根据对Frontier中Node的排序方法，以及所采用的启发函数的不同 可以配置出不同的特性的算法。
 *  h(n) consistent
 * final类，不能被继承的类。同学们不可改写这个类！！
 *
 */
public final class BestFirstSearch extends AbstractSearcher {

	private final Predictor predictor; //预测器，对当前状态进行启发式估值

	/**
	 * 构造函数
	 *
	 * @param explored 具体的状态类的Set hashSet
	 * @param frontier Node对象的一个优先队列，可以确定一个状态所对应的结点是否在frontier中，
	 * @param predictor 具体的预测器（不在位将牌，曼哈顿距离等）
	 */
	public BestFirstSearch(Set<State> explored, Frontier frontier, Predictor predictor) {
		super(explored, frontier);
		this.predictor = predictor;
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
		Node root = problem.root(predictor);
		frontier.offer(root);

		while (true) {
			if (frontier.isEmpty())
				return null; // 失败

			Node node = frontier.poll(); // choose the lowest-cost node in frontier
			if (problem.goal(node.getState())) {
				return generatePath(node);
			}

			explored.add(node.getState());

			for (Node child : problem.childNodes(node, predictor)) {
				// 如果新扩展出的节点，还没有被扩展，则插入到frontier中。
				if (!expanded(child))
					frontier.offer(child);
				// 因为在启发函数满足单调条件的前提下，出现在Explored表里的节点，肯定不在Fringe中；
				// 而且到达这个节点的新路径肯定不会比旧路径更优，所以舍弃掉。
				int xxx;
			}
		}
	}

	private boolean expanded(Node child) {
		return explored.contains(child.getState());
	}
}