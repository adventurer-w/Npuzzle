package core.problem;

import core.solver.algorithm.heuristic.Predictor;
import core.solver.queue.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * ��������ĳ�����
 * initialState
 * goal
 */
public abstract class Problem {
    //��Ա����
    protected State initialState;
    protected State goal;
    protected int size;     // ����Ĺ�ģ��15-puzzleΪ4��Puzzle�ı߳���
    // Ѱ·����ΪGrid�ı߳���
    // Ұ�˴���ʿΪҰ���봫��ʿ������

    public Problem(State initialState, State goal) {
        this.initialState = initialState;
        this.goal = goal;
    }

    public Problem(State initialState, State goal, int size) {
        this(initialState, goal);
        this.size = size;
    }

    /**
     * ��ǰ�����Ƿ��н�
     * @return �н⣬true; �޽⣬false
     *
     */
    public abstract boolean solvable();

    /**
     * �ӳ�ʼ״̬�����������ĸ��ڵ�
     * @return ��ǰ����ĸ����
     */
    public final Node root(Predictor predictor){
        //ʹ��predictor��state��ֵ
        int heuristics = predictor.heuristics(initialState, goal);
        return root().setHeuristic(heuristics);
    }
    public final Node root(){
        return new Node(initialState, null, null, 0);
    }
    /**
     * ����node�����кϷ��ĺ�̽��
     * @param parent      �����
     *
     * @return  parent���������ӽ��
     */
    public final List<Node> childNodes(Node parent) {
        List<Node> nodes = new ArrayList<>();
        //������״̬
        State parentState = parent.getState();
        //����parentState�����п��ܵ�action�����������е�action������
        for (var action : parentState.actions()){
            //��������״̬�µĶ����ǿ��е�
            if (applicable(parentState, action)){
                //�õ����״̬
                State state = parentState.next(action);
                //����·������ = �����·������ + ������״̬����ȡ�Ķ����Ĵ���
                int pathCost = parent.getPathCost() + stepCost(state, action);
                //�����ӽ��
                Node child = new Node(state, parent, action, pathCost);
                nodes.add(child);
            }
        }
        return nodes;
    }

    public final List<Node> childNodes(Node parent, Predictor predictor) {
        List<Node> nodes = new ArrayList<>();
        for (var node : childNodes(parent)) {
            //ʹ��predictor��state��ֵ
            int heuristics = predictor.heuristics(node.getState(), goal);
            nodes.add(node.setHeuristic(heuristics));
        }
        return nodes;
    }

    /**
     *
     * @param state     ��ǰ״̬
     * @param action    ���뵱ǰ״̬����ȡ��Action
     * @return          ���뵱ǰ״̬�Ĵ���
     */
    public abstract int stepCost(State state, Action action);

    /**
     * ��״̬state�ϵ�action�Ƿ���ã�
     * @param state     ��ǰ״̬
     * @param action    ��ǰ״̬�������õĶ���
     * @return  true�����ã�false��������
     */
    public abstract boolean applicable(State state, Action action);

    /**
     * �ж�ĳ��״̬state�Ƿ񵽴�Ŀ��״̬��������������жϸ�Ŀ��״̬�Ƿ���ȡ�
     *
     * @param state Ҫ�жϵ�״̬
     * @return  true��Ҫ�жϵ�״̬�Ѿ���Ŀ��״̬������false
     */
    public boolean goal(State state){
        return state.equals(goal);
    }

    /**
     * ��·���Ŀ��ӻ�
     * @param path ��·��
     */
    public abstract void showSolution(Deque<Node> path) throws IOException;
}
