package core.solver.algorithm.heuristic;

import java.util.*;

import core.problem.Problem;
import core.problem.State;
import core.solver.algorithm.AbstractSearcher;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

/**
 * ������������㷨�� ���ݶ�Frontier��Node�����򷽷����Լ������õ����������Ĳ�ͬ �������ó���ͬ�����Ե��㷨��
 *  h(n) consistent
 * final�࣬���ܱ��̳е��ࡣͬѧ�ǲ��ɸ�д����࣡��
 *
 */
public final class BestFirstSearch extends AbstractSearcher {

	private final Predictor predictor; //Ԥ�������Ե�ǰ״̬��������ʽ��ֵ

	/**
	 * ���캯��
	 *
	 * @param explored �����״̬���Set hashSet
	 * @param frontier Node�����һ�����ȶ��У�����ȷ��һ��״̬����Ӧ�Ľ���Ƿ���frontier�У�
	 * @param predictor �����Ԥ����������λ���ƣ������پ���ȣ�
	 */
	public BestFirstSearch(Set<State> explored, Frontier frontier, Predictor predictor) {
		super(explored, frontier);
		this.predictor = predictor;
	}

	@Override
	public Deque<Node> search(Problem problem) {
		// ���ж������Ƿ�ɽ⣬�޽�ʱֱ�ӷ��ؽ�·��Ϊnull
		if (!problem.solvable()) {
			return null;
		}

		// ����ϴ�������Frontier��Explored�����¿�ʼ����
		frontier.clear();
		explored.clear();

		// ��ʼ�ڵ�root
		Node root = problem.root(predictor);
		frontier.offer(root);

		while (true) {
			if (frontier.isEmpty())
				return null; // ʧ��

			Node node = frontier.poll(); // choose the lowest-cost node in frontier
			if (problem.goal(node.getState())) {
				return generatePath(node);
			}

			explored.add(node.getState());

			for (Node child : problem.childNodes(node, predictor)) {
				// �������չ���Ľڵ㣬��û�б���չ������뵽frontier�С�
				if (!expanded(child))
					frontier.offer(child);
				// ��Ϊ�������������㵥��������ǰ���£�������Explored����Ľڵ㣬�϶�����Fringe�У�
				// ���ҵ�������ڵ����·���϶�����Ⱦ�·�����ţ�������������
				int xxx;
			}
		}
	}

	private boolean expanded(Node child) {
		return explored.contains(child.getState());
	}
}