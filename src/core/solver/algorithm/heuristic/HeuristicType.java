package core.solver.algorithm.heuristic;

public enum HeuristicType {
    //Npuzzle����������
    MISPLACED,  // ����λ����
    MANHATTAN,  // �����پ���
    LINEAR_CONFLICT,  // ���Գ�ͻ �������پ��� + 2 * ���Գ�ͻ��
    DISJOINT_PATTERN,

    //PathFinding���������� (8��������)
    PF_EUCLID,      // ŷ����þ���
    PF_MANHATTAN,   // 8�����ƶ�ʱ������admissible��
    PF_GRID,        // �������߶Խ��ߣ�Ȼ��ƽ���ߣ�>= EUCLID, ��չ�����

    //Ұ�˴���ʿ����
    MC_HARMONY  //ȥ��Ұ�˻���˵�Լ��

}
