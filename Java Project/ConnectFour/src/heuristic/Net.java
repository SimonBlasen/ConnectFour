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
		
		
		for (int i = 0; i < list.size(); i++)
		{
			String decl = list.get(i).split("=").length == 2 ? list.get(i).split("=")[0] : "";
			String val = list.get(i).split("=").length == 2 ? list.get(i).split("=")[1] : "";
			
			try
			{
				if (decl.equals("learning_rate"))
				{
					learning_rate = Float.valueOf(val);
				}
				
				else if (decl.equals("learning_momentum"))
				{
					learning_momentum = Float.valueOf(val);
				}
			}
			catch (Exception ex)
			{
				System.out.println("Error reading net config file at \"" + configFile + "\"");
			}
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
		float[] weights = net.weights;
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
				
				weights = currentNeuron.getWeights(); //TODO fix
				
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
					neuronSum += weights[2] * neurons[2].getValue();
				case 2:
					neuronSum += weights[1] * neurons[1].getValue();
				
				case 1:
					neuronSum += weights[0] * neurons[0].getValue();
					
				case 0:
					break;
					
				}
				
				for(; i < numConnections; i+=4) {
					
					neuronSum += weights[i] * neurons[i].getValue() 
							+ weights[i+1] * neurons[i+1].getValue()
							+ weights[i+2] * neurons[i+2].getValue()
							+ weights[i+3] * neurons[i+3].getValue();
				}
				
				neuronSum += (steepness * neuronSum);
				
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
