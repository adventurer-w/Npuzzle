#include "State.h"
#include <queue>
#include <string>
#include <fstream>
using namespace std;

class DataBaseCreate {
public:
    // 移动增量
    const static int dx[];
    const static int dy[];

    // 问题规模以及每一位滑块存储所需的比特数
    const static int Size = 4;

    //public static HashMap<Integer, Integer> stateCost;
    static map<int,int> stateCost;

    static void create(string filePreName,vector<Block>blocks);

    static vector<int>& readFromFile(string filePath);
    
private:
    static int makeMove(State& state, int dx, int dy);

    static void BreadedFirstSearch(Block block);

    static void writeToFile(string filePreName,int index);


};
