package core.solver.queue;

import core.problem.Action;
import core.problem.State;
import core.solver.algorithm.heuristic.EvaluationType;
import core.solver.algorithm.heuristic.Predictor;

import java.util.Comparator;
import java.util.EnumMap;
import static core.solver.algorithm.heuristic.EvaluationType.*;

/**
 * 搜索树的结点类
 *
 * 同学们不需要修改这个类！！
 */
public final class Node implements Comparable<Node> {

	/**
	 * Node for informed search
	 * 
	 * @param state     当前结点
	 * @param parent    父结点
	 * @param action    从父结点到当前结点所采取的Action
	 * @param pathCost  从根结点到当前结点的耗散值
	 * @param heuristic 从当前结点到目标结点的距离估计值
	 */
	public Node(State state, Node parent, Action action, int pathCost, int heuristic) {
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.pathCost = pathCost;
		this.heuristic = heuristic;
	}

	/**
	 * Node for blind search
	 * @param parent
	 * @param action
	 * @param pathCost
	 */
	public Node(State state, Node parent, Action action, int pathCost) {
		this(state, parent, action, pathCost, 0);
	}

	public Action getAction() {
		return action;
	}

	// 返回当前Node的f值 f = g + h
	public int evaluation() {
		return pathCost + heuristic;
	}

	public State getState() {
		return state;
	}

	public Node getParent() {
		return parent;
	}

	public int getPathCost() {
		return pathCost;
	}

	public int getHeuristic() {
		return heuristic;
	}

	/**
	 * Node的状态相同，即认为他们是相同的
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj instanceof Node) {
			Node another = (Node) obj;
			// 两个Node对象的状态相同，则认为是相同的
			return this.getState().equals(another.getState());
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + state.toString() + "⬅" + "[" + parent.getState().toString() + ", " + action.toString() + "], "
				+ pathCost + ", " + heuristic + "]";
	}

	private final State state; // the state in the state space to which the node corresponds
	private final Node parent; // the node in the search tree which generated this node
	private final Action action; // the action that was applied to the parent to generate the node
	private final int pathCost; // the cost of the path from the initial state to this node
	private int heuristic; 		// estimated cost of the cheapest path from the state of this node to a goal
									// state

	/**
	 * 不同估值函数的枚举映射表(EnumMap)
	 */
	private static final EnumMap<EvaluationType, Comparator<Node>> evaluators =
			new EnumMap<>(EvaluationType.class);
	// 评估器类型与所对应的评估器之间的映射关系的静态初始化
	static {
		// f = g + h FULL
		evaluators.put(FULL, (o1, o2) -> {
			// 如果二者的f值相同
			if (o1.evaluation() == o2.evaluation()) {
				// 返回浅层的Node
				return o1.getPathCost() - o2.getPathCost();
			}
			return o1.evaluation() - o2.evaluation();
		});

		// g PATH_COST
		evaluators.put(PATH_COST, Comparator.comparingInt(Node::getPathCost));

		// h HEURISTIC
		evaluators.put(HEURISTIC, Comparator.comparingInt(Node::getHeuristic));
	}

	/**
	 * 根据评估器类型获得相关的评估器
	 * 
	 * @param type 结点评估器的类型
	 * @return 相关类型的结点评估器
	 */
	public static Comparator<Node> evaluator(EvaluationType type) {
		return evaluators.get(type);
	}

	/**
	 * 自然序 比较二者的f值 f = g + h
	 * 
	 * @param another 另外的Node
	 * @return >0; <0; =0
	 */
	@Override
	public int compareTo(Node another) {
		return this.evaluation() - another.evaluation();
	}

	/**
	 * 设置Node的h值
	 * @param predictor
	 * @param goal
	 */
	public void setHeuristic(Predictor predictor, State goal) {
		this.heuristic = predictor.heuristics(this.state, goal);
	}

	public Node setHeuristic(int heuristics) {
		this.heuristic = heuristics;
		return this;
	}
}