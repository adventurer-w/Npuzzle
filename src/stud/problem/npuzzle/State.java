package stud.problem.npuzzle;

import java.util.ArrayList;

public class State {
    // 存储一个block的状态
    private int state;

    // 状态存储的滑块个数
    private int num;

    public State() {
        state = 0;
    }

    public State(int state, int num) {
        this.state = state;
        this.num = num;
    }

    public State(State a){
        this.state = a.state;
        this.num = a.num;
    }

    public void setNum(int num) {
        this.num =num;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getNum(){
        return this.num;
    }
    public int getState(){
        return this.state;
    }
    public static State slidesToState(ArrayList<Integer> slides) {
        int res = 0;
        for (int i = 0; i < 4 * slides.size(); i++) {
            int j = slides.size() - i / 4 - 1;
            if (((slides.get(j) - 1) >> (i % 4) & 1) == 1) {
                res |= 1 << i;
            }
        }
        return new State(res,slides.size());
    }

    public int stateToSlide(int index) {
        int res = (this.state >> ((this.num - index - 1) * 4)) & 15;
        return res + 1;
    }

    public  void setStateSlide(int index,int value){
        value--;
        int base = this.num - index - 1;
        for(int i = 0; i < 4;i++){
            if(((value>>i)&1)==1){
                this.state |= 1<<(base*4+i);
            }else{
                this.state &=~(1<<(base*4+i));
            }
        }
    }

    public void slidePrint(){
        for(int i = 0; i < this.num; i++){
            System.out.print(this.stateToSlide(i)+" ");
        }
        System.out.println();
    }
}
