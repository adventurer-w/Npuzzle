package stud.queue;

import core.problem.State;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class myFrontier  extends PriorityQueue<Node> implements Frontier {



//    private final TreeMap<Integer, Node> hashMap = new TreeMap<>();


    private final Comparator<Node> evaluator;
    public myFrontier(Comparator<Node> evaluator) {
        super(evaluator);
        this.evaluator = evaluator;
    }

    @Override
    public boolean contains(Node node) {
        return getNode(node.getState()) != null;
    }
    private boolean checkReplace(Node oldNode, Node node) {
        if (evaluator.compare(oldNode, node) > 0) {
            hashMap.put((oldNode.getState()).hashCode(),node);
            super.offer(node);
            return true;
        }
        return false;
    }

    private final HashMap<Integer, Node> hashMap = new HashMap<>();

    private Node getNode(State state) {
        return hashMap.get(state.hashCode());
    }
    @Override
    public Node poll() {
        Node node = super.poll();
        hashMap.remove((node.getState()).hashCode());
        return node;
    }

    @Override
    public boolean offer(Node node) {
        Node oldNode = getNode(node.getState());
        if (oldNode == null) {
            super.offer(node);
            hashMap.put((node.getState()).hashCode(), node);
            return true;
        } else {
            return checkReplace(oldNode, node);
        }
    }

}
