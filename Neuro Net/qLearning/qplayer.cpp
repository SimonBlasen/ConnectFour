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

vector<MemoryItem> replayMemory;


QPlayer::QPlayer()
{
    oldScore = 0;

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

        long inputState = board;

        //add Player action
        //generate all possible actions?
        long action = 0x1;

        inputState = (board | action);

        ////////////////
        MemoryItem item(reward, board, inputState);
        replayMemory.push_back(item);

        if(replayMemory.size() > REPLAY_MEMORY_SIZE)
        {
            //select random elements from replayMemory:
            for(int i = 0; i < REPLAY_BATCH_SIZE; i++){
                int randomIndex = rand() %  replayMemory.size();
                MemoryItem currentItem = replayMemory[randomIndex];

//                vector<float> qTableRow;

//                vector<long> possibleActions; ///TODO create possible actions

//                for(int j = 0; j < possibleActions.size(); j++){
//                    long currentBoard = possibleActions[j];
//                    qTableRow[j] = net.run(currentBoard);
//                }

//                float updatedQValue = *std::max_element( possibleActions.begin(), possibleActions.end();


            }

        }






    }

    return 0x1L;

}
