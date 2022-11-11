#include "play.h"
#include "ui_play.h"

Play::Play(QWidget *parent) :
    QPushButton(parent),
    ui(new Ui::Play)
{
    ui->setupUi(this);
    this->setIcon(QIcon(":play2.png"));
}

Play::~Play()
{
    delete ui;
}
