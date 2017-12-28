package heuristic;

public class Net {

//struct fann_neuron *neuron_it, *first_neuron;

	int mNumInput = 0;
	Layer[] mLayers;
	
	
	
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
		
		numInput = net.mNumInput;
		
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
