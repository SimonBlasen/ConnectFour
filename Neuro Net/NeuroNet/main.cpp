#include "floatfann.h"
#include "fann_cpp.h"

#include <ios>
#include <iostream>
#include <iomanip>
using std::cout;
using std::cerr;
using std::endl;
using std::setw;
using std::left;
using std::right;
using std::showpos;
using std::noshowpos;


// Callback function that simply prints the information to cout
int print_callback(FANN::neural_net &net, FANN::training_data &train,
    unsigned int max_epochs, unsigned int epochs_between_reports,
    float desired_error, unsigned int epochs, void *user_data)
{
    cout << "Epochs     " << setw(8) << epochs << ". "
         << "Current Error: " << left << net.get_MSE() << right << endl;
    return 0;
}

// Test function that demonstrates usage of the fann C++ wrapper
void xor_test()
{
    cout << endl << "XOR test started." << endl;

    const float learning_rate = 0.7f;
    const unsigned int num_layers = 3;
    const unsigned int num_input = 2;
    const unsigned int num_hidden = 2;
    const unsigned int num_output = 1;
    const float desired_error = 0.01f;
    const unsigned int max_iterations = 1000;
    const unsigned int iterations_between_reports = 1000;

    cout << endl << "Creating network." << endl;

    FANN::neural_net net;
    //net.create_standard(num_layers, num_input, num_hidden, num_output);

    const unsigned int layers [3] = {num_input, num_hidden,num_output};

    net.create_standard_array(num_layers,layers);

    net.set_learning_rate(learning_rate);

    net.set_activation_steepness_hidden(1.0);
    net.set_activation_steepness_output(1.0);


    //net.set_activation_function_hidden(FANN::LINEAR);
    //net.set_activation_function_output(FANN::LINEAR);
    net.set_activation_function_hidden(FANN::SIGMOID_SYMMETRIC);
    net.set_activation_function_output(FANN::SIGMOID_SYMMETRIC);

    // Set additional properties such as the training algorithm
    net.set_training_algorithm(FANN::TRAIN_QUICKPROP);

    // Output network type and parameters
    cout << endl << "Network Type                         :  ";
    switch (net.get_network_type())
    {
    case FANN::LAYER:
        cout << "LAYER" << endl;
        break;
    case FANN::SHORTCUT:
        cout << "SHORTCUT" << endl;
        break;
    default:
        cout << "UNKNOWN" << endl;
        break;
    }
    net.print_parameters();

    cout << endl << "Training network." << endl;

    FANN::training_data data;
    if (data.read_train_from_file("D:/Dokumente/GitHub2/ConnectFour/Neuro Net/xor.data"))
    {
        cout << "in loop" << endl;

        // Initialize and train the network with the data
        net.init_weights(data);

        cout << "Max Epochs " << setw(8) << max_iterations << ". "
            << "Desired Error: " << left << desired_error << right << endl;
        net.set_callback(print_callback, NULL);
        net.train_on_data(data, max_iterations,
            iterations_between_reports, desired_error);

        cout << endl << "Testing network." << endl;

        for (unsigned int i = 0; i < data.length_train_data(); ++i)
        {
            // Run the network on the test data
            fann_type *calc_out = net.run(data.get_input()[i]);

            cout << "XOR test (" << showpos << data.get_input()[i][0] << ", "
                 << data.get_input()[i][1] << ") -> " << *calc_out
                 << ", should be " << data.get_output()[i][0] << ", "
                 << "difference = " << noshowpos
                 << fann_abs(*calc_out - data.get_output()[i][0]) << endl;
        }

        cout << endl << "Saving network." << endl;

        // Save the network in floating point and fixed point
        net.save("D:/Dokumente/xor_float.net");
        unsigned int decimal_point = net.save_to_fixed("xor_fixed.net");
        data.save_train_to_fixed("xor_fixed.data", decimal_point);

        cout << endl << "XOR test completed." << endl;
    }
}























void connectfour_training()
{
    cout << endl << "ConnectFour training started." << endl;


    const unsigned int cascade_max_neurons = 64;

    const float learning_rate = 0.35f;//0.7f;
    const unsigned int num_layers = 3;
    const unsigned int num_input = 64;
    const unsigned int num_hidden = 64;
    const unsigned int num_output = 1;
    const float desired_error = 0.0000040f;
    const unsigned int max_iterations = 100000;
    const unsigned int iterations_between_reports = 100;

    cout << endl << "Creating network." << endl;

    FANN::neural_net net;
    //net.create_standard(num_layers, num_input, num_hidden, num_output);

    //const unsigned int layers [2] = {num_input,num_output};
    const unsigned int layers [3] = {num_input,num_hidden,num_output};

    //net.create_shortcut_array(2,layers);
    net.create_standard_array(num_layers,layers);

    net.set_learning_rate(learning_rate);
    net.randomize_weights(-2.0, 2.0);

    net.set_activation_steepness_hidden(0.5);
    net.set_activation_steepness_output(0.5);

    net.set_activation_function_hidden(FANN::SIGMOID_SYMMETRIC_STEPWISE);
    net.set_activation_function_output(FANN::SIGMOID_SYMMETRIC_STEPWISE);

    // Set additional properties such as the training algorithm
    net.set_training_algorithm(FANN::TRAIN_QUICKPROP);


    // Output network type and parameters
    cout << endl << "Network Type                         :  ";
    switch (net.get_network_type())
    {
    case FANN::LAYER:
        cout << "LAYER" << endl;
        break;
    case FANN::SHORTCUT:
        cout << "SHORTCUT" << endl;
        break;
    default:
        cout << "UNKNOWN" << endl;
        break;
    }
    net.print_parameters();

    cout << endl << "Training network." << endl;

    FANN::training_data data;
    if (data.read_train_from_file("D:/Dokumente/connectfour_float.data"))
    {
        cout << "in loop" << endl;

        data.scale_input_train_data(-1.0, 1.0);

        // Initialize and train the network with the data
        net.init_weights(data);

        cout << "Max Epochs " << setw(8) << max_iterations << ". "
            << "Desired Error: " << left << desired_error << right << endl;
        net.set_callback(print_callback, NULL);

        //net.cascadetrain_on_data(data, cascade_max_neurons, iterations_between_reports, desired_error);

        net.train_on_data(data, max_iterations,
            iterations_between_reports, desired_error);

        cout << endl << "Testing network." << endl;

        for (unsigned int i = 0; i < 128; ++i)
        {
            // Run the network on the test data
            fann_type *calc_out = net.run(data.get_input()[i]);

            cout << "XOR test (" << showpos << data.get_input()[i][0] << ", "
                 << data.get_input()[i][1] << ") -> " << *calc_out
                 << ", should be " << data.get_output()[i][0] << ", "
                 << "difference = " << noshowpos
                 << fann_abs(*calc_out - data.get_output()[i][0]) << endl;
        }

        cout << endl << "Saving network." << endl;

        // Save the network in floating point and fixed point
        net.save("D:/Dokumente/connectfour_float.net");
        //unsigned int decimal_point = net.save_to_fixed("xor_fixed.net");
        //data.save_train_to_fixed("xor_fixed.data", decimal_point);

        cout << endl << "ConnectFour training completed." << endl;
    }
}

/* Startup function. Syncronizes C and C++ output, calls the test function
   and reports any exceptions */
int main(int argc, char **argv)
{
    try
    {
        std::ios::sync_with_stdio(); // Syncronize cout and printf output

        //xor_test();

        connectfour_training();
    }
    catch (...)
    {
        cerr << endl << "Abnormal exception." << endl;
    }
    return 0;
}
