package core.solver.algorithm.heuristic;

import core.problem.State;

/**
 *  预测器接口
 *  根据当前状态和目标状态，给出当前状态到目标状态的耗散值的一个估计
 *
 */
public interface Predictor {
    /**
     * 根据目标状态，对当前状态进行启发式估值
     * @param
     *      state 被评估的状态
     *      goal  目标状态
     * @return 该状态到目标状态的启发值
     */
    int heuristics(State state, State goal);
}
