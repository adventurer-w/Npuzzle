#include "slide.h"

Slide::Slide(int row,int col,int index,int size,QWidget *parent,bool hide) : QLabel(parent)
{

    this->setAlignment(Qt::AlignCenter);
    this->index = index;
    this->row = row;
    this->col = col;
    if(index){
        setText(QString::number(index));
        setStyleSheet("QLabel{border:2px solid black;\
                      border-radius: 10px;        \
                      font-size:20pt;}");
    }
    this->size = size;
    assert(size!=0);
    w = h = (600-(size+1)*30)/size;
    setGeometry(30+col*(w+30),baseline+row*(h+30),w,h);
    if(hide)this->hide();

}

int Slide::getIndex()
{
    return index;
}

int Slide::getRow()
{
    return row;
}

int Slide::getCol()
{
    return col;
}

int Slide::baseline = 0;
