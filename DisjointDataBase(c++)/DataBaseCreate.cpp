#include "DataBaseCreate.h"


const int DataBaseCreate::dx[] = {0, 1, 0, -1};
const int DataBaseCreate::dy[] = {1, 0, -1, 0};

map<int,int> DataBaseCreate::stateCost;

void DataBaseCreate::create(string filePreName,vector<Block>blocks) {
    for (int i = 0;i < (int)blocks.size();i++) {
        cout<<"this the " << i << " block"<<endl;
        BreadedFirstSearch(blocks[i]);
        DataBaseCreate::writeToFile(filePreName,i);

    }
}

vector<int>& DataBaseCreate::readFromFile(string filePath) {
    fstream fin(filePath,ios::in);

    vector<int>* tmp = new vector<int>();
    int state,cost;
    while(!fin.eof()){
        fin>>state>>cost;
        while(tmp->size()<state){
            tmp->push_back(0);
        }
        (*tmp).push_back(state);
    }
    return *tmp;
    
}

int DataBaseCreate::makeMove(State& state, int dx, int dy) {
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

    State newState = State(state);
 
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

    int newCost = stateCost[state.getState()] + costInc;
    if (!(stateCost.find(newState.getState())==stateCost.end())) {
        return -1;
    }
    stateCost[newState.getState()] = newCost;

    //更新当前状态
    state.setState(newState.getState());
    return costInc;
}

void DataBaseCreate::BreadedFirstSearch(Block block) {
    deque<int> deq;
    vector<int> slides = block.getSlides();
    slides.push_back(16);
    State tmp = State::slidesToState(slides);

    stateCost[tmp.getState()] = 0;
    deq.push_back(tmp.getState());

    int stateNow, exitFlag;
 
    int epoch = 0;
    while (!deq.empty()) {

        epoch++;
        if (epoch % 10000000 == 0)
            cout<<"epoch: "<<epoch<<"\n";

        stateNow = deq.front();
        deq.pop_front();

        for (int i = 0; i < 4; i++) {
            tmp.setState(stateNow);
            exitFlag = makeMove(tmp, dx[i], dy[i]);

            if (exitFlag == 0) {
                deq.push_front(tmp.getState());
            } else if (exitFlag == 1) {
                deq.push_back(tmp.getState());
            }
        }
    }
    cout<<"finshed all  epoch is " << epoch<<endl;
}

void DataBaseCreate::writeToFile(string filePreName,int index){
    map<int,int>tmp;

    string path = "./" + filePreName + "_" + to_string(index)+ ".txt";
    //path = filePreName;
    fstream fout(path,ios::out);
    cout<<path<<" "<< fout.is_open()<<endl;
    int state,cost;
    for(auto& it:DataBaseCreate::stateCost){
        state = it.first>>4;
        cost = it.second;
        if(tmp.find(state) == tmp.end()){
            tmp[state] = cost;
        }else{
            tmp[state] = min (tmp[state], cost);
        }
    }

    for(auto& it:tmp){
        fout<<it.first<<" "<<it.second<<endl;
    }

    stateCost.clear();
    fout.close();
    cout<<"write file finished "<<fout.is_open()<<endl;
}

