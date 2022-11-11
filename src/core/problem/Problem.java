package core.problem;

import core.solver.algorithm.heuristic.Predictor;
import core.solver.queue.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 所有问题的抽象超类
 * initialState
 * goal
 */
public abstract class Problem {
    //成员变量
    protected State initialState;
    protected State goal;
    protected int size;     // 问题的规模：15-puzzle为4，Puzzle的边长；
    // 寻路问题为Grid的边长；
    // 野人传教士为野人与传教士的人数

    public Problem(State initialState, State goal) {
        this.initialState = initialState;
        this.goal = goal;
    }

    public Problem(State initialState, State goal, int size) {
        this(initialState, goal);
        this.size = size;
    }

    /**
     * 当前问题是否有解
     * @return 有解，true; 无解，false
     *
     */
    public abstract boolean solvable();

    /**
     * 从初始状态产生搜索树的根节点
     * @return 当前问题的根结点
     */
    public final Node root(Predictor predictor){
        //使用predictor对state估值
        int heuristics = predictor.heuristics(initialState, goal);
        return root().setHeuristic(heuristics);
    }
    public final Node root(){
        return new Node(initialState, null, null, 0);
    }
    /**
     * 生成node的所有合法的后继结点
     * @param parent      父结点
     *
     * @return  parent结点的所有子结点
     */
    public final List<Node> childNodes(Node parent) {
        List<Node> nodes = new ArrayList<>();
        //父结点的状态
        State parentState = parent.getState();
        //对于parentState上所有可能的action，但不是所有的action都可行
        for (var action : parentState.actions()){
            //如果父结点状态下的动作是可行的
            if (applicable(parentState, action)){
                //得到后继状态
                State state = parentState.next(action);
                //计算路径长度 = 父结点路径长度 + 进入后继状态所采取的动作的代价
                int pathCost = parent.getPathCost() + stepCost(state, action);
                //生成子结点
                Node child = new Node(state, parent, action, pathCost);
                nodes.add(child);
            }
        }
        return nodes;
    }

    public final List<Node> childNodes(Node parent, Predictor predictor) {
        List<Node> nodes = new ArrayList<>();
        for (var node : childNodes(parent)) {
            //使用predictor对state估值
            int heuristics = predictor.heuristics(node.getState(), goal);
            nodes.add(node.setHeuristic(heuristics));
        }
        return nodes;
    }

    /**
     *
     * @param state     当前状态
     * @param action    进入当前状态所采取的Action
     * @return          进入当前状态的代价
     */
    public abstract int stepCost(State state, Action action);

    /**
     * 在状态state上的action是否可用？
     * @param state     当前状态
     * @param action    当前状态下所采用的动作
     * @return  true：可用；false：不可用
     */
    public abstract boolean applicable(State state, Action action);

    /**
     * 判断某个状态state是否到达目标状态，多数情况下是判断跟目标状态是否相等。
     *
     * @param state 要判断的状态
     * @return  true：要判断的状态已经是目标状态；否则，false
     */
    public boolean goal(State state){
        return state.equals(goal);
    }

    /**
     * 解路径的可视化
     * @param path 解路径
     */
    public abstract void showSolution(Deque<Node> path) throws IOException;
}
