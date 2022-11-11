#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QMenuBar>
#include <QMap>
#include "sample.h"
#include "play.h"
QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

    void viewPath();
    void getPre();
    void getNext();
    void speedUp();
    void speedDown();

private:
    Ui::MainWindow *ui;
    int now = 0;
    double speed = 1;
    QVector<Sample>samples;
    bool isStart = false;

    QMenuBar* menubar;
    Play* play;
    QPushButton* start;
    QPushButton* up;
    QPushButton* down;
    QPushButton* left;
    QPushButton* right;
    QLabel* label;
    QLabel* steps;

};
#endif // MAINWINDOW_H
