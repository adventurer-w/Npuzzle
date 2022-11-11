#include "Block.h"
using namespace std;

int Block::getSize() {
    return size;
}

int Block::getSlide(int index) {
    return slides[index];
}

int Block::getIndex(int slide) {
    return Index[slide];
}

vector<int>& Block::getSlides() {
    vector<int>* tmp = new vector<int>(this->slides);
    return *tmp;
}

bool Block::isSlideIn(int slide) {
    return Index.find(slide)!=Index.end();
}


void Block::Print() {
    for (int i = 0; i < size; i++) {
        cout<<slides[i] << " "<<endl;
    }
}

Block::Block(vector<int>slides) {
    this->size = slides.size();
    for (int i = 0; i < size; i++) {
        this->slides.push_back(slides[i]);
        this->Index[slides[i]] = i;
    }
}

