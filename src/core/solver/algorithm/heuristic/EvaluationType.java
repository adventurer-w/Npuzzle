package core.solver.algorithm.heuristic;

/**
 * Best-First���������಻ͬ�Ĺ�ֵ����
 * ͬʱ���������ԣ�h���������ж��ֿ��ܡ�
 * ���������ⶼ��ͨ�õ�
 */
public enum EvaluationType {
    FULL,       // f = g + h;    Best First
    PATH_COST,  // f = g; h��0;   Dijkstra
    HEURISTIC   // f = h;        Greedy Best First
}
