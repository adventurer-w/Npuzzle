#include <iostream>
#include <vector>
#include <map>
#include <iterator>
#include <unordered_map>
using namespace std;

class Block {
private:
    int size;
    vector<int> slides;
    map<int, int> Index;
public:
    int getSize();

    int getSlide(int index);

    int getIndex(int slide);

    vector<int>& getSlides();

    bool isSlideIn(int slide);

    void Print();

    Block(vector<int>slides);
};
