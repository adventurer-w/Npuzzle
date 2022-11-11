package core.solver.algorithm.blinded;

import core.problem.Problem;
import core.problem.State;
import core.solver.algorithm.AbstractSearcher;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.Deque;
import java.util.Set;

/**
 * �Ľ���BFS��DFS��ͨ���࣬����frontier�Ĳ�ͬ���ֱ��ӦBFS��DFS
 *
 * ��ͬѧ�ǲο�����ɱ���Ŀ�ò�����
 *
 */
public final class BlindSearch extends AbstractSearcher {

	/**
	 * ���캯��
	 * 
	 * @param explored �����״̬���Set hashSet; �����ɵĽ�㱻����explored.
	 * @param frontier Node�����һ�����ȶ���
	 */
	public BlindSearch(Set<State> explored, Frontier frontier) {
		super(explored, frontier);
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
		Node root = problem.root();
		frontier.offer(root);
		while (true) {
			if (frontier.isEmpty())
				return null; // ʧ��
			Node node = frontier.poll(); // choose the lowest-cost node in frontier
			explored.add(root.getState());
			for (Node child : problem.childNodes(node)) {
				if (problem.goal(node.getState())) {
					return generatePath(node);
				}
				// ��������ɵĽ�㣬��δ���ʹ��ģ�δ��explored��frontier�г��ֹ�
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