package stud.problem.npuzzle;


import core.problem.Action;
import core.problem.State;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

import static core.solver.algorithm.heuristic.HeuristicType.*;

/**
 * Npuzzle问题的状态
 * 表示一种格局
 */

public class NPuzzleState extends State {

    public int size ; //大小
    public byte[][] states; //局面
    private int col = 0; //空格的位置
    private int row = 0;
    private int hash = 0; //格局hash值
    private int manhattanDistance  = 0; //曼哈顿距离
    private int misplacedDistance = 0;//错位数
    private static final EnumMap<HeuristicType, Predictor> predictors = new EnumMap<>(HeuristicType.class);

    public static  ArrayList<Block> blocks_663= new ArrayList<>() { //不相交数据库
        {
            add(new Block(1,5,6,9,10,13));
            add(new Block(7,8,11,12,14,15));
            add(new Block(2,3,4));

        }
    };



    public NPuzzleState(NPuzzleState state) {
        this.size = state.getSize();
        this.states = new byte[size][];
        for (int i = 0; i < state.getSize(); i++) {
            this.states[i] = new byte[size];
            for (int j = 0; j < state.getSize(); j++) {
                this.states[i][j] = state.getStates()[i][j];
            }
        }
    }


    public NPuzzleState(int size, byte[] board,boolean isRoot) {
        this.size = size;
        this.states = new byte[size][];
        for (int i = 0; i < size; i++) {
            this.states[i] = new byte[size];
            for (int j = 0; j < size; j++) {
                this.states[i][j] = board[i * size + j];
                if (this.states[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        //只有根节点需要计算曼哈顿距离、错位数，后续动态更新即可
        if (isRoot) {
            //曼哈顿
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (states[i][j] == 0) continue;
                    manhattanDistance += Math.abs((states[i][j] - 1) / size - i) + Math.abs((states[i][j] - 1) % size - j);
                }
            }
            //错位
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (states[i][j] == 0 && i * size + j + 1 == size * size)continue;
                    if (states[i][j] != i * size + j + 1) misplacedDistance++;
                }
            }
        }
        //zobristHash
        for (int i = 0; i < size * size; i++) {
            if (states[i / size][i % size] != 0) {
                if(size==4){ hash ^= Zobrist.zobristHash4[i][states[i / size][i % size]]; }
                else { hash ^= Zobrist.zobristHash3[i][states[i / size][i % size]]; }
            }
        }
    }

    static {
        predictors.put(LINEAR_CONFLICT, (state, goal) -> ((NPuzzleState) state).getLinearConflictDistance());
        predictors.put(MANHATTAN, (state, goal) -> ((NPuzzleState) state).getManhattanDistance());
        predictors.put(MISPLACED, (state, goal) -> ((NPuzzleState) state).getMisplacedDistance());
        predictors.put(DISJOINT_PATTERN, (state, goal) -> ((NPuzzleState) state).getDataBaseDistance663());
    }


    public static Predictor predictor(HeuristicType type) {
        return predictors.get(type);
    }

    public int getSize() { return size; }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }

    public int getManhattanDistance() {
        return manhattanDistance;
    }

    public void setManhattanDistance(int manhattanDistance) {
        this.manhattanDistance = manhattanDistance;
    }



//
    public int getMisplacedDistance() {
        return misplacedDistance;
    }
    public void setMisplacedDistance(int misplacedDistance) {
        this.misplacedDistance = misplacedDistance;
    }

    public int getDataBaseDistance663() {
        return Math.max(DataBaseCreate.getDistance(states,blocks_663),getLinearConflictDistance());
//        return DataBaseCreate.getDistance(states,blocks_663);
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
    public int getHash() {
        return hash;
    }
    public byte[][] getStates() {
        return states;
    }


    @Override
    public void draw() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (states[i][j] != 0) {
                    System.out.print(states[i][j] + " ");
                } else {
                    System.out.print("# ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public State next(Action action) {
        //得到当前action的位移量，由此得到新的行列
        Direction direction = ((Move) action).getDirection();
        int[] offsets = Direction.offset(direction);
        int newRow = row + offsets[0];
        int newCol = col + offsets[1];

        //得到新格局
        NPuzzleState newState = new NPuzzleState(this);
        byte val = states[newRow][newCol];
        newState.states[row][col] = val;
        newState.states[newRow][newCol] = 0;

        newState.setCol(newCol); newState.setRow(newRow);

        //动态修改曼哈顿距离、错位数
        int old = Math.abs((val - 1) / size - newRow) + Math.abs((val - 1) % size - newCol);
        int nw = Math.abs((val - 1) / size - row) + Math.abs((val - 1) % size - col);
        newState.setManhattanDistance(manhattanDistance - old + nw);

        int v=0;
        if(row * size +col +1 == val) v--;
        if(row * size +col +1 == 9) v++;
        if(newRow * size + newCol +1 == val) v++;
        if(newRow * size + newCol +1 == 9) v--;
        newState.setMisplacedDistance(misplacedDistance+v);

        if(size==4) {
            newState.setHash(hash ^ Zobrist.zobristHash4[newRow * size + newCol][val] ^ Zobrist.zobristHash4[row * size + col][val]);
        }else {
            newState.setHash(hash ^ Zobrist.zobristHash3[newRow * size + newCol][val] ^ Zobrist.zobristHash3[row * size + col][val]);
        }

        return newState;
    }

    public int getLinearConflictDistance() {
        return manhattanDistance+2*nLinearConflicts();
    }

    public int nLinearConflicts(){
        int conflicts = 0;
        int[] pR = new int[size*size + 1];
        int[] pC = new int[size*size + 1];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                pR[states[r][c]] = r;
                pC[states[r][c]] = c;
            }
        }

        for (int r = 0; r < size; r++) {
            for (int cl = 0; cl < size; cl++) {
                for (int cr = cl + 1; cr < size; cr++) {
                    if ( (r*size + cl +1)!=0 && (r*size + cr +1)!=0 && r == pR[(r*size + cl +1)]
                            && pR[(r*size + cl +1)] == pR[(r*size + cr +1)] &&
                            pC[(r*size + cl +1)] > pC[(r*size + cr +1)]) {
                        conflicts++;
                    }
                    if ((cl*size +  r +1)!=0 && (cr*size +  r +1)!=0 &&  r == pC[(cl*size +  r +1)]
                            && pC[(cl*size +  r +1)] == pC[(cr*size +  r +1)] &&
                            pR[(cl*size +  r +1)] > pR[(cr*size + r +1)]) {
                        conflicts++;
                    }
                }
            }
        }
        return conflicts;
    }

    @Override
    public Iterable<? extends Action> actions() {
        Collection<Move> moves = new ArrayList<>();
        for (Direction d : Direction.FOUR_DIRECTIONS)
            moves.add(new Move(d));
        return moves;
    }

    @Override
    public int hashCode() {
        return hash;
    }


    @Override
    public boolean equals(Object obj) {                    //用于比较两个棋盘是否相同
        NPuzzleState another = (NPuzzleState) obj;
        for(int i=0;i<this.size;i++)
            for(int j=0;j<this.size;j++)
                if(this.states[i][j]!=another.states[i][j])
                    return false;
        return true;
    }

}
