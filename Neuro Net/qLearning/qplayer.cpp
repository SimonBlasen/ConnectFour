#include "qplayer.h"

const double QPlayer::DISCOUNT = 0.9;
const double QPlayer::EPSILON = 0.1;
const double QPlayer::MAX_EPSILON = 0.9;
const double QPlayer::EPSILON_INCREASE_FACTOR = 800;

const int QPlayer::REPLAY_MEMORY_SIZE = 10000;
const double QPlayer::REPLAY_BATCH_SIZE = 500;




QPlayer::QPlayer()
{
    //replayMemory.reserve(REPLAY_MEMORY_SIZE);
    replayIndex = 0;
    oldScore = 0.0f;
    oldStateOwn = 0L;
    oldStateEnemy = 0L;
    runs =  0;
    firstRun = true;

}

void QPlayer::applyReward(float score, long boardOwn, long boardEnemy, bool isNew)
{
    //cout << "score=" << score << endl;

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

        if (replayMemory.size() < REPLAY_MEMORY_SIZE)
        {
            replayMemory.push_back(item);
        }
        else
        {
            //delete &replayMemory[replayIndex];
            replayMemory[replayIndex] = item;
            replayIndex = (replayIndex < replayMemory.size()) ? replayIndex+1 : 0;
        }
        //cout << "Im in" << replayMemory.size() << endl;


        if(replayMemory.size() >= REPLAY_MEMORY_SIZE)
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

                    float* input = new float[64];
                    //vector<float> input;
                    NetUtils::generateInput(newBoard,currentItem.getNewStateEnemy(),input);

                    // a bit hacky but should work because run returns array with outputs.
                    qTableRow[j] = *net.run(input); ///FIX NEEDED

                    delete[] input;
                }

                float updatedQValue = currentItem.getReward() + DISCOUNT *  *max_element( qTableRow.begin(), qTableRow.end());


                float* trainingInput = new float[64];
                //vector<float> trainingInput;
                NetUtils::generateInput(currentItem.getOldStateOwn(),currentItem.getOldStateEnemy(),trainingInput);

                trainingDataX.push_back(trainingInput);
                float* trainingOutput = new float[1];
                trainingOutput[0] = updatedQValue;
                //float trainingOutput[1]= {updatedQValue};
                trainingDataY.push_back(trainingOutput);
            }



            for (int i = 0; i < trainingDataY.size(); i++)
            {
                //cout <<"Fucker"<< " qValue: " << trainingDataY[i][0] << endl;
            }

            //FANN::training_data data = NetUtils::generateTrainData(trainingDataX,trainingDataY);














/*


            fann_type** inputs = new fann_type*[trainingDataX.size()];

            for (int i = 0; i < trainingDataX.size(); i++)
            {
                //cout << "T" << i << endl;
                inputs[i] = new fann_type[64];
                for (int j = 0; j < 64; j++)
                {
                    inputs[i][j] = (fann_type)trainingDataX[i][j];
                    //cout << inputs[i][j] << ",";
                }
                //cout << endl;
            }

            //std::copy(trainingInput.begin(), trainingInput.end(), inputs);
            fann_type** outputs = new fann_type*[trainingDataY.size()];

            for (int i = 0; i < trainingDataY.size(); i++)
            {
                outputs[i] = (fann_type*)trainingDataY[i];
                //cout << trainingOutput[i][ 0] << endl;
            }
*/









            //cout << trainingInput.size() << "," << trainingOutput.size() << "" << endl;


            //std::copy(trainingOutput.begin(), trainingOutput.end(), outputs);
/*
            FANN::training_data *data2 = new FANN::training_data();

            fann_type** inputs2 = new fann_type*[2];
            fann_type** outputs2 = new fann_type*[2];
            inputs2[0] = new fann_type[2];
            inputs2[1] = new fann_type[2];
            inputs2[0][0] = ((fann_type)1.0f);
            inputs2[0][1] = ((fann_type)1.0f);
            inputs2[1][0] = ((fann_type)1.0f);
            inputs2[1][1] = ((fann_type)1.0f);
            outputs2[0] = new fann_type[1];
            outputs2[1] = new fann_type[1];
            outputs2[0][0] = ((fann_type)1.0f);
            outputs2[0][1] = ((fann_type)1.0f);
*/



            //data = new FANN::training_data();
            //data->set_train_data(trainingDataX.size(), 64, inputs,1,outputs);








            //net.train_on_data(*data,1,0,0.000000002f);



            //net.train_on_data(data,1,1,0.4f);

            //net.train_on_data(data,NetUtils::MAX_ITERATIONS,NetUtils::ITERATION_TO_NEXT_PRINT,NetUtils::DESIRED_ERROR); ///FIX input format




            /*for (int i = 0; i < trainingDataX.size(); i++)
            {
                delete trainingDataX[i];
            }

            for (int i = 0; i < trainingDataY.size(); i++)
            {
                delete trainingDataY[i];
            }*/


            //delete data;
        }
        _CrtDumpMemoryLeaks();
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


    double r = ((double)rand()) / ((double)RAND_MAX);
    vector<long> possibleActions = generatePossibleMoves(boardOwn,boardEnemy);

    int actionIndex = 0;
    //cout << "r=" << r << ", EPSILON=" << EPSILON << "epsilonrunfactor=" << epsilonRunFactor << endl;

    if(r > (EPSILON + epsilonRunFactor))
    {
        actionIndex = rand() %  possibleActions.size();
        //cout << "Took random action: " << actionIndex << endl;
    }
    else
    {

        vector<float> qTableRow(possibleActions.size());

        //cout << "QTableRow: [";

        for(int i = 0; i < possibleActions.size();i++){
            long currentStateOwn = boardOwn;
            long currentStateWithAction = currentStateOwn | possibleActions[i];


            //cout << currentStateOwn << "==" << currentStateWithAction << endl;

            float input[64] = {0};
            NetUtils::generateInput(currentStateWithAction,boardEnemy,input);

            qTableRow[i] = *net.run(input);
            //cout << qTableRow[i] << ",";

        }
        //cout << "]" << endl;
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


int QPlayer::getRuns()
{
    return runs;
}
