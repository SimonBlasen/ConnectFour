package heuristic;

public class Neuron {
	
	//private float[] mWeights;
	//private Neuron[] mConnectedNeurons;
	//private int mNumConnections;
	private float mValue = 0;
	private int mBeginConnectionsIndex;
	private float mActivationSteepness;
	
	public Neuron(float value, int beginConnectionsIndex, float activationSteepness) {
		//mWeights = weights;
		//mConnectedNeurons = connectedNeurons;
		//mNumConnections = numConnections;
		mValue = value;
		mBeginConnectionsIndex = beginConnectionsIndex;
		mActivationSteepness = activationSteepness;
	}
	
	
	public int getBeginConnectionsIndex()
	{
		return mBeginConnectionsIndex;
	}
	
	/*public float[] getWeights() {
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
	}*/
	public float getValue() {
		return mValue;
	}
	public void setValue(float value) {
		this.mValue = value;
	}
	public float getActivationSteepness()
	{
		return mActivationSteepness;
	}
	

}
