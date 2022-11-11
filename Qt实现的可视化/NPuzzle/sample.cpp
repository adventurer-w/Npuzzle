#include "sample.h"

Sample::Sample()
{

}
void Sample::makeMove(int dir,double speed)
{
    int x =  blank.first;
    int y = blank.second;
    int newX = x + dx[dir];
    int newY = y + dy[dir];
    Slide* src = slides[x*size+y];
    Slide* dst = slides[newX*size+newY];
    int h = src->h;
    int w = src->w;
    slides[x*size+y] = dst;
    slides[newX*size+newY] = src;
    blank.first = newX;
    blank.second = newY;

    int srcX = src->geometry().x();
    int srcY = src->geometry().y();
    int dstX = dst->geometry().x();
    int dstY = dst->geometry().y();
    src->setGeometry(dstX,dstY,w,h);
    // 每次移动到对方距离时分成十步移动
    for(int i=1;i<=10;i++){
        QElapsedTimer timer;
        timer.start();
        dst->setGeometry(dstX + i/10.0*(srcX-dstX),dstY + i/10.0*(srcY-dstY),w,h);
        // 每次移动花费至少60/speed ms，同时用一个bool变量isRun来控制暂停与运行
        while(timer.elapsed()<(60/speed)||!isRun)
            QCoreApplication::processEvents();
    }
}

void Sample::show()
{
    for(auto it:slides){
        it->show();
    }
}
void Sample::hide()
{
    for(auto it:slides){
        it->hide();
    }
}
int Sample::dx[4]={0,-1,0,1};
int Sample::dy[4]={1,0,-1,0};
