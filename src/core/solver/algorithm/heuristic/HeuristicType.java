package core.solver.algorithm.heuristic;

public enum HeuristicType {
    //Npuzzle的启发函数
    MISPLACED,  // 不在位将牌
    MANHATTAN,  // 曼哈顿距离
    LINEAR_CONFLICT,  // 线性冲突 （曼哈顿距离 + 2 * 线性冲突）
    DISJOINT_PATTERN,

    //PathFinding的启发函数 (8方向的情况)
    PF_EUCLID,      // 欧几里得距离
    PF_MANHATTAN,   // 8方向移动时，不是admissible的
    PF_GRID,        // 尽可能走对角线，然后平行走，>= EUCLID, 扩展结点少

    //野人传教士问题
    MC_HARMONY  //去掉野人会吃人的约束

}
