#include "floatfann.h"
#include "fann_cpp.h"
#include <vector>
#include <ios>
#include <iostream>
#include <iomanip>

#include "game.h"

using std::cout;
using std::cerr;
using std::endl;
using std::setw;
using std::left;
using std::right;
using std::showpos;
using std::noshowpos;

using namespace std;




// Callback function that simply prints the information to cout
int print_callback(FANN::neural_net &net, FANN::training_data &train,
    unsigned int max_epochs, unsigned int epochs_between_reports,
    float desired_error, unsigned int epochs, void *user_data)
{
    cout << "Epochs     " << setw(8) << epochs << ". "
         << "Current Error: " << left << net.get_MSE() << right << endl;
    return 0;
}





/* Startup function. Syncronizes C and C++ output, calls the test function
   and reports any exceptions */
int main(int argc, char **argv)
{

    Game g;
    Game::play();

    cout << "game is fine" << endl;




    try
    {
        std::ios::sync_with_stdio(); // Syncronize cout and printf output
        //xor_test();
    }
    catch (...)
    {
        cerr << endl << "Abnormal exception." << endl;
    }

    return 0;
}
