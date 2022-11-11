package stud.problem.npuzzle;

import core.problem.Action;
import core.problem.Problem;
import core.problem.State;
import core.solver.queue.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;

public class NPuzzle extends Problem {

    public NPuzzle(State initialState, State goal, int size) {
        super(initialState, goal, size);
    }

    private int getReverseCount(byte[][] state){
        int res=0;
        for(int i=0;i<size*size-1;i++){
            for(int j=i+1;j<size*size;j++){
                int x1=i/size,y1=i%size,x2=j/size,y2=j%size;
                if(state[x2][y2]==0) continue;
                if(state[x2][y2]<state[x1][y1]) res++;
            }
        }
        return res;
    }

    public State getInitialState() {
        return initialState;
    }

    @Override
    public boolean solvable() {  //判断是否可解
        byte[][] state = ((NPuzzleState)getInitialState()).getStates();
        int count = getReverseCount(state);
        if(size % 2 == 1) { //size为奇数,逆序对为偶数才有解
            return (count % 2 == 0);
        } else { //size为偶数
            if((size - ((NPuzzleState)getInitialState()).getRow()) % 2 == 1) {  //空格在奇数行
                return (count % 2 == 0);
            } else { //空格在偶数行
                return (count % 2 == 1);
            }
        }
    }

    @Override
    public int stepCost(State state, Action action) {
        return 1;
    }

    @Override
    public boolean applicable(State state, Action action) {
        int[] offsets = Direction.offset(((Move)action).getDirection());
        int row = ((NPuzzleState)state).getRow() + offsets[0];
        int col = ((NPuzzleState)state).getCol() + offsets[1];
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    @Override
    public void showSolution(Deque<Node> path) throws IOException {
        FileWriter f=new FileWriter("resources/data.txt",true);
        NPuzzleState x= (NPuzzleState)path.getFirst().getState();
        PrintWriter pw = new PrintWriter(f);
        NPuzzleState nw;

        pw.print(path.size()+" ");
            for(var temp:path){
                nw = (NPuzzleState)temp.getState();
                int bx = x.getRow()-nw.getRow(),by = x.getCol() - nw.getCol();
                if(bx==0&&by==1){ //下
                    pw.print("1 ");
                }else if(bx==0&&by==-1){ //上
                    pw.print("3 ");
                }else if(bx==1&&by==0){ //左
                    pw.print("0 ");
                }else if(bx==-1&&by==0){ //右
                    pw.print("2 ");
                }
                x=nw;
            }
        pw.println();
        f.flush();
        f.close();
        pw.flush();
        pw.close();
    }
}
