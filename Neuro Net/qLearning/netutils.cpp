#include "netutils.h"

const float NetUtils::LEARNING_RATE = 0.2f;
const unsigned int NetUtils::AMOUNT_LAYERS = 3;
const unsigned int NetUtils::SIZE_INPUT = 2;
const unsigned int NetUtils::SIZE_HIDDEN = 2;
const unsigned int NetUtils::SIZE_OUTPUT = 1;
const float NetUtils::DESIRED_ERROR = 0.0001f;
const unsigned int NetUtils::MAX_ITERATIONS = 30000;
const unsigned int NetUtils::ITERATION_TO_NEXT_PRINT = 100;
const double NetUtils::STEEPNESS_HIDDEN = 1;
const double NetUtils::STEEPNESS_INPUT = 1;
const double NetUtils::STEEPNESS_OUTPUT = 1;


NetUtils::NetUtils()
{
}

void NetUtils::initQNeuralNet(FANN::neural_net &net){
    int numLayers = 3;
    const unsigned int layers [3] = {SIZE_INPUT, SIZE_HIDDEN,SIZE_OUTPUT};

    net.create_standard_array(numLayers,layers);
    net.set_learning_rate(LEARNING_RATE);
    net.set_activation_steepness_hidden(STEEPNESS_HIDDEN);
    net.set_activation_steepness_output(STEEPNESS_OUTPUT);
    net.set_activation_function_hidden(FANN::LINEAR);
    net.set_activation_function_output(FANN::LINEAR);
    net.set_training_algorithm(FANN::TRAIN_QUICKPROP);
}

void NetUtils::generateInput(long p1, long p2, float input[]){

    for(int i = 0; i < 64; i++){
        if((p1 >> i) & 0x1L == 1){
            input[i] = 0.3f;
        }
        else if((p2 >> i) & 0x1L == 1){
            input[i] = -0.3f;
        }
        else {
            if (i < 16)
            {
                input[i] = 0.1f;
            }
            else if (((p1 >> (i - 16)) & p1) == 0x0L && ((p1 >> (i - 16)) & p2) == 0x0L)
            {
                input[i] = -0.1f;
            }
            else
            {
                input[i] = 0.1f;
            }
        }
    }
}

void NetUtils::generateTrainData(vector<float *> trainingInput, vector<float*> trainingOutput,FANN::training_data &data)
{
    float* inputs[trainingInput.size()];

    std::copy(trainingInput.begin(), trainingInput.end(), inputs);
    float* outputs[trainingOutput.size()];
    std::copy(trainingOutput.begin(), trainingOutput.end(), outputs);
    data.set_train_data(trainingInput.size(),64, inputs,1,outputs);

}


