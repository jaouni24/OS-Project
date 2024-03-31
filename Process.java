//Hiba Jaouni 1201154
//Maisam Alaa' 120650
//Submission Date: 12/2/2023
//section : 2

package OS_Project;

import java.util.Arrays;

public class Process {
	final private double alpha = 0.5;
	
	private static int counter = -1;
	private int PID;
	
	private int arrivalTime;
	private int maxCPUBurst;
	
	private int []CPUBurst;
	private int []IOBurst;
	
	private int maxArrival;
	
	private int maxIO,minIO;
	private int minCPU,maxCPU;
	
	private int state;
	
	private int timeQuantaForOneCPUBurst;
	
	private int preemption = 0;
	private double estimatedTime;
	
	private int arrivedIO = 0;
	private int leftIO = 0;
	
	public Process(int maxArrival,int maxCPUBurst, int maxIO, int minIO, int minCPU, int maxCPU) {
		super();
		this.maxArrival = maxArrival;
		this.maxCPUBurst = maxCPUBurst;
		this.maxIO = maxIO;
		this.minIO = minIO;
		this.minCPU = minCPU;
		this.maxCPU = maxCPU;
		
		this.PID = counter + 1;
		counter++;
		
		arrivalTime = (int)(Math.random() * (maxArrival + 1));
		
		int size = (int)(Math.random() * maxCPUBurst) + 1;
		CPUBurst = new int[size];
		IOBurst = new int [size - 1];
		
		setCPUBurstOriginal(CPUBurst);
		setIOBurstOriginal(IOBurst);
	}
	
	public Process(int PID, int arrivalTime, int[] CPUBurst, int[] IOBurst) {
		super();
		this.PID = PID;
		this.arrivalTime = arrivalTime;
		this.CPUBurst = CPUBurst;
		this.IOBurst = IOBurst;
	}

	public int getPID() {
		return PID;
	}
	

	private int[] updateSizeCPUBurst(int[] CPUBurst) { //after finishing with the burst
		int []newCPUBurst;
		newCPUBurst = new int [CPUBurst.length - 1];
		for(int i = 0; i <CPUBurst.length - 1;i++) {
			newCPUBurst[i] = CPUBurst[i+1];
		}
		return newCPUBurst;
	}
	
	private int[] updateSizeIOBurst(int[] IOBurst) { //after finishing with the burst
		int []newIOBurst;
		newIOBurst = new int [IOBurst.length - 1];
		for(int i = 0; i <IOBurst.length - 1;i++) {
			newIOBurst[i] = IOBurst[i+1];
		}
		return newIOBurst;
	}

	public int[] getCPUBurst() {
		return CPUBurst;
	}
	
	
	public void setCPUBurst(int[] CPUBurst) {
		this.CPUBurst = updateSizeCPUBurst(CPUBurst);
	}

	public void setIOBurst(int[] IOBurst) {
		this.IOBurst = updateSizeIOBurst(IOBurst);
	}

	private void setCPUBurstOriginal(int[] CPUBurst) {
		int value;
		for (int i = 0; i < CPUBurst.length; i++) {
			value = (int)(Math.random() * (maxCPU - minCPU + 1)) + minCPU;
			CPUBurst[i] = value;
		}
		
	}

	public int[] getIOBurst() {
		return IOBurst;
	}
	
	public int getPreemption() {
		return preemption;
	}

	public void incrementPreemption() {
		this.preemption = preemption++;
	}

	private void setIOBurstOriginal(int[] IOBurst) {
		int value;
		for (int i = 0; i < IOBurst.length; i++) {
			value = (int)(Math.random() * (maxIO - minIO + 1)) + minIO;
			IOBurst[i] = value;
		}
	}	

	public double getEstimatedTime() {
		return estimatedTime;
	}

	//needs work
	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public double getAlpha() {
		return alpha;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getMaxArrival() {
		return maxArrival;
	}

	public int getMaxIO() {
		return maxIO;
	}

	public int getMinIO() {
		return minIO;
	}

	public int getMinCPU() {
		return minCPU;
	}

	public int getMaxCPU() {
		return maxCPU;
	}
	

	public int getArrivedIO() {
		return arrivedIO;
	}

	public void setArrivedIO(int arrivedIO) {
		this.arrivedIO = arrivedIO;
	}

	public int getLeftIO() {
		return leftIO;
	}

	public void setLeftIO(int leftIO) {
		this.leftIO = leftIO;
	}

	public int getTimeQuantaForOneCPUBurst() {
		return timeQuantaForOneCPUBurst;
	}

	public void setTimeQuantaForOneCPUBurst(int timeQuantaForOneCPUBurst) { //only increment time Quantum by one
		this.timeQuantaForOneCPUBurst = timeQuantaForOneCPUBurst;
	}

	@Override
	public String toString() {
		return "Process [PID=" + PID + ", arrivalTime=" + arrivalTime + ", CPUBurst=" + Arrays.toString(CPUBurst)
				+ ", IOBurst=" + Arrays.toString(IOBurst) + "]";
	}
}
