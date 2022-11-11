#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QFile>
#include <QFileDialog>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setWindowTitle("N-Puzzle solve path");
    this->setWindowIcon(QIcon(":/puzzle.png"));
    int foreH = 300;
    this->setFixedSize(QSize(600,600+foreH));
    menubar = menuBar();
    Slide::baseline = menubar->height()+foreH;
    setMenuBar(menubar);


    label = new QLabel(this);
    label->setPixmap(QPixmap(":/label.png"));
    label->setGeometry(0,25,600,150);

    steps = new QLabel(this);
    steps->setText("step:");
    steps->setGeometry(30,200,200,50);
    steps->setStyleSheet("QLabel{font-family:Snap ITC;\
                         font-size: 30px;}");

    start = new QPushButton(this);
    start->setIcon(QIcon(":/start.png"));
    start->setGeometry(200,200,50,50);

    play = new Play(this);
    play->setGeometry(350,200,50,50);

    left = new QPushButton(this);
    left->setIcon(QIcon(":/left.png"));
    left->setGeometry(300,200,50,50);

    right = new QPushButton(this);
    right->setIcon(QIcon(":/right.png"));
    right->setGeometry(400,200,50,50);

    down = new QPushButton(this);
    down->setIcon(QIcon(":/down.png"));
    down->setGeometry(250,200,50,50);

    up = new QPushButton(this);
    up->setIcon(QIcon(":/up.png"));
    up->setGeometry(450,200,50,50);

    connect(start,&QPushButton::clicked,[=](){
        viewPath();
    });

    connect(play,&QPushButton::clicked,[&](){
        if(isStart){
            if(play->isPlay){
               play->setIcon(QIcon(":/play2.png"));
            }else{
               play->setIcon(QIcon(":/stop.png"));
            }
            play->isPlay = 1-play->isPlay;
            samples[now].isRun = 1-samples[now].isRun;
        }
    });

    connect(left,&QPushButton::clicked,[=](){
        if(!isStart)
            getPre();
    });

    connect(right,&QPushButton::clicked,[=](){
        if(!isStart)
            getNext();
    });

    connect(down,&QPushButton::clicked,[=](){
        speedDown();
    });

    connect(up,&QPushButton::clicked,[=](){
        speedUp();
    });

    QAction* start = menubar->addAction("Start");
    QAction* next = menubar->addAction("Next");
    QAction* pre = menubar->addAction("Pre");
    QAction* pause = menubar->addAction("Pause");
    QAction* speedup = menubar->addAction("SpeedUp");
    QAction* speeddown = menubar->addAction("SpeedDown");

    QStatusBar* statuBar = statusBar();
    setStatusBar(statuBar);

//    statusLabel = new QLabel;
    //statuBar->addWidget(statusLabel);



    QFile file("../NPuzzle/data.txt");
    file.open(QIODevice::ReadOnly);
    if(file.isOpen()){
        qDebug()<<"file is  opened!";
    }

    QString line = file.readLine();
    line.remove(line.length()-2,2);

    int cases = line.toInt();
    qDebug()<<"cases:"<<cases;
    samples.resize(cases);
    for(int k=0;k<cases;k++){
        line = file.readLine();
        QStringList list = line.split(' ');
        int size = samples[k].size = list[0].toInt();

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                int val = list[i*size+j+1].toInt();
                samples[k].init[i*size+j] = new Slide(i,j,val,size,this);
                if(val==0){
                    samples[k].blank.first = i;
                    samples[k].blank.second = j;
                }
            }
        }

        int step = list[size*size+1].toInt();

        for(int i=0;i<step;i++){
           samples[k].steps.push_back(list[size*size+2+i].toInt());
        }

    }
    file.close();

    connect(start,&QAction::triggered,[=](){
        viewPath();
    });

    connect(pre,&QAction::triggered,[=](){
        if(!isStart)
            getPre();
    });

    connect(next,&QAction::triggered,[=](){
        if(!isStart)
            getNext();
    });

    connect(pause,&QAction::triggered,[&](){
        if(isStart)
            samples[now].isRun = 1-samples[now].isRun;
    });

    connect(speedup,&QAction::triggered,[=](){
        speedUp();
    });

    connect(speeddown,&QAction::triggered,[=](){
        speedDown();
    });

}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::viewPath()
{
    if(isStart)return;
    isStart = true;
    emit play->click();
    // 将当前slides更新位初始状态
    QMap<int,Slide*>::iterator iter = samples[now].init.begin();
    for(;iter!=samples[now].init.end();iter++){
        if(samples[now].slides.find(iter.key())!=samples[now].slides.end()){
            if(samples[now].slides[iter.key()]!=nullptr)
                delete samples[now].slides[iter.key()];
        }
        int row = iter.value()->getRow();
        int col = iter.value()->getCol();
        int index = iter.value()->getIndex();
        samples[now].slides[iter.key()] = new Slide(row,col,index,samples[now].size,this);
        //qDebug()<<iter.key()<<" "<<row<<" "<<col;
    }
    // 将当前的滑块显示出来
    samples[now].show();
    int dir;
    for(int i=0;i<samples[now].steps.length();i++){
        dir = samples[now].steps[i];
        samples[now].makeMove(dir,speed);
        // 每次移动完显示当前步数
        steps->setText("step:"+QString::number(i+1));
    }
    emit play->click();
    isStart = false;
}

void MainWindow::getPre()
{
    if(now>0)
    {
        steps->setText("step:");
        samples[now].hide();
        now--;
        QMap<int,Slide*>::iterator iter = samples[now].init.begin();
        for(;iter!=samples[now].init.end();iter++){
            if(samples[now].slides.find(iter.key())!=samples[now].slides.end()){
                if(samples[now].slides[iter.key()]!=nullptr)
                    delete samples[now].slides[iter.key()];
            }
            int row = iter.value()->getRow();
            int col = iter.value()->getCol();
            int index = iter.value()->getIndex();
            samples[now].slides[iter.key()] = new Slide(row,col,index,samples[now].size,this);
            //qDebug()<<iter.key()<<" "<<row<<" "<<col;
        }
        samples[now].show();
    }
}
void MainWindow::getNext()
{
    if(now<samples.size()-1)
    {
        steps->setText("step:");
        samples[now].hide();
        now++;
        QMap<int,Slide*>::iterator iter = samples[now].init.begin();
        for(;iter!=samples[now].init.end();iter++){
            if(samples[now].slides.find(iter.key())!=samples[now].slides.end()){
                if(samples[now].slides[iter.key()]!=nullptr)
                    delete samples[now].slides[iter.key()];
            }
            int row = iter.value()->getRow();
            int col = iter.value()->getCol();
            int index = iter.value()->getIndex();
            samples[now].slides[iter.key()] = new Slide(row,col,index,samples[now].size,this);
            //qDebug()<<iter.key()<<" "<<row<<" "<<col;
        }
        samples[now].show();
    }
}
void MainWindow::speedUp()
{
    if(speed<5)
        speed++;
}
void MainWindow::speedDown(){
    if(speed>1)
        speed--;
}
