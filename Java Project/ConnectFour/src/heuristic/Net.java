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

		Layer layerIt;
		Layer lastLayer;
		
		int i = 0;
		int numConnections  =0;
		int numInput = 0;
		int numOutput = 0;


		float neuronSum = 0;
		float output = 0;
		float[] weights;
		float steepness = 0;

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
		
		lastLayer = net.mLayers[net.mLayers.length-1];
		
		
		
		




		return 0f;
	}

}
