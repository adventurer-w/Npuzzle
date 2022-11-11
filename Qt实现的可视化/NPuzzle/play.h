#ifndef PLAY_H
#define PLAY_H

#include <QPushButton>

namespace Ui {
class Play;
}

class Play : public QPushButton
{
    Q_OBJECT

public:
    explicit Play(QWidget *parent = nullptr);
    ~Play();
     bool isPlay =false;
private:
    Ui::Play *ui;

};

#endif // PLAY_H
