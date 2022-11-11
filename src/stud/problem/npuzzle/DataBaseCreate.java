package stud.problem.npuzzle;

import java.io.*;
import java.util.*;

public class DataBaseCreate {
    // 移动增量
    public final static int[] dx = {0, 1, 0, -1};
    public final static int[] dy = {1, 0, -1, 0};


    // 问题规模以及每一位滑块存储所需的比特数
    public final static int Size = 4;

    public static TreeMap<Integer, Integer> stateCost;
    public static ArrayList<ArrayList<Integer>> Table = new ArrayList<>() {{
            add(DataBaseCreate.readFromFile("resources/663model_0.txt"));
            add(DataBaseCreate.readFromFile("resources/663model_1.txt"));
            add(DataBaseCreate.readFromFile("resources/663model_2.txt"));
            System.out.println("数据库读取成功");
        }};

    public static ArrayList<Integer> readFromFile(String filePath) {
        Scanner fileIn;
        try {
            fileIn = new Scanner(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int state, cost;

        ArrayList<Integer> tmp = new ArrayList<>();

        while (fileIn.hasNext()) {
            state = fileIn.nextInt();
            cost = fileIn.nextInt();
            while (tmp.size() < state) {
                tmp.add(0);
            }
            tmp.add(cost);
        }
        return tmp;
    }
    public static void create(String filePreName, Block... blocks) {
        int i = 0;
        for (Block b : blocks) {
            System.out.println("this the " + i + " block");
            BreadedFirstSearch(b);
            DataBaseCreate.writeToFile(filePreName, i++);
        }
    }



    // 从NPuzzleState的状态获取距离 state格局(大小4*4)
    public static int getDistance(byte[][] state, ArrayList<Block> blocks) {
        int distance = 0, index, value, blockIndex;
        ArrayList<State> states = new ArrayList<>();
        for (int i = 0; i < blocks.size(); i++) {
            states.add(State.slidesToState(blocks.get(i).getSlides()));
        }
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                if (state[i][j] == 0) continue;
                blockIndex = Block.belongs.get(state[i][j]);
                index = blocks.get(blockIndex).getIndex(state[i][j]);
                value = i * Size + j + 1;
                states.get(blockIndex).setStateSlide(index, value);
            }
        }
        for (int i = 0; i < blocks.size(); i++) {
            distance += Table.get(i).get(states.get(i).getState());
        }
        return distance;
    }

    private static int makeMove(State state, int dx, int dy) {
        // 当前空格index
        int blankIndex = state.stateToSlide(state.getNum() - 1);
        // 当前空格的位置
        int blankX = (blankIndex - 1) / Size;
        int blankY = (blankIndex - 1) % Size;
        // 目标空格的位置
        int newX = blankX + dx;
        int newY = blankY + dy;
        // 越界返回-1
        if (newX < 0 || newX >= Size || newY < 0 || newY >= Size) {
            return -1;
        }
        // 目标空格index
        int newIndex = newX * Size + newY + 1;

        State newState = new State(state);

        // 记录cost是否增加
        int costInc = 0;
        for (int i = 0; i < state.getNum(); i++) {
            int x = (state.stateToSlide(i) - 1) / Size;
            int y = (state.stateToSlide(i) - 1) % Size;

            // 目标滑块为block中的滑块，则需要修改状态中的滑块
            if (x == newX && y == newY) {
                newState.setStateSlide(i, blankIndex);
                costInc = 1;
                break;
            }
        }
        newState.setStateSlide(state.getNum() - 1, newIndex);

        int newCost = stateCost.get(state.getState()) + costInc;
        // 如果已经访问过则直接返回-1
        if (stateCost.containsKey(newState.getState())) {
            return -1;
        }
        stateCost.put(newState.getState(), newCost);
        //更新当前状态
        state.setState(newState.getState());
        return costInc;
    }

    private static void BreadedFirstSearch(Block block) {
        stateCost = new TreeMap<>();
        Deque<Integer> deq = new LinkedList<>();
        ArrayList<Integer> slides = block.getSlides();
        // 增加空格位置
        slides.add(16);
        State tmp = State.slidesToState(slides);

        // 将初始状态存入
        stateCost.put(tmp.getState(), 0);
        deq.addLast(tmp.getState());

        int stateNow, exitFlag, epoch = 0;
        while (!deq.isEmpty()) {
            epoch++;
            if (epoch % 1000000 == 0)
                System.out.println("epoch:" + epoch);

            stateNow = deq.getFirst();
            deq.removeFirst();
            for (int i = 0; i < 4; i++) {
                tmp.setState(stateNow);
                exitFlag = makeMove(tmp, dx[i], dy[i]);

                if (exitFlag == 0) {
                    deq.addFirst(tmp.getState());
                } else if (exitFlag == 1) {
                    deq.addLast(tmp.getState());
                }
            }
        }
        System.out.println("finished all epoch is " + epoch);
    }

    private static void writeToFile(String filePreName, int index) {
        TreeMap<Integer, Integer> tmp = new TreeMap<>();
        String path = "./" + filePreName + "_" + index + ".txt";
        File file = new File(path);
        FileOutputStream fileOut;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOut = new FileOutputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int state, cost;
        Iterator iter = DataBaseCreate.stateCost.entrySet().iterator();
        Map.Entry entry;

        while (iter.hasNext()) {
            entry = (Map.Entry) iter.next();
            state = ((int) entry.getKey()) >> 4;
            cost = (int) entry.getValue();
            if (tmp.containsKey(state)) {
                tmp.put(state, Math.min(cost, tmp.get(state)));
            } else {
                tmp.put(state, cost);
            }
        }

        Iterator it = tmp.entrySet().iterator();
        while (it.hasNext()) {
            entry = (Map.Entry) it.next();
            state = (int) entry.getKey();
            cost = (int) entry.getValue();
            try {
                fileOut.write(state);
                fileOut.write(' ');
                fileOut.write(cost);
                fileOut.write('\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // stateCost映射清空
        stateCost.clear();
        try {
            fileOut.close();
            System.out.println("file closed successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
