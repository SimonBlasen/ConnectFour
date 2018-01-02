#include "qplayer.h"

const double QPlayer::DISCOUNT = 0.9;
const double QPlayer::EPSILON = 0.1;
const double QPlayer::MAX_EPSILON = 0.9;
const double QPlayer::EPSILON_INCREASE_FACTOR = 800;

const int QPlayer::REPLAY_MEMORY_SIZE = 500;
const double QPlayer::REPLAY_BATCH_SIZE = 400;




QPlayer::QPlayer()
{
    replayMemory.reserve(REPLAY_MEMORY_SIZE);
    replayIndex = 0;
    oldScore = 0.0f;
    oldState = 0L;
    runs =  0;
    firstRun = true;

}

long QPlayer::getInput(float score, long board, bool isNew){
    runs++;

    if(firstRun){
        firstRun = false;
        NetUtils::initQNeuralNet(net);

    }
    else{
        double reward = 0;
        if(!isNew && oldScore < score){
            reward = 1;
        }
        else if(!isNew && oldScore > score){
            reward = -1;
        }
        else if(!isNew){
            reward = -0.1;
        }


        MemoryItem item(reward, oldState, board);

        replayMemory[replayIndex] = item;
        replayIndex = (replayIndex < replayMemory.size()) ? replayIndex+1 : 0;

        if(replayMemory.size() > REPLAY_MEMORY_SIZE)
        {
            vector<long> trainingDataX;
            vector<float> trainingDataY;

            //select random elements from replayMemory:
            for(int i = 0; i < REPLAY_BATCH_SIZE; i++){
                int randomIndex = rand() %  replayMemory.size();
                MemoryItem currentItem = replayMemory[randomIndex];


                //should return long with one 1 at the chosen position
                vector<long> possibleActions; ///TODO create possible actions
                vector<float> qTableRow(possibleActions.size());

                for(int j = 0; j < possibleActions.size(); j++){
                    long currentBoard = (possibleActions[j] | currentItem.getNewInput());




                    qTableRow[j] = (float) net.run(currentBoard); ///FIX NEEDED
                }

                float updatedQValue = currentItem.getReward() + DISCOUNT *  *max_element( qTableRow.begin(), qTableRow.end());

                trainingDataX.push_back(currentItem.getOldInput());
                trainingDataY.push_back(updatedQValue);
            }

            net.train_on_data(); ///FIX input format

        }
    }

    double epsilonRunFactor = 0;

    if((double) runs / EPSILON_INCREASE_FACTOR > (MAX_EPSILON - EPSILON)){
        epsilonRunFactor = MAX_EPSILON - EPSILON;
    }
    else{
        epsilonRunFactor = (double) runs / EPSILON_INCREASE_FACTOR;
    }


    double r = rand() / RAND_MAX;
    vector<long> possibleActions; ///TODO need method

    int actionIndex = 0;

    if(r > (EPSILON + epsilonRunFactor))
    {
        actionIndex = rand() %  possibleActions.size();
    }
    else
    {
        vector<float> qTableRow(possibleActions.size());
        for(int i = 0; i < possibleActions.size();i++){
            long currentState = board;
            long currentStateWithAction = currentState | possibleActions[actionIndex];


            qTableRow[i] = net.run(currentStateWithAction); ///FIX NEEDED
        }
        actionIndex = getBestMoveIndex(qTableRow);
    }

    oldScore = score;
    oldState = board;

    return possibleActions[actionIndex];


}

int QPlayer::getBestMoveIndex(vector<float> values)
{
    int currentBest = 0;
    for(int i = 0; i < values.size(); i++){
        if(values[i] > values[currentBest]){
            currentBest = i;
        }
    }
    return currentBest;

}
