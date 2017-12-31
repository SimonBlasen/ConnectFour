#ifndef QPLAYER_H
#define QPLAYER_H


#include "memoryitem.h"
#include "netutils.h"


class QPlayer
{
public:
    QPlayer();
    int test;
    long getInput();
    FANN::neural_net net;
};

#endif // QPLAYER_H
