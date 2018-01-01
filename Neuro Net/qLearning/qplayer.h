#ifndef QPLAYER_H
#define QPLAYER_H


#include "memoryitem.h"
#include "netutils.h"

class QPlayer
{
public:
    QPlayer();
    int oldScore;
    long getInput(float oldScore, long board, bool isNew);
    FANN::neural_net net;
};

#endif // QPLAYER_H
