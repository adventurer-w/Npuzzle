package core.runner;

import algs4.util.StopwatchCPU;
import core.problem.Problem;
import core.problem.ProblemType;
import core.solver.algorithm.Searcher;
import core.solver.queue.Node;
import core.solver.algorithm.heuristic.HeuristicType;
import stud.problem.npuzzle.DataBaseCreate;
import stud.problem.npuzzle.NPuzzle;
import stud.problem.npuzzle.NPuzzleState;
import stud.problem.npuzzle.Zobrist;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

import static core.solver.algorithm.heuristic.HeuristicType.*;
import static core.solver.algorithm.heuristic.HeuristicType.LINEAR_CONFLICT;

/**
 * 对学生的搜索算法进行检测的主程序
 * arg0: 问题输入样例      resources/problems.txt
 * arg1: 问题类型         NPUZZLE
 * arg2: 项目的哪个阶段    1
 * arg3: 各小组的Feeder   stud.runner.PuzzleFeeder
 */
//-Xmx5120m -Xms5120m -Xmn1280m -Xss5m
//    -Xmx1024m -Xms1024m -Xmn256m -Xss4096k
public final class SearchTester {

    public static void main(String[] args) throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException, InterruptedException {

        //根据args[3]提供的类名生成学生的EngineFeeder对象
        EngineFeeder feeder = (EngineFeeder)
                Class.forName(args[3])
                        .getDeclaredConstructor().newInstance();


        ////从文件读入所有输入样例的文本； args[0]：输入样例文件的相对路径
        Scanner scanner = new Scanner(new File(args[0]));
        ArrayList<String> problemLines = getProblemLines(scanner);

        //feeder从输入样例文本获取寻路问题的所有实例
        ArrayList<Problem> problems = feeder.getProblems(problemLines);
        //问题实例读入到ArrayList中

        //当前问题的类型 args[1]    寻路问题，数字推盘，野人传教士过河等
        ProblemType type = ProblemType.valueOf(args[1]);
        //任务第几阶段 args[2]
        int step = Integer.parseInt(args[2]);

        //根据问题类型和当前阶段，获取所有启发函数的类型
        //寻路问题分别使用Grid距离和Euclid距离作为启发函数
        ArrayList<HeuristicType> heuristics = getHeuristicTypes(type, step);

        for (HeuristicType heuristicType : heuristics) {
            //solveProblems方法根据不同启发函数生成不同的searcher
            //从Feeder获取所使用的搜索引擎（AStar，IDAStar等），
            if(step == 1){
//                solveProblems(problems, feeder.getIdaStar(heuristicType), heuristicType,step);
                solveProblems(problems, feeder.getAStar(heuristicType), heuristicType,step);
            }else if(step==2) {
//                solveProblems(problems, feeder.getAStar(heuristicType), heuristicType,step);
                solveProblems(problems, feeder.getIdaStar(heuristicType), heuristicType,step);
            }else{
                DataBaseCreate dataBaseCreate;
                solveProblems(problems, feeder.getIdaStar(heuristicType), heuristicType,step);
//                solveProblems(problems, feeder.getAStar(heuristicType), heuristicType,step);
            }
            System.out.println();
        }
    }

    /**
     * 根据问题类型和当前阶段，获取所有启发函数的类型
     * @param type
     * @param step
     * @return
     */
    private static ArrayList<HeuristicType> getHeuristicTypes(ProblemType type, int step) {
        //求解当前问题在当前阶段可用的启发函数类型列表
        ArrayList<HeuristicType> heuristics = new ArrayList<>();
        //根据不同的问题类型，进行不同的测试
        if (type == ProblemType.PATHFINDING) {
            heuristics.add(PF_GRID);
            heuristics.add(PF_EUCLID);
        }
        else {
            //NPuzzle问题的第一阶段，使用不在位将牌和曼哈顿距离
            if (step == 1 || step == 2) {
                heuristics.add(LINEAR_CONFLICT);
                heuristics.add(MANHATTAN);
//                heuristics.add(MISPLACED);
            }
            //NPuzzle问题的第三阶段，使用Disjoint Pattern
            else if (step == 3){
                DataBaseCreate dataBase = new DataBaseCreate();
//                heuristics.add(MANHATTAN);
                heuristics.add(DISJOINT_PATTERN);
                heuristics.add(LINEAR_CONFLICT);
            }
        }
        return heuristics;
    }

    /**
     * 使用给定的searcher，求解问题集合中的所有问题，同时使用解检测器对求得的解进行检测
     * @param problems     问题集合
     * @param searcher     searcher
     * @param heuristicType 使用哪种启发函数？
     */
    static int xx=0;
    private static void solveProblems(ArrayList<Problem> problems, Searcher searcher, HeuristicType heuristicType,int step) throws InterruptedException, IOException {
        int i=1;

        if(xx==0){
            PrintWriter pw=new PrintWriter("resources/data.txt");
            pw.println(problems.size());
            pw.flush();
            pw.close();
        }
        for (Problem problem : problems) {
            // 使用AStar引擎求解问题
            Zobrist.getZobrist(3);
            StopwatchCPU timer1 = new StopwatchCPU();
            Deque<Node> path = searcher.search(problem);
            double time0 = timer1.elapsedTime()*0.7;
            double  time1=( double )(Math.round(time0* 100000 ))/ 100000;

            if (path == null) {
                System.out.println("No Solution" + "，执行了" + time1 + "s，"+
                        "共生成了" + searcher.nodesGenerated() + "个结点，" +
                        "扩展了" + searcher.nodesExpanded() + "个结点");
                continue;
            }




            // 解路径的可视化
            if(xx==0){
                FileWriter f=new FileWriter("resources/data.txt",true);
                NPuzzleState x= (NPuzzleState)(((NPuzzle)((NPuzzle) problem)).getInitialState());
                PrintWriter pw = new PrintWriter(f);
                pw.print(x.getSize()+" ");
                for(int j=0;j<x.size*x.size;j++){
                    int tx=j/x.size,ty=j%x.size;
                    pw.print(x.states[tx][ty]+" ");
                }
                pw.print(path.size()+" ");


                NPuzzleState nw;
                pw.print(path.size()+" ");
                for(var temp:path){
                    nw = (NPuzzleState)temp.getState();
                        int bx = x.getRow()-nw.getRow(),by = x.getCol() - nw.getCol();
                        if(bx==0&&by==1){ //左
                            pw.print("2 ");
                        }else if(bx==0&&by==-1){ //右
                            pw.print("0 ");
                        }else if(bx==1&&by==0){ //上
                            pw.print("1 ");
                        }else if(bx==-1&&by==0){ //下
                            pw.print("3 ");
                        }
                        x=nw;
                }

                pw.println();
                f.flush();
                f.close();
                pw.flush();
                pw.close();

//                problem.showSolution(path);
            }
//
//            for(var temp:path){
//                temp.getState().draw();
//            }

            System.out.println((i++)+" "+"启发函数：" + heuristicType + "，解路径长度：" + path.size() + "，执行了" + time1 + "s，" +
                    "共生成了" + searcher.nodesGenerated() + "个结点，" +
                    "扩展了" + searcher.nodesExpanded() + "个结点");
        }
        xx++;
    }

    /**
     * 从文件读入问题实例的字符串，放入字符串数组里
     * @param scanner
     * @return
     */
    public static ArrayList<String> getProblemLines(Scanner scanner) {
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNext()){
            lines.add(scanner.nextLine());
        }
        return lines;
    }


}