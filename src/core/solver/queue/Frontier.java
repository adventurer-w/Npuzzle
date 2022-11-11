package core.solver.queue;

/**
 * Frontier�ӿ�
 */
public interface Frontier {
    /**
     * ȡ��Frontier�еĵ�һ��Ԫ��
     * @return
     */
    Node poll();

    /**
     * ���Frontier
     */
    void clear();

    /**
     * Frontier��Ԫ�صĸ���
     * @return
     */
    int size();

    /**
     * Frontier�Ƿ�Ϊ��
     *
     */
    boolean isEmpty();

    /**
     * Frontier���Ƿ��н��node
     * @param node
     * @return
     */
    boolean contains(Node node);

    /***
     * ��Frontier�в�����node��
     * �������Ľ�㲻��frontier�У���ֱ�Ӳ���
     * ���Frontier���Ѿ�������ͬ״̬��������㣬������������֮�в��õġ�
     * @param node Ҫ����Ľ��
     * @return
     */
    boolean offer(Node node);
}
