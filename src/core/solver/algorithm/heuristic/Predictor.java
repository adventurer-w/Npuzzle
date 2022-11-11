package core.solver.algorithm.heuristic;

import core.problem.State;

/**
 *  Ԥ�����ӿ�
 *  ���ݵ�ǰ״̬��Ŀ��״̬��������ǰ״̬��Ŀ��״̬�ĺ�ɢֵ��һ������
 *
 */
public interface Predictor {
    /**
     * ����Ŀ��״̬���Ե�ǰ״̬��������ʽ��ֵ
     * @param
     *      state ��������״̬
     *      goal  Ŀ��״̬
     * @return ��״̬��Ŀ��״̬������ֵ
     */
    int heuristics(State state, State goal);
}
