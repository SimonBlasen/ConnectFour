#ifndef NETUTILS_H
#define NETUTILS_H
#include "floatfann.h"
#include "fann_cpp.h"
#include <vector>
#include <ios>
#include <iostream>
#include <iomanip>
#include <vector>
#include <qplayer.h>

using namespace std;

class NetUtils
{
private:


public:
    static const float LEARNING_RATE;
    static const unsigned int AMOUNT_LAYERS;
    static const unsigned int SIZE_INPUT;
    static const unsigned int SIZE_HIDDEN;
    static const unsigned int SIZE_OUTPUT;
    static const float DESIRED_ERROR;
    static const unsigned int MAX_ITERATIONS;
    static const unsigned int ITERATION_TO_NEXT_PRINT;
    static const double STEEPNESS_HIDDEN;
    static const double STEEPNESS_INPUT;
    static const double STEEPNESS_OUTPUT;
    NetUtils();
    static void initQNeuralNet(FANN::neural_net &net);
    static void generateInput(long p1, long p2, vector<float> input);
    static void generateInput(long p1, long p2, float input[]);
    static FANN::training_data generateTrainData(vector<float*> trainingInput, vector<float*> trainingOutput);
};

#endif // NETUTILS_H
