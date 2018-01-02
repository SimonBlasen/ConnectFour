#ifndef QPLAYER_H
#define QPLAYER_H


#include "memoryitem.h"
#include "netutils.h"
#include <iostream>
#include <stdlib.h>

#include <algorithm>

using namespace std;

class QPlayer
{
private:
    vector<MemoryItem> replayMemory;
    float oldScore;
    long oldState;
    FANN::neural_net net;
    double runs;
    bool firstRun;
    int replayIndex;

    const static double DISCOUNT;
    const static double EPSILON;
    const static double MAX_EPSILON;
    const static double EPSILON_INCREASE_FACTOR;

    const static int REPLAY_MEMORY_SIZE;
    const static double REPLAY_BATCH_SIZE;


public:
    QPlayer();

    long getInput(float oldScore, long board, bool isNew);
    static int getBestMoveIndex(vector<float> moves);

};

#endif // QPLAYER_H
