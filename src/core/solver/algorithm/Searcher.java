package core.solver.algorithm;

import core.problem.Problem;
import core.solver.queue.Node;

import java.util.ArrayDeque;
import java.util.Deque;

public interface Searcher {
    /**
     * 通用的搜索算法
     * @return 存放在堆栈中的解路径，栈顶为初始结点
     */
    Deque<Node> search(Problem problem);

    /**
     * 默认实现，从目标结点，反向回溯得到一条路径
     * @param goal 目标结点
     * @return 倒推生成的从根结点到目标结点的路径，栈底是目标结点，栈顶是根结点
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
