package stud.problem.npuzzle;

import java.security.SecureRandom;

public class Zobrist {
    public static long x=0;
    public static int[][] zobristHash4 = Zobrist.getZobrist(4);
    public static int[][] zobristHash3 = Zobrist.getZobrist(3);

    public static int[][] getZobrist(int size) {
        SecureRandom rand = new SecureRandom();
        int[][] zobrist = new int[size*size][];
        for(int i=0;i<size*size;i++){
            zobrist[i]= new int[size*size];
            for(int j=0;j<size*size;j++){
                zobrist[i][j]=rand.nextInt();
                x++;
            }
        }
        return zobrist;
    }
}
