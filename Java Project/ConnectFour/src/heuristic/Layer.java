package heuristic;

public class Layer {
	
	//TODO remove first and last cz only needed for C
	private Neuron mFirstNeuron;
	private Neuron mLastNeuron;
	private Neuron[] mNeurons;
	
	public Layer(Neuron firstNeuron, Neuron lastNeuron, Neuron[] neurons) {
		mFirstNeuron = firstNeuron;
		mLastNeuron = lastNeuron;
		mNeurons = neurons;
	}
	
	public Neuron[] getNeurons() {
		return mNeurons;
	}
	
	public Neuron getNeuron(int index) {
		return mNeurons[index];
	}
	
	
	public Neuron getFirstNeuron() {
		return mFirstNeuron;
	}
	public void setmFirstNeuron(Neuron firstNeuron) {
		this.mFirstNeuron = firstNeuron;
	}
	public Neuron getLastNeuron() {
		return mLastNeuron;
	}
	public void setmLastNeuron(Neuron lastNeuron) {
		this.mLastNeuron = lastNeuron;
	}

}
