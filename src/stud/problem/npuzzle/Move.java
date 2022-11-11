package stud.problem.npuzzle;

import core.problem.Action;

public class Move extends Action {

    public Move(Direction direction){  //���캯��
        this.direction =direction;
    }

    private static int count = 1;
    @Override
    public int stepCost() {
        return 1;  //cost�̶���1
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        Move another = (Move) obj;
            //����Node�����״̬��ͬ������Ϊ����ͬ��
        return this.direction.equals(another.direction);
    }
    @Override
    public void draw() {
        System.out.println(this);
    }

    private Direction direction;


    public Direction getDirection() {    //ȡ����
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public static int getCount() {    //ȡ��
        return count;
    }

    public static void setCount(int count) {  Move.count = count;
    }


}
