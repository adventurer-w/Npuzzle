#ifndef SLIDE_H
#define SLIDE_H

#include <QWidget>
#include <QLabel>
#include <QtDebug>
#include <QTimer>
#include <QTime>
#include <QThread>
#include <QElapsedTimer>

class Slide : public QLabel
{
    Q_OBJECT
public:
    explicit Slide(int row,int col,int index,int size,QWidget *parent = nullptr,bool hide=true);
    int w;
    int h;
    int size;
    static int baseline;

    int getIndex();
    int getRow();
    int getCol();
private:
    int index;
    int row;
    int col;

signals:

};

#endif // SLIDE_H
