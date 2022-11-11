package core.solver.algorithm;

import core.problem.Problem;
import core.problem.State;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.Deque;
import java.util.Set;

/**
 * 抽象的搜素者，保留搜索历史。
 * 使用explored保存已经扩展过的结点，
 * 使用Frontier保存已扩展出来但尚未被扩展的结点
 *
 */
public abstract class AbstractSearcher implements Searcher {

    // 已经访问过的节点集合
    protected final Set<State> explored;
    // 还未扩展的节点队列
    protected final Frontier frontier;

    public AbstractSearcher(Set<State> explored, Frontier frontier) {
        this.explored = explored;
        this.frontier = frontier;
    }

    @Override
    public abstract Deque<Node> search(Problem problem);

    /**
     *
     * @return	算法执行至此，已经被扩展的结点个数。
     */
    @Override
    public int nodesExpanded() {
        return explored.size();
    }

    /**
     *
     * @return 已经生成的结点的个数
     */
    @Override
    public int nodesGenerated(){
        return explored.size() + frontier.size();
    }
}