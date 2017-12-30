package heuristic;

public class Layer {
	
	private Neuron[] mNeurons;
	
	public Layer(Neuron[] neurons) {
		mNeurons = neurons;
	}
	
	public Neuron[] getNeurons() {
		return mNeurons;
	}
	
	public Neuron getNeuron(int index) {
		return mNeurons[index];
	}
	
	
	public Neuron getFirstNeuron() {
		return mNeurons[0];
	}
	public Neuron getLastNeuron() {
		return mNeurons[mNeurons.length - 1];
	}

}
