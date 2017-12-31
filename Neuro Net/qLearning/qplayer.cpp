#include "qplayer.h"

const double DISCOUNT = 0.9;
const double EPSILON = 0.1;
const double MAX_EPSILON = 0.9;
const double EPSILON_INCREASE_FACTOR = 800;

const double REPLAY_MEMORY_SIZE = 500;
double MEMORY_POINTER = 0; //????
const double REPLAY_BATCH_SIZE = 400;
double runs = 0;
bool firstRun = true;

vector<memoryItem> replayMemory;


QPlayer::QPlayer()
{
    test= 2;

}

long QPlayer::getInput(){
    runs++;

    if(firstRun){
        firstRun = false;
        NetUtils::initQNeuralNet(net);

    }
    else{
        double reward = 0;

    }

    return 0;

}
