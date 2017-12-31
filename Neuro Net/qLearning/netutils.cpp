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


