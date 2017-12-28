package heuristic;

public class Neuron {
	
	float[] mWeights;
	Neuron[] mConnectedNeurons;
	int mNumConnections;
	float mValue = 0;
	
	public Neuron(float[] weights, Neuron[] connectedNeurons, int numConnections, float value) {
		mWeights = weights;
		mConnectedNeurons = connectedNeurons;
		mNumConnections = numConnections;
		mValue = value;
	}
	
	
	public float[] getWeights() {
		return mWeights;
	}
	public void setWeights(float[] weights) {
		this.mWeights = weights;
	}
	public Neuron[] getConnectedNeurons() {
		return mConnectedNeurons;
	}
	public void setConnectedNeurons(Neuron[] mConnectedNeurons) {
		this.mConnectedNeurons = mConnectedNeurons;
	}
	public int getNumConnections() {
		return mNumConnections;
	}
	public void setNumConnections(int numConnections) {
		this.mNumConnections = numConnections;
	}
	public float getValue() {
		return mValue;
	}
	public void setValue(float value) {
		this.mValue = value;
	}
	
	

}
