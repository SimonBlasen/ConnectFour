package heuristic;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Net {

//struct fann_neuron *neuron_it, *first_neuron;

	//private int mNumInput = 0;
	private Layer[] mLayers;
	
	
	// Parameters in struct fann
	//
	
	private float learning_rate;
	private float learning_momentum;
	private float connection_rate;
	private Nettype_Enum network_type;
	private int total_neurons;
	private int num_inputs;
	private int num_outputs;
	private float[] weights;
	
	
	private Layer first_layer;
	private Layer last_layer;
	
	//
	//	
	
	
	public Net(String configFile)
	{
		List<String> list = new ArrayList<>();

		try (BufferedReader br = Files.newBufferedReader(Paths.get(configFile))) {

			//br returns as stream and convert it into a List
			list = br.lines().collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int num_layers = 0;
		int network_type = 0;
		int training_algorithm = 0;
		int train_error_function = 0;
		int train_stop_function = 0;
		float connection_rate = 0.0f;
		float cascade_output_change_fraction = 0.0f;
		float quickprop_decay = 0.0f;
		float quickprop_mu = 0.0f;
		float rprop_increase_factor = 0.0f;
		float rprop_decrease_factor = 0.0f;
		float rprop_delta_min = 0.0f;
		float rprop_delta_max = 0.0f;
		float rprop_delta_zero = 0.0f;
		int cascade_output_stagnation_epochs = 0;
		float cascade_candidate_change_fraction = 0.0f;
		int cascade_candidate_stagnation_epochs = 0;
		int cascade_max_out_epochs = 0;
		int cascade_min_out_epochs = 0;
		int cascade_max_cand_epochs = 0;
		int cascade_min_cand_epochs = 0;
		int cascade_num_candidate_groups = 0;
		float bit_fail_limit = 0.0f;
		float cascade_candidate_limit = 0.0f;
		float cascade_weight_multiplier = 0.0f;
		int cascade_activation_functions_count = 0;
		int[] cascade_activation_functions = new int[0];
		int cascade_activation_steepnesses_count = 0;
		float[] cascade_activation_steepnesses = new float[0];
		int[] layer_sizes = new int[0];
		int scale_included = 0;
		ParserNeurons[] neurons = new ParserNeurons[0];
		ParserConnections[] connections = new ParserConnections[0];
		
		
		
		mLayers = new Layer[num_layers];
		
		
		for (int i = 0; i < list.size(); i++)
		{
			String decl = list.get(i).split("=").length == 2 ? list.get(i).split("=")[0] : "";
			String val = list.get(i).split("=").length == 2 ? list.get(i).split("=")[1] : "";
			
			if (decl.length() > 0 && val.length() > 0)
			{
				try
				{
					if (decl.equals("learning_rate"))
					{
						learning_rate = Float.valueOf(val);
					}
					
					else if (decl.equals("connections (connected_to_neuron, weight)"))
					{
						String[] vals = val.split("\\) \\(");
						connections = new ParserConnections[vals.length];
						for (int j = 0; j < vals.length; j++)
						{
							connections[j] = new ParserConnections();
							
							String[] innerVals = vals[j].split(",");
							for (int k = 0; k < innerVals.length; k++)
							{
								String rawValue = innerVals[k].replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
								
								if (k == 0)
								{
									connections[j].connected_to_neuron = Integer.valueOf(rawValue);
								}
								else if (k == 1)
								{
									connections[j].weight = Float.valueOf(rawValue);
								}
							}
						}
					}
					
					else if (decl.equals("neurons (num_inputs, activation_function, activation_steepness)"))
					{
						String[] vals = val.split("\\) \\(");
						neurons = new ParserNeurons[vals.length];
						for (int j = 0; j < vals.length; j++)
						{
							neurons[j] = new ParserNeurons();
							
							String[] innerVals = vals[j].split(",");
							for (int k = 0; k < innerVals.length; k++)
							{
								String rawValue = innerVals[k].replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
								
								if (k == 0)
								{
									neurons[j].num_inputs = Integer.valueOf(rawValue);
								}
								else if (k == 1)
								{
									neurons[j].activation_function = Integer.valueOf(rawValue);
								}
								else if (k == 2)
								{
									neurons[j].activation_steepness = Float.valueOf(rawValue);
								}
							}
						}
					}
					
					else if (decl.equals("scale_included"))
					{
						scale_included = Integer.valueOf(val);
					}
					
					else if (decl.equals("layer_sizes"))
					{
						String[] vals = val.split(" ");
						layer_sizes = new int[vals.length];
						for (int j = 0; j < vals.length; j++)
						{
							layer_sizes[j] = Integer.valueOf(vals[j]);
						}
					}
					
					else if (decl.equals("cascade_activation_steepnesses"))
					{
						String[] vals = val.split(" ");
						cascade_activation_steepnesses = new float[vals.length];
						for (int j = 0; j < vals.length; j++)
						{
							cascade_activation_steepnesses[j] = Float.valueOf(vals[j]);
						}
					}
					
					else if (decl.equals("cascade_activation_steepnesses_count"))
					{
						cascade_activation_steepnesses_count = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_activation_functions"))
					{
						String[] vals = val.split(" ");
						cascade_activation_functions = new int[vals.length];
						for (int j = 0; j < vals.length; j++)
						{
							cascade_activation_functions[j] = Integer.valueOf(vals[j]);
						}
					}
					
					else if (decl.equals("cascade_activation_functions_count"))
					{
						cascade_activation_functions_count = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_candidate_limit"))
					{
						cascade_candidate_limit = Float.valueOf(val);
					}
					
					else if (decl.equals("cascade_weight_multiplier"))
					{
						cascade_weight_multiplier = Float.valueOf(val);
					}
					
					else if (decl.equals("bit_fail_limit"))
					{
						bit_fail_limit = Float.valueOf(val);
					}
					
					else if (decl.equals("cascade_max_out_epochs"))
					{
						cascade_max_out_epochs = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_min_out_epochs"))
					{
						cascade_min_out_epochs = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_max_cand_epochs"))
					{
						cascade_max_cand_epochs = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_min_cand_epochs"))
					{
						cascade_min_cand_epochs = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_num_candidate_groups"))
					{
						cascade_num_candidate_groups = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_candidate_stagnation_epochs"))
					{
						cascade_candidate_stagnation_epochs = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_candidate_change_fraction"))
					{
						cascade_candidate_change_fraction = Float.valueOf(val);
					}
					
					else if (decl.equals("cascade_output_stagnation_epochs"))
					{
						cascade_output_stagnation_epochs = Integer.valueOf(val);
					}
					
					else if (decl.equals("rprop_delta_min"))
					{
						rprop_delta_min = Float.valueOf(val);
					}
					
					else if (decl.equals("rprop_delta_max"))
					{
						rprop_delta_max = Float.valueOf(val);
					}
					
					else if (decl.equals("rprop_delta_zero"))
					{
						rprop_delta_zero = Float.valueOf(val);
					}
					
					else if (decl.equals("rprop_decrease_factor"))
					{
						rprop_decrease_factor = Float.valueOf(val);
					}
					
					else if (decl.equals("rprop_increase_factor"))
					{
						rprop_increase_factor = Float.valueOf(val);
					}
					
					else if (decl.equals("quickprop_mu"))
					{
						quickprop_mu = Float.valueOf(val);
					}
					
					else if (decl.equals("learning_momentum"))
					{
						learning_momentum = Float.valueOf(val);
					}
					
					else if (decl.equals("num_layers"))
					{
						num_layers = Integer.valueOf(val);
					}
					
					else if (decl.equals("connection_rate"))
					{
						connection_rate = Float.valueOf(val);
					}
					
					else if (decl.equals("network_type"))
					{
						network_type = Integer.valueOf(val);
					}
					
					else if (decl.equals("training_algorithm"))
					{
						training_algorithm = Integer.valueOf(val);
					}
					
					else if (decl.equals("train_error_function"))
					{
						train_error_function = Integer.valueOf(val);
					}
					
					else if (decl.equals("train_stop_function"))
					{
						train_stop_function = Integer.valueOf(val);
					}
					
					else if (decl.equals("cascade_output_change_fraction"))
					{
						cascade_output_change_fraction = Float.valueOf(val);
					}
					
					else if (decl.equals("quickprop_decay"))
					{
						quickprop_decay = Float.valueOf(val);
					}
				}
				catch (Exception ex)
				{
					System.out.println("Error reading net config file at \"" + configFile + "\"");
				}
			}
			
			
		}
		
		int afdsfds = 0;
		afdsfds++;
		
		if (afdsfds == 0)
		{
			
		}
	}
	
	public static float run(Net net, int[] input) {
		
		Neuron neuronIt;
		Neuron lastNeuron;
		Neuron[] neurons; //????

		Layer layerIt; //Todo remove
		Layer lastLayer;
		
		int i = 0;
		int numConnections  =0;
		int numInput = 0;
		int numOutput = 0;


		float neuronSum = 0;
		//float[] weights = net.weights;
		float steepness = 0.5f; //default value

		float maxSum = 0;
		
		numInput = net.num_inputs;
		
		//for fast acces:
		Neuron firstNeuron = net.mLayers[0].getFirstNeuron();
		

		
		//fann neuron firstNeuron = net...firstNeuron
		
		for(i = 0; i < numInput; i++) {
			net.mLayers[0].getNeuron(i).setValue(input[i]);
		}
		
		
		
		//set bias neuron in input layer
		net.mLayers[0].getLastNeuron().setValue(1);
		
		lastLayer = net.mLayers[net.mLayers.length-1]; //TODO remove
		
		
		
		for(int j= 0; j < net.mLayers.length; j++) {
			lastNeuron =  net.mLayers[j].getLastNeuron();
			//TODO refactor bias neuron aus schleife rausnehmen
			
			for(int k = 0; k < net.mLayers[j].getNeurons().length; k++) {
				Neuron currentNeuron = net.mLayers[j].getNeurons()[k];
				
				if(currentNeuron.equals(net.mLayers[j].getLastNeuron())) {
					//bias neurons
					currentNeuron.setValue(1);
					
					continue;
				}
				
				//activation function 
				//not needed cz we use the same function for each neuron
				//steepnes..
				
				neuronSum = 0;
				
				numConnections = currentNeuron.getNumConnections();
				
				int weightsIndex = currentNeuron.getBeginConnectionsIndex();
				//net.weights = currentNeuron.getWeights(); //TODO fix
				
				//TODO was ist in unserem Fall FANN_NETTYPE_SHORTCUT?
				
				if(net.network_type == Nettype_Enum.FANN_NETTYPE_SHORTCUT) {
					neurons = net.mLayers[0].getNeurons();
				}
				else {
					neurons = net.mLayers[j-1].getNeurons();
				}
				
				i = numConnections % 4;
				switch(i)
				{
				case 3:
					neuronSum += net.weights[weightsIndex + 2] * neurons[2].getValue();
				case 2:
					neuronSum += net.weights[weightsIndex + 1] * neurons[1].getValue();
				
				case 1:
					neuronSum += net.weights[weightsIndex + 0] * neurons[0].getValue();
					
				case 0:
					break;
					
				}
				
				for(; i < numConnections; i+=4) {
					
					neuronSum += net.weights[weightsIndex + i] * neurons[i].getValue() 
							+ net.weights[weightsIndex + i+1] * neurons[i+1].getValue()
							+ net.weights[weightsIndex + i+2] * neurons[i+2].getValue()
							+ net.weights[weightsIndex + i+3] * neurons[i+3].getValue();
				}
				
				neuronSum = (steepness * neuronSum);
				
				maxSum = 150.0f / steepness;
				if(neuronSum > maxSum) {
					neuronSum = maxSum;
				}
				else if(neuronSum < - maxSum){
					neuronSum = -maxSum;
				}
				
				//neuron_it->sum = neuron_sum; ?? Member sum existiert nicht
				
				currentNeuron.setValue(activationFunction(neuronSum));
			}		
		}
		
		//set output
		// vereinfacht weil nur ein output neuron
		
		return net.mLayers[net.mLayers.length-1].getNeuron(0).getValue();

	}
	
	public static float activationFunction(float value) {
		return (float) (2.0f/(1.0f + Math.exp(-2.0f * value)) - 1.0f);
	}

}
