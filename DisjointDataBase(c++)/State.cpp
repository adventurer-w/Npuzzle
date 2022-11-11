#include "State.h"
State::State() {
    state = 0;
}

State::State(int state, int num) {
    this->state = state;
    this->num = num;
}

State::State(const State& a){
    this->state = a.state;
    this->num = a.num;
}

void State::setNum(int num) {
    this->num =num;
}

void State::setState(int state){
    this->state = state;
}

int State::getNum(){
    return this->num;
}
int State::getState(){
    return this->state;
}

State& State::slidesToState(vector<int>slides) {
    int res = 0;
    for (int i = 0; i < int(4 * slides.size()); i++) {
        int j = slides.size() - i / 4 - 1;
        if (((slides[j] - 1) >> (i % 4) & 1) == 1) {
            res |= 1 << i;
        }
    }
    State* tmp = new State(res,slides.size());
    return *tmp;
}

int State::stateToSlide(int index) {
    int res = (this->state >> ((this->num - index - 1) * 4)) & 15;
    return res + 1;
}

void State::setStateSlide(int index,int value){
    value--;
    int base = this->num - index - 1;
    for(int i = 0; i < 4;i++){
        if(((value>>i)&1)==1){
            this->state |= 1<<(base*4+i);
        }else{
            this->state &=~(1<<(base*4+i));
        }
    }
}

void State::slidePrint(){
    for(int i = 0; i < this->num; i++){
        cout<<this->stateToSlide(i)<<" ";
    }
    cout<<endl;
}
