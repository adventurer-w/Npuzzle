package stud.problem.npuzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class Block {

    public static int num = 0;

    public int order;

    public static ArrayList<Integer> belongs;

    static {
        belongs = new ArrayList<Integer>();
        for (int i = 0; i < 4 * 4; i++) {
            belongs.add(0);
        }
    }

    private final int size;
    private final ArrayList<Integer> slides;
    private final HashMap<Integer, Integer> Index;

    public int getSize() {
        return size;
    }

    public int getSlide(int index) {
        return slides.get(index);
    }

    public int getIndex(int slide) {
        return Index.get(slide);
    }

    public ArrayList<Integer> getSlides() {
        return new ArrayList<>(this.slides);
    }

    public boolean isSlideIn(int slide) {
        return Index.containsKey(slide);
    }


    public void Print() {
        for (int i = 0; i < size; i++) {
            System.out.print(slides.get(i) + " ");
        }
    }

    public Block(int... slides) {
        this.order = Block.num;
        Block.num++;
        size = slides.length;
        this.slides = new ArrayList<>();
        this.Index = new HashMap<>();

        for (int i = 0; i < size; i++) {
            this.slides.add(slides[i]);
            this.Index.put(slides[i], i);
            Block.belongs.set(slides[i], this.order);
        }

    }
}
