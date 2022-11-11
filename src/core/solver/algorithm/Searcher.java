package core.solver.algorithm;

import core.problem.Problem;
import core.solver.queue.Node;

import java.util.ArrayDeque;
import java.util.Deque;

public interface Searcher {
    /**
     * ͨ�õ������㷨
     * @return ����ڶ�ջ�еĽ�·����ջ��Ϊ��ʼ���
     */
    Deque<Node> search(Problem problem);

    /**
     * Ĭ��ʵ�֣���Ŀ���㣬������ݵõ�һ��·��
     * @param goal Ŀ����
     * @return �������ɵĴӸ���㵽Ŀ�����·����ջ����Ŀ���㣬ջ���Ǹ����
     */
    default Deque<Node> generatePath(Node goal) {
        Deque<Node> stack = new ArrayDeque<>();
        Node curr = goal;
        while (curr.getParent() != null) {
            stack.push(curr);
            curr = curr.getParent();
        }
        return stack;
    }

    int nodesExpanded();

    int nodesGenerated();
}
