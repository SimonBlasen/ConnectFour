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
    oldStateOwn = 0L;
    oldStateEnemy = 0L;
    runs =  0;
    firstRun = true;

}

void QPlayer::applyReward(float score, long boardOwn, long boardEnemy, bool isNew)
{

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

        MemoryItem item(reward, oldStateOwn, oldStateEnemy, boardOwn,boardEnemy);

        replayMemory[replayIndex] = item;
        replayIndex = (replayIndex < replayMemory.size()) ? replayIndex+1 : 0;

        if(replayMemory.size() > REPLAY_MEMORY_SIZE)
        {
            vector<float*> trainingDataX;
            vector<float*> trainingDataY;

            //select random elements from replayMemory:
            for(int i = 0; i < REPLAY_BATCH_SIZE; i++){
                int randomIndex = rand() %  replayMemory.size();
                MemoryItem currentItem = replayMemory[randomIndex];


                //should return long with one 1 at the chosen position
                vector<long> possibleActions = generatePossibleMoves(boardOwn, boardEnemy);
                vector<float> qTableRow(possibleActions.size());

                for(int j = 0; j < possibleActions.size(); j++){
                    long newBoard = (possibleActions[j] | currentItem.getNewStateOwn());

                    float input[64] = {0};
                    NetUtils::generateInput(newBoard,currentItem.getNewStateEnemy(),input);

                    // a bit hacky but should work because run returns array with outputs.
                    qTableRow[j] = *net.run(input); ///FIX NEEDED
                }

                float updatedQValue = currentItem.getReward() + DISCOUNT *  *max_element( qTableRow.begin(), qTableRow.end());

                float trainingInput[64] = {0};
                NetUtils::generateInput(currentItem.getOldStateOwn(),currentItem.getOldStateEnemy(),trainingInput);

                trainingDataX.push_back(trainingInput);
                float trainingOutput[1]= {updatedQValue};
                trainingDataY.push_back(trainingOutput);
            }
            FANN::training_data data;
            NetUtils::generateTrainData(trainingDataX,trainingDataY,data);

            net.train_on_data(data,NetUtils::MAX_ITERATIONS,NetUtils::ITERATION_TO_NEXT_PRINT,NetUtils::DESIRED_ERROR); ///FIX input format
            net.save("qLearn.net");
        }
    }
}

long QPlayer::getInput(float score, long boardOwn, long boardEnemy, bool isNew){


    double epsilonRunFactor = 0;

    if((double) runs / EPSILON_INCREASE_FACTOR > (MAX_EPSILON - EPSILON)){
        epsilonRunFactor = MAX_EPSILON - EPSILON;
    }
    else{
        epsilonRunFactor = (double) runs / EPSILON_INCREASE_FACTOR;
    }


    double r = rand() / RAND_MAX;
    vector<long> possibleActions = generatePossibleMoves(boardOwn,boardEnemy);

    int actionIndex = 0;

    if(r > (EPSILON + epsilonRunFactor))
    {
        actionIndex = rand() %  possibleActions.size();
    }
    else
    {

        vector<float> qTableRow(possibleActions.size());
        for(int i = 0; i < possibleActions.size();i++){
            long currentStateOwn = boardOwn;
            long currentStateWithAction = currentStateOwn | possibleActions[actionIndex];

            float input[64] = {0};
            NetUtils::generateInput(currentStateOwn,boardEnemy,input);

            qTableRow[i] = *net.run(input); ///FIX NEEDED
        }
        actionIndex = getBestMoveIndex(qTableRow);
    }

    oldScore = score;
    oldStateOwn = boardOwn;
    oldStateEnemy = boardEnemy;

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

vector<long> QPlayer::generatePossibleMoves(long boardP1, long boardP2)
{
    vector<long> possMoves;
    for (int i = 0; i < 16; i++)
    {
        for (int j = 0; j < 4; j++)
        {
            long toCheck = (0x01 << ((j * 16) + i));

            if ((toCheck & boardP1) == 0x0L && (toCheck & boardP2) == 0x0L)
            {
                possMoves.push_back(toCheck);
                break;
            }
        }
    }

    return possMoves;
}
