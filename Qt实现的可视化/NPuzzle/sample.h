#ifndef SAMPLE_H
#define SAMPLE_H

#include <QCoreApplication>
#include "slide.h"

class Sample
{

public:
    explicit Sample();
    static int dx[4];
    static int dy[4];
    bool isRun = false;
    QPair<int,int> blank;
    int size;
    QMap<int,Slide*>init;
    QMap<int,Slide*>slides;
    QVector<int>steps;

    void makeMove(int dir,double speed);
    void show();
    void hide();


signals:

};
//Q_DECLARE_METATYPE(Sample);
#endif // SAMPLE_H
