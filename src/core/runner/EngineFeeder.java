package core.runner;

import core.problem.Problem;
import core.problem.State;
import core.solver.algorithm.Searcher;
import core.solver.algorithm.heuristic.BestFirstSearch;
import core.solver.algorithm.heuristic.EvaluationType;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.queue.Frontier;
import core.solver.queue.Node;
import stud.queue.myFrontier;
import stud.solver.IDAStar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Ϊ�����㷨�ṩ�����زġ�����
 *    ���⣺problem��
 *    ʹ�õ�Frontier��
 *    ʹ�õĹ�ֵ���� h����������ʽ����
 *
 */
public abstract class EngineFeeder {
    /**
     * ���ݴ�����������������ı��ļ������ݣ���������ʵ���б�
     * @param problemLines  �ַ������飬��ŵ��ǣ����������������ı��ļ�������
     * @return
     */
    public abstract ArrayList<Problem> getProblems(ArrayList<String> problemLines);

    /**
     * ���ɲ�ȡĳ�ֹ�ֵ���Ƶ�Frontier
     *
     * @param type ���������������
     * @return ʹ���������Ƶ�һ��Frontierʵ��
     */
    public Frontier getFrontier(EvaluationType type) {
        return new myFrontier(Node.evaluator(type));
//        return new ListFrontier(Node.evaluator(type));
//        return new ZobristFrontier(Node.evaluator(type));
    }
    /**
     * ��ö�״̬���й�ֵ��Predictor
     *
     * @param type ��ֵ����������
     * @return ��������
     */
    public abstract Predictor getPredictor(HeuristicType type);

    /**
     * ����IdaStar������һ��ʵ��
     */
    public Searcher getIdaStar(HeuristicType type) {
        return new IDAStar(getPredictor(type));
//        return new myIDAStar(getPredictor(type));
    }

    /**
     * �������Ա�ʵ���AStar, ���������ⶼ��һ����
     * ������ʹ�ò�ͬ����������
     * @param type �����õ�������������
     */
    public final Searcher getAStar(HeuristicType type) {
        // ��ȡFrontier����Node��g(n)+h(n)���������У���ͬʱ������g(n)����������
        Frontier frontier = getFrontier(EvaluationType.FULL);
        // ��HashSet��ΪExplored��
        Set<State> explored = new HashSet<>();
        // ����explored��frontier����AStar���棬��ʹ������Ϊtype����������
        return new BestFirstSearch(explored, frontier, getPredictor(type));
    }

    /**
     * �������Ա�ʵ���Dijkstra�������е����ⶼ��һ����
     *
     * @return Dijkstra�����㷨
     */
    public final Searcher getDijkstra() {
        // ��ȡFrontier����Node��g(n)����������
        Frontier frontier = getFrontier(EvaluationType.PATH_COST);
        // ��HashSet��ΪExplored��
        Set<State> explored = new HashSet<>();
        // predictor��h(n)��0����Dijkstra�㷨
        return new BestFirstSearch(explored, frontier, (state, goal) -> 0);
    }
}
