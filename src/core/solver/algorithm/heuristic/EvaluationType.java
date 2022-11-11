package core.solver.algorithm.heuristic;

/**
 * Best-First搜索的三类不同的估值策略
 * 同时，启发策略（h函数）又有多种可能。
 * 对所有问题都是通用的
 */
public enum EvaluationType {
    FULL,       // f = g + h;    Best First
    PATH_COST,  // f = g; h≡0;   Dijkstra
    HEURISTIC   // f = h;        Greedy Best First
}
