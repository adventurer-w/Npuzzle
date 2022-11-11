#include "Block.h"
using namespace std;

class State {
private:
    // 存储一个block的状态
    int state;

    // 状态存储的滑块个数
    int num;
public:
    State();

    State(int state, int num);

    State(const State& a);

    void setNum(int num);

    void setState(int state);

    int getNum();

    int getState();

    static State& slidesToState(vector<int>slides);

    int stateToSlide(int index);

    void setStateSlide(int index,int value);

    void slidePrint();
};
