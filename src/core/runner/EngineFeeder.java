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
 * 为搜索算法提供各样素材。包括
 *    问题：problem，
 *    使用的Frontier，
 *    使用的估值函数 h函数，启发式函数
 *
 */
public abstract class EngineFeeder {
    /**
     * 根据存放问题输入样例的文本文件的内容，生成问题实例列表
     * @param problemLines  字符串数组，存放的是：问题输入样例的文本文件的内容
     * @return
     */
    public abstract ArrayList<Problem> getProblems(ArrayList<String> problemLines);

    /**
     * 生成采取某种估值机制的Frontier
     *
     * @param type 结点评估器的类型
     * @return 使用评估机制的一个Frontier实例
     */
    public Frontier getFrontier(EvaluationType type) {
        return new myFrontier(Node.evaluator(type));
//        return new ListFrontier(Node.evaluator(type));
//        return new ZobristFrontier(Node.evaluator(type));
    }
    /**
     * 获得对状态进行估值的Predictor
     *
     * @param type 估值函数的类型
     * @return 启发函数
     */
    public abstract Predictor getPredictor(HeuristicType type);

    /**
     * 生成IdaStar搜索的一个实例
     */
    public Searcher getIdaStar(HeuristicType type) {
        return new IDAStar(getPredictor(type));
//        return new myIDAStar(getPredictor(type));
    }

    /**
     * 用来做对比实验的AStar, 对所有问题都是一样的
     * 可配置使用不同的启发函数
     * @param type 可配置的启发函数类型
     */
    public final Searcher getAStar(HeuristicType type) {
        // 获取Frontier，其Node以g(n)+h(n)的升序排列，相同时，按照g(n)的升序排列
        Frontier frontier = getFrontier(EvaluationType.FULL);
        // 以HashSet作为Explored表
        Set<State> explored = new HashSet<>();
        // 根据explored和frontier生成AStar引擎，并使用类型为type的启发函数
        return new BestFirstSearch(explored, frontier, getPredictor(type));
    }

    /**
     * 用来做对比实验的Dijkstra，对所有的问题都是一样的
     *
     * @return Dijkstra搜索算法
     */
    public final Searcher getDijkstra() {
        // 获取Frontier，其Node以g(n)的升序排列
        Frontier frontier = getFrontier(EvaluationType.PATH_COST);
        // 以HashSet作为Explored表
        Set<State> explored = new HashSet<>();
        // predictor：h(n)≡0，即Dijkstra算法
        return new BestFirstSearch(explored, frontier, (state, goal) -> 0);
    }
}
