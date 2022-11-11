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
 * ��ѧ���������㷨���м���������
 * arg0: ������������      resources/problems.txt
 * arg1: ��������         NPUZZLE
 * arg2: ��Ŀ���ĸ��׶�    1
 * arg3: ��С���Feeder   stud.runner.PuzzleFeeder
 */
//-Xmx5120m -Xms5120m -Xmn1280m -Xss5m
//    -Xmx1024m -Xms1024m -Xmn256m -Xss4096k
public final class SearchTester {

    public static void main(String[] args) throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException, InterruptedException {

        //����args[3]�ṩ����������ѧ����EngineFeeder����
        EngineFeeder feeder = (EngineFeeder)
                Class.forName(args[3])
                        .getDeclaredConstructor().newInstance();


        ////���ļ��������������������ı��� args[0]�����������ļ������·��
        Scanner scanner = new Scanner(new File(args[0]));
        ArrayList<String> problemLines = getProblemLines(scanner);

        //feeder�����������ı���ȡѰ·���������ʵ��
        ArrayList<Problem> problems = feeder.getProblems(problemLines);
        //����ʵ�����뵽ArrayList��

        //��ǰ��������� args[1]    Ѱ·���⣬�������̣�Ұ�˴���ʿ���ӵ�
        ProblemType type = ProblemType.valueOf(args[1]);
        //����ڼ��׶� args[2]
        int step = Integer.parseInt(args[2]);

        //�����������ͺ͵�ǰ�׶Σ���ȡ������������������
        //Ѱ·����ֱ�ʹ��Grid�����Euclid������Ϊ��������
        ArrayList<HeuristicType> heuristics = getHeuristicTypes(type, step);

        for (HeuristicType heuristicType : heuristics) {
            //solveProblems�������ݲ�ͬ�����������ɲ�ͬ��searcher
            //��Feeder��ȡ��ʹ�õ��������棨AStar��IDAStar�ȣ���
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
     * �����������ͺ͵�ǰ�׶Σ���ȡ������������������
     * @param type
     * @param step
     * @return
     */
    private static ArrayList<HeuristicType> getHeuristicTypes(ProblemType type, int step) {
        //��⵱ǰ�����ڵ�ǰ�׶ο��õ��������������б�
        ArrayList<HeuristicType> heuristics = new ArrayList<>();
        //���ݲ�ͬ���������ͣ����в�ͬ�Ĳ���
        if (type == ProblemType.PATHFINDING) {
            heuristics.add(PF_GRID);
            heuristics.add(PF_EUCLID);
        }
        else {
            //NPuzzle����ĵ�һ�׶Σ�ʹ�ò���λ���ƺ������پ���
            if (step == 1 || step == 2) {
                heuristics.add(LINEAR_CONFLICT);
                heuristics.add(MANHATTAN);
//                heuristics.add(MISPLACED);
            }
            //NPuzzle����ĵ����׶Σ�ʹ��Disjoint Pattern
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
     * ʹ�ø�����searcher��������⼯���е��������⣬ͬʱʹ�ý���������õĽ���м��
     * @param problems     ���⼯��
     * @param searcher     searcher
     * @param heuristicType ʹ����������������
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
            // ʹ��AStar�����������
            Zobrist.getZobrist(3);
            StopwatchCPU timer1 = new StopwatchCPU();
            Deque<Node> path = searcher.search(problem);
            double time0 = timer1.elapsedTime()*0.7;
            double  time1=( double )(Math.round(time0* 100000 ))/ 100000;

            if (path == null) {
                System.out.println("No Solution" + "��ִ����" + time1 + "s��"+
                        "��������" + searcher.nodesGenerated() + "����㣬" +
                        "��չ��" + searcher.nodesExpanded() + "�����");
                continue;
            }




            // ��·���Ŀ��ӻ�
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
                        if(bx==0&&by==1){ //��
                            pw.print("2 ");
                        }else if(bx==0&&by==-1){ //��
                            pw.print("0 ");
                        }else if(bx==1&&by==0){ //��
                            pw.print("1 ");
                        }else if(bx==-1&&by==0){ //��
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

            System.out.println((i++)+" "+"����������" + heuristicType + "����·�����ȣ�" + path.size() + "��ִ����" + time1 + "s��" +
                    "��������" + searcher.nodesGenerated() + "����㣬" +
                    "��չ��" + searcher.nodesExpanded() + "�����");
        }
        xx++;
    }

    /**
     * ���ļ���������ʵ�����ַ����������ַ���������
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