package stud.problem.npuzzle;

import java.util.EnumMap;
import java.util.List;

public enum Direction {

    N('↑'),  //北
    W('←'),  //西
    S('↓'),  //南
    E('→');  //东

    private final char symbol;

    Direction(char symbol){
        this.symbol = symbol;
    }
    public char getSymbol(){
        return symbol;
    }

    // 4个方向。
    public static final List<Direction> FOUR_DIRECTIONS = List.of(Direction.values());

    //位移量
    private static final EnumMap<Direction, int[]> DIRECTION_OFFSET = new EnumMap<>(Direction.class);
    public static final int SCALE = 1;

    static{
        DIRECTION_OFFSET.put(N, new int[]{-1, 0});
        DIRECTION_OFFSET.put(W, new int[]{0, -1});
        DIRECTION_OFFSET.put(S, new int[]{1, 0});
        DIRECTION_OFFSET.put(E, new int[]{0, 1});
   }

    public static int[] offset(Direction dir){
        return DIRECTION_OFFSET.get(dir);
    }

}
