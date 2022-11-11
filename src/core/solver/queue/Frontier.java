package core.solver.queue;

/**
 * Frontier接口
 */
public interface Frontier {
    /**
     * 取出Frontier中的第一个元素
     * @return
     */
    Node poll();

    /**
     * 清空Frontier
     */
    void clear();

    /**
     * Frontier中元素的个数
     * @return
     */
    int size();

    /**
     * Frontier是否为空
     *
     */
    boolean isEmpty();

    /**
     * Frontier中是否含有结点node
     * @param node
     * @return
     */
    boolean contains(Node node);

    /***
     * 在Frontier中插入结点node。
     * 如果插入的结点不在frontier中，则直接插入
     * 如果Frontier中已经存在相同状态的其他结点，则舍弃掉二者之中不好的。
     * @param node 要插入的结点
     * @return
     */
    boolean offer(Node node);
}
