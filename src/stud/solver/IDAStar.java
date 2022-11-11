package stud.solver;

import algs4.util.StopwatchCPU;
import core.problem.Problem;
import core.solver.algorithm.Searcher;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.queue.Node;

import java.util.*;

/**
 * 迭代加深的A*算法，需要同学们自己编写完成
 */
public class IDAStar implements Searcher {
    Predictor predictor;
    private final Set<Integer> explored = new HashSet<>();
    private final Set<Integer> expanded = new HashSet<>();
    Problem problem;
    Deque<Node> path = new ArrayDeque<>();
    private Deque<Node> result;


    public IDAStar(Predictor predictor) {
        this.predictor = predictor;
    }
    @Override
    public Deque<Node> search(Problem p) {
        problem = p;
        if (!problem.solvable())  return null;
        Node root = problem.root(predictor);
        int depth = root.getHeuristic();
        explored.clear();
        expanded.clear();
        while (!dfs(root, null, depth))
        {
            depth++;
        }
        return path;
    }

    public boolean dfs(Node node,Node fa,int  depth){
        if (node.getPathCost() >= depth)  return false;
        if (problem.goal(node.getState())) //找到了
        {
            path = generatePath(node);
            return true;
        }
        expanded.add(node.getState().hashCode());
        List<Node> childNodes = problem.childNodes(node, predictor);
        for (var child : childNodes){
            explored.add(child.getState().hashCode());
            if (!(fa == null || !child.getState().equals(fa.getState())))  continue;
            if (child.evaluation() < depth && dfs(child, node, depth))  return true;
        }
        return false;
    }

    @Override
    public int nodesExpanded() { return expanded.size(); }

    @Override
    public int nodesGenerated() { return explored.size(); }
}
