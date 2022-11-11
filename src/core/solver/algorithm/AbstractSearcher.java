package core.solver.algorithm;

import core.problem.Problem;
import core.problem.State;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.Deque;
import java.util.Set;

/**
 * ����������ߣ�����������ʷ��
 * ʹ��explored�����Ѿ���չ���Ľ�㣬
 * ʹ��Frontier��������չ��������δ����չ�Ľ��
 *
 */
public abstract class AbstractSearcher implements Searcher {

    // �Ѿ����ʹ��Ľڵ㼯��
    protected final Set<State> explored;
    // ��δ��չ�Ľڵ����
    protected final Frontier frontier;

    public AbstractSearcher(Set<State> explored, Frontier frontier) {
        this.explored = explored;
        this.frontier = frontier;
    }

    @Override
    public abstract Deque<Node> search(Problem problem);

    /**
     *
     * @return	�㷨ִ�����ˣ��Ѿ�����չ�Ľ�������
     */
    @Override
    public int nodesExpanded() {
        return explored.size();
    }

    /**
     *
     * @return �Ѿ����ɵĽ��ĸ���
     */
    @Override
    public int nodesGenerated(){
        return explored.size() + frontier.size();
    }
}