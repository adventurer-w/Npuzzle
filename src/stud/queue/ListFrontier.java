package stud.queue;

import core.problem.State;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 虽然性能不高，但可以用于求解所有问题。
 * 请同学们参照编写自己的Frontier类。
 */

public class ListFrontier extends ArrayList<Node> implements Frontier {
    // 节点优先级比较器，在Node类中定义了三个不同的比较器（
    //      Dijkstra,
    //      Greedy Best-First,
    //      Best-First
    // evaluator决定了优先取出Frontier中的哪个元素。

    private final Comparator<Node> evaluator;

    public ListFrontier(Comparator<Node> evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public Node poll() {
        return super.remove(0);
    }

    /**
     *
     * @param node
     * @return
     */
    @Override
    public boolean contains(Node node) {
        return getNode(node.getState()) != null;
    }
    /**
     * 将结点node插入到优先队列中，
     * 如果Frontier中已经存在与node状态相同的结点，则舍弃掉二者中估值更大的结点
     * @param node 要插入优先队列的结点
     * @return
     */
    @Override
    public boolean offer(Node node) {
        Node oldNode = getNode(node.getState());
        if (oldNode == null) {
            //按照f值排序时，node应该在的位置
            super.add(getIndex(node), node);
            return true;
        } else { //child已经在Frontier中
            return discardOrReplace(oldNode, node);
        }
    }

    /**
     *
     * 如果Frontier中已经存在与node状态相同的结点， 则舍弃掉二者之间不好的那一个。
     *
     *
     * @param oldNode
     * @param node 结点，其状态要么已经出现在Explored中，要么已经出现在Frontier中
     * @return true: replaced; false: discarded
     *
     */
    private boolean discardOrReplace(Node oldNode, Node node) {
        // 如果旧结点的估值比新的大，即新生成的结点更好
        if (evaluator.compare(oldNode, node) > 0) {
            // 用新节点替换旧节点
            replace(oldNode, node);
            return true;
        }
        return false;   //discard，扔掉新结点
    }

    private Node getNode(State state) {
        for (var node : this){
            if (node.getState().equals(state)){
                return node;
            }
        }
        return null;
    }

    private int getIndex(Node node) {
        int index = Collections.binarySearch(this, node, evaluator);
        if (index < 0) index = -(index + 1);
        return index;
    }

    /**
     * 用节点 e 替换掉具有相同状态的旧节点 oldNode 同学们可能需要改写这个函数！
     *
     * @param oldNode 被替换的结点
     * @param newNode 新结点
     */
    private void replace(Node oldNode, Node newNode) {
        super.remove(oldNode);
        super.add(getIndex(newNode), newNode);
    }
}
