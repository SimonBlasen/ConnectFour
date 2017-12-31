#include "floatfann.h"
#include "fann_cpp.h"
#include <vector>
#include <ios>
#include <iostream>
#include <iomanip>

#include "memoryitem.h"
using std::cout;
using std::cerr;
using std::endl;
using std::setw;
using std::left;
using std::right;
using std::showpos;
using std::noshowpos;

using namespace std;


const float LEARNING_RATE = 0.2f;
const unsigned int AMOUNT_LAYERS = 3;
const unsigned int SIZE_INPUT = 2;
const unsigned int SIZE_HIDDEN = 2;
const unsigned int SIZE_OUTPUT = 1;
const float DESIRED_ERROR = 0.0001f;
const unsigned int MAX_ITERATIONS = 30000;
const unsigned int ITERATION_TO_NEXT_PRINT = 100;
const double STEEPNESS_HIDDEN = 1;
const double STEEPNESS_INPUT = 1;
const double STEEPNESS_OUTPUT = 1;

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



// Callback function that simply prints the information to cout
int print_callback(FANN::neural_net &net, FANN::training_data &train,
    unsigned int max_epochs, unsigned int epochs_between_reports,
    float desired_error, unsigned int epochs, void *user_data)
{
    cout << "Epochs     " << setw(8) << epochs << ". "
         << "Current Error: " << left << net.get_MSE() << right << endl;
    return 0;
}


bool initQNeuralNet(FANN::neural_net &net){
    int numLayers = 3;
    const unsigned int layers [3] = {SIZE_INPUT, SIZE_HIDDEN,SIZE_OUTPUT};

    net.create_standard_array(numLayers,layers);
    net.set_learning_rate(LEARNING_RATE);
    net.set_activation_steepness_hidden(STEEPNESS_HIDDEN);
    net.set_activation_steepness_output(STEEPNESS_OUTPUT);
    net.set_activation_function_hidden(FANN::LINEAR);
    net.set_activation_function_output(FANN::LINEAR);
    net.set_training_algorithm(FANN::TRAIN_QUICKPROP);

    return true;
}

bool getInput(FANN::neural_net net){
    runs++;

    if(firstRun){
        firstRun = false;
        initQNeuralNet(net);

    }
    else{
        double reward = 0;

    }

}


/* Startup function. Syncronizes C and C++ output, calls the test function
   and reports any exceptions */
int main(int argc, char **argv)
{
    cout << "bla" << endl;
    cout << "start" << endl;
    try
    {
        std::ios::sync_with_stdio(); // Syncronize cout and printf output
        //xor_test();
    }
    catch (...)
    {
        cerr << endl << "Abnormal exception." << endl;
    }

    cout << "loaded" << endl;

    return 0;
}
