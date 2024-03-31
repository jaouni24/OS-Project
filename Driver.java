//Hiba Jaouni 1201154
//Submission Date: 12/2/2023
//section : 2

package OS_Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;           //These are all the libraries we need 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Driver {

	static Scanner input = new Scanner(System.in);
	
	final static int numberOfTimeQuanta = 10;
	
	static int numberOfProcesses;
	static int maxArrivalTime;
	static int maxCPUBurst;
	static int minIO,maxIO;
	static int minCPU,maxCPU;         
	
	static int waitingTime = 0;
	
	static int counter = 0;
	static int PID_unfinishedProcess;
	static boolean enteredProcessToQ3 = false;
	static boolean leftProcessFromQ3 = false;
	
	static int previousState = 0;
	static int previousPID = 0;
	
	static String CPUExcutionQ1 = "";
	static String CPUExcutionQ2 = "";
	static String CPUExcutionQ3 = "";
	static String CPUExcutionQ4 = "";
	static String ExcutingIO = "";
	static String Finished = "";
//----------------------------------------------------------------------------	
//This is the main method in our program, it depends on the user's choice
	public static void main(String[] args) throws InvalidInputException, IOException, InterruptedException {
		
		int option = options();
		if(option == 1) {
			start();
			Process []processes1;
			processes1 = new Process[numberOfProcesses];
			defineArray(processes1,maxArrivalTime,maxCPUBurst,minIO,maxIO,minCPU,maxCPU);
			print(processes1);
			writeToFile(processes1);
			startScheduling(processes1);
		}
		
		if(option == 2) {
			Process []processes2;
			processes2 = readFile();
			print(processes2);
			startScheduling(processes2);
		}
		
	}
//-------------------------------------------------------------------------------
//This process prints all the processes including id,arrival time,cpu bursts and io bursts.
	public static void print(Process []processes) {
		System.out.println("-------------------------------------------------------------------------------------");
		for (int i = 0; i < processes.length; i++) 
			System.out.println(processes[i].toString());
		System.out.println("-------------------------------------------------------------------------------------");
	}
//----------------------------------------------------------------------------------
	
	public static void printProcessToString(int currentState,int currentPID,int time,boolean start) {
		if(currentState == previousState && currentPID == previousPID && start) {
			//not close the duration of the working process
		}
		else if(currentState == previousState && start) { //but different PID's
			//close the Q of the state, start with the new process
			if(currentState == 0) {
				CPUExcutionQ1 += (time-1)+"] ";
				CPUExcutionQ1 += "| P"+currentPID+"["+time+":";
			}
			else if(currentState == 1) {
				CPUExcutionQ2 += (time-1)+"] ";
				CPUExcutionQ2 += "| P"+currentPID+"["+time+":";
			}
			else if(currentState == 2) {
				CPUExcutionQ3 += (time-1)+"] ";
				CPUExcutionQ3 += "| P"+currentPID+"["+time+":";
			}
			else if(currentState == 3) {
				CPUExcutionQ4 += (time-1)+"] ";
				CPUExcutionQ4 += "| P"+currentPID+"["+time+":";
			}
		}
		else if(currentState != previousState && start) { 
			//close the open Q
			//open the new one according to the state
			if(previousState == 0)
				CPUExcutionQ1 += (time-1)+"] ";
			else if (previousState == 1)
				CPUExcutionQ2 += (time-1)+"] ";
			else if (previousState == 2)
				CPUExcutionQ3 += (time-1)+"] ";
			else if (previousState == 3)
				CPUExcutionQ4 += (time-1)+"] ";
			
			if(currentState == 0) 
				CPUExcutionQ1 += "| P"+currentPID+"["+time+":";
			else if(currentState == 1) 
				CPUExcutionQ2 += "| P"+currentPID+"["+time+":";
			else if(currentState == 2) 
				CPUExcutionQ3 += "| P"+currentPID+"["+time+":";
			else if(currentState == 3) 
				CPUExcutionQ4 += "| P"+currentPID+"["+time+":";
		}
		previousPID = currentPID;
		previousState = currentState;
	}
// In this method we give the user the choice between reading from file or Generate data randomly
	public static int options() throws InvalidInputException{     
		System.out.println("Do you want to read from file or Generate data randomly?");
		System.out.println("Press (1) for Random Gereration.");
		System.out.println("Press (2) for reading from file.");
		int option = 0;
		try {
			option = input.nextInt();
			if(option > 2 || option < 0) {           
				throw new InvalidInputException("Input Error!");
			}
		}catch(InvalidInputException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}catch(InputMismatchException e) {
			System.out.println("Invalid input!");
			System.exit(0);
		}
		return option;
	}
//----------------------------------------------------------------------------------------
//This method reads the date from the file 
	public static Process[] readFile() throws IOException {
		Process []processes;
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.println("Please enter the name of the file(xxx.txt) that you to read the data from: ");
		String fileName = input.next();
//------------------------------------------------------------------------------------------
	  	ArrayList <String> processesFromFile = new ArrayList<String>();
	  	
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferReader = new BufferedReader(fileReader);
		String lineFile;
            
	    try{    
	    	while((lineFile = bufferReader.readLine()) != null)
	           	processesFromFile.add(lineFile);
	    	
	    } catch (FileNotFoundException e) {
	    System.out.println("An error occurred.");
	    e.getMessage();
	    }finally {
	   	bufferReader.close();
	    }
	      
	    processes = new Process[processesFromFile.size()];
	    
	    for (int i = 0; i < processesFromFile.size(); i++) {
	    	String [] line = processesFromFile.get(i).split(" ");
	    	int sizeOfLine = line.length;
	    	int IOSize = (sizeOfLine - 2) / 2;
	    	int []CPUBurst = new int[IOSize + 1];
	    	int []IOBurst = new int[IOSize];
	    	int counter1 = 0;
	    	int counter2 = 0;
	    	for(int j = 2 ; j<line.length ; j++) {
	    		if(j%2 == 0) {
	    			CPUBurst[counter1] = Integer.parseInt(line[j]);
	    			counter1++;
	    		}
	    		else if(j%2 == 1) {
	    			IOBurst[counter2] = Integer.parseInt(line[j]);
	    			counter2++;
	    		}
	    	}
	    	
	    	processes[i] = new Process(Integer.parseInt(line[0]),Integer.parseInt(line[1]),CPUBurst,IOBurst);
		}
	       
		return processes;
	}
//--------------------------------------------------------------------------------------------
//This method has the work of the four queues
		public static void startScheduling(Process []processes) throws InvalidInputException, IOException, InterruptedException{		
		int timeQuantum1 = 0;
		int timeQuantum2 = 0;
		int preemption = 0;
		double estimatedTime;
		
		int time;
		int lostTime = 0;
		
		ArrayList<Process> RR1 = new ArrayList<>();
		ArrayList<Process> RR2 = new ArrayList<>();
		ArrayList<Process> SRTF = new ArrayList<>();
		ArrayList<Process> FCFS = new ArrayList<>();
		
		ArrayList<Process> IOExcution = new ArrayList<>();
		ArrayList<Process> FinishedProcesses = new ArrayList<>();
		
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.println("Now we are going to start schedualing......");
		System.out.println();
		try {
			Thread.sleep(2000);
		}catch (InterruptedException e) {
			System.out.println("Error!");
		}
		System.out.println("We are going to start with Round Robin Algorithm,");
		try {
			Thread.sleep(2000);
		}catch (InterruptedException e) {
			System.out.println("Error!");
		}
		System.out.println("we are going to simulate 2 Round Robin Queues but with differant time quantums.");
		try {
			Thread.sleep(2500);
		}catch (InterruptedException e) {
			System.out.println("Error!");
		}
		System.out.println("Then we are going to use Shortest Remaining Time First Algorithm, and the last one is");
		try {
			Thread.sleep(2500);
		}catch (InterruptedException e) {
			System.out.println("Error!");
		}
		System.out.println("First Come First Served (FCFS) Algorithm.");
		try {
			Thread.sleep(2500);
		}catch (InterruptedException e) {
			System.out.println("Error!");
		}
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.println();
		
		
		try {
		System.out.println("So, please enter the time quantime for the first Round Robin Queue: ");
		timeQuantum1 = input.nextInt();
		if(timeQuantum1 <= 0)
			throw new InvalidInputException("Time Quantum must be larger than 0.");
		
		System.out.println("Please enter the time quantime for the second Round Robin Queue: ");
		timeQuantum2 = input.nextInt();
		if(timeQuantum2 <= 0)
			throw new InvalidInputException("Time Quantum must be larger than 0.");
		
		System.out.println("Please enter max # of preemption for shortest-remaining-time first algorithm: ");
		preemption = input.nextInt();
		}catch(InvalidInputException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}catch(InputMismatchException e) {
			System.out.println("Invalid input!");
			System.exit(0);
		}
		
		estimatedTime = 10;
		
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.println();
		boolean start = false;
		for(time = 0;  ;time++) {
			
			if(FinishedProcesses.size() == processes.length) {
				if(FinishedProcesses.get(FinishedProcesses.size()-1).getState() == 0)
					CPUExcutionQ1 += (time-1)+"] ";
				else if (FinishedProcesses.get(FinishedProcesses.size()-1).getState() == 1)
					CPUExcutionQ2 += (time-1)+"] ";
				else if (FinishedProcesses.get(FinishedProcesses.size()-1).getState() == 2)
					CPUExcutionQ3 += (time-1)+"] ";
				else if (FinishedProcesses.get(FinishedProcesses.size()-1).getState() == 3)
					CPUExcutionQ4 += (time-1)+"] ";
				System.out.println();
				System.out.println();
				break;
			}
			
			
			if(RR1.size() != processes.length) //RR1 is filled with all processes
				for (int i = 0; i < processes.length; i++)   //check if the process arrived to CPU
					if(processes[i].getArrivalTime() == time) {
						if(start == false) {
							previousPID = processes[i].getPID();
							previousState = processes[i].getState();
							CPUExcutionQ1 += "| P"+processes[i].getPID()+"["+time+":";
							start = true;
						}
						
						RR1.add(processes[i]);
					}
			if(!IOExcution.isEmpty()) { //decrement IO by 1 
				for (int i = 0; i < IOExcution.size(); i++) {
					if(IOExcution.get(i).getIOBurst()[0] == 0) {
						IOExcution.get(i).setIOBurst(IOExcution.get(i).getIOBurst());
						if(IOExcution.get(i).getIOBurst().length >= 1 && IOExcution.get(i).getState() == 2) {
							Process temp = IOExcution.get(i);
							double estimateTime = 
							(temp.getAlpha() * temp.getCPUBurst()[0]) + ((1-temp.getAlpha())*temp.getEstimatedTime());
							temp.setEstimatedTime(estimateTime);
						}
						IOExcution.get(i).setLeftIO(time);
						ExcutingIO += "| P"+IOExcution.get(i).getPID()+"["+IOExcution.get(i).getArrivedIO()+
								":"+IOExcution.get(i).getLeftIO()+"] ";
						returnProcessToTheQueue(IOExcution.get(i),RR1,RR2,SRTF,FCFS);
						IOExcution.remove(IOExcution.get(i));
					}
					else 
						IOExcution.get(i).getIOBurst()[0]--;
				}
			}
//--------------------------------------------------------------------------------		
			if(!RR1.isEmpty()) {    //There are processes arrived to first queue
				waitingTime += RR1.size() - 1 + RR2.size() + SRTF.size() + FCFS.size();
				RRQueueWork(time,0,timeQuantum1,estimatedTime,RR1,RR2,IOExcution,FinishedProcesses);
				continue;
			}
//----------------------------------------------------------------------------------------
			if(!RR2.isEmpty()) {    //There are processes arrived to secound queue
				waitingTime += RR2.size() - 1 + SRTF.size() + FCFS.size();
				RRQueueWork(time,1,timeQuantum2,estimatedTime,RR2,SRTF,IOExcution,FinishedProcesses);
				continue;
			}
//-----------------------------------------------------------------------------------------	
			if(!SRTF.isEmpty()) {           //There are processes arrived to third queue
				waitingTime += SRTF.size() - 1 + FCFS.size();
				Process CPUProcess;
				CPUProcess = SRTF.get(0);
				boolean processNotFinished = false;
				SRTFQueueWork(time,2,preemption,SRTF,FCFS,IOExcution,FinishedProcesses,CPUProcess,
						processNotFinished);
				continue;
			}
//---------------------------------------------------------------------------------------------	
			if(!FCFS.isEmpty()) {          //There are processes arrived to fourth queue
				waitingTime += FCFS.size() - 1;
				FCFSQueueWork(time,2,FCFS,IOExcution,FinishedProcesses);
				continue;
			}
//--------------------------------------------------------------------------------------------
			lostTime++;
		} //end of for loop for time
		programeStatistics(time-1,lostTime,waitingTime,FinishedProcesses.size());
	}

//-------------------------------------------------------------------------------------------
//This method prints the four queues,IO Execution, Finished Processes, total time,CPU Utilization and Average waiting time
	public static void programeStatistics(int Totaltime,int lostTime,int waitingTime,int numberOfProcess) {
		int usedTime = Totaltime - lostTime;
		double cpuUtlization = (usedTime / (double)Totaltime) * 100;
		double avgWaitingTime = waitingTime / (double)numberOfProcess;
		
		System.out.println("RR1 : "+CPUExcutionQ1);
		System.out.println();
		System.out.println("RR2 : "+CPUExcutionQ2);
		System.out.println();
		System.out.println("SRTF: "+CPUExcutionQ3);
		System.out.println();
		System.out.println("FCFS: "+CPUExcutionQ4);
		System.out.println();
		System.out.println("IO Excution: "+ExcutingIO);
		System.out.println();
		System.out.println("Finished Processes: "+Finished);
		System.out.println();
		
        System.out.println("Total Time : "+Totaltime+" ms");
		System.out.printf("CPU Utilization: %.1f%% \n",cpuUtlization);
		System.out.printf("Average waiting time: %.1f ms \n",avgWaitingTime);
		System.out.println();
		System.out.println("The program finished...");
	}
//-----------------------------------------------------------------------------------------
// In this method we return the ID of the currently running process for the SRTF queue
	public static Process findPreviousRunningProcess(int PID,ArrayList<Process> SRTF) {
		for (int i = 0; i < SRTF.size(); i++)
			if(SRTF.get(i).getPID() == PID)
				return SRTF.get(i);
		return null;
	}
//--------------------------------------------------------------------------------------
//This method is for the shortest remaining time first queue
	public static void SRTFQueueWork(int time,int state,int preemption,ArrayList<Process> SRTF,
			ArrayList<Process> FCFS,ArrayList<Process> IOExcution,ArrayList<Process>FinishedProcesses,
			Process CPUProcess,boolean processNotFinished) {
		
		if((enteredProcessToQ3 = true || leftProcessFromQ3 == true) && SRTF.size() > 1) {
			CPUProcess = shortestTimeProcess(SRTF);
			if(processNotFinished == true && CPUProcess.getPID()!= PID_unfinishedProcess) {
				Process previousProcess = findPreviousRunningProcess(PID_unfinishedProcess, SRTF);
				previousProcess.incrementPreemption();
			}
			
		}
		
		else if(processNotFinished == true) 
			CPUProcess = findPreviousRunningProcess(PID_unfinishedProcess,SRTF);
		
		printProcessToString(state, CPUProcess.getPID(),time,true);
		
		enteredProcessToQ3 = false;
		leftProcessFromQ3 = false;
		CPUProcess.getCPUBurst()[0]--;
		if(CPUProcess.getCPUBurst()[0] == 0){
			if(CPUProcess.getCPUBurst().length - 1 == 0) { 
					Finished += "| P"+CPUProcess.getPID()+"[time:"+time+"] ";
					FinishedProcesses.add(CPUProcess);
					SRTF.remove(CPUProcess);    //terminate process
					processNotFinished = false;
					leftProcessFromQ3 = true;
					return;
				}
			else {  //if process has IO that is need to be execute 
				CPUProcess.setState(state);
				CPUProcess.setCPUBurst(CPUProcess.getCPUBurst());
				CPUProcess.setArrivedIO(time+1);
				//add the process to array list for IO execution 
				IOExcution.add(CPUProcess);
				SRTF.remove(CPUProcess);
				processNotFinished = false;
				leftProcessFromQ3 = true;
			}
			return;
		}
					
		else if (CPUProcess.getPreemption() == preemption) {
			int newState = state + 1;
			CPUProcess.setState(newState);
			FCFS.add(CPUProcess);
			SRTF.remove(CPUProcess);
			leftProcessFromQ3 = true;
			processNotFinished = false;
			return;
		}
		processNotFinished = true;
		PID_unfinishedProcess = CPUProcess.getPID();
		return;
	}
//-----------------------------------------------------------------------------------	
//This method returns the process with the shortest time left to compare with other processes in the SRTF queue
	public static Process shortestTimeProcess(ArrayList<Process> SRTF) {
		Process shortestTimeProcess = SRTF.get(0);
		
		for (int i = 0; i < SRTF.size(); i++) {
			if(shortestTimeProcess.getEstimatedTime() > SRTF.get(i).getEstimatedTime())
				shortestTimeProcess = SRTF.get(i);
		}
		return shortestTimeProcess;
	}
//------------------------------------------------------------------------------------------
//This method has the final queue work which is FCFS queue 
	public static void FCFSQueueWork(int time,int state,ArrayList<Process> FCFS,ArrayList<Process> 
		IOExcution,ArrayList<Process> FinishedProcesses) {
		Process CPUProcess;
		CPUProcess = FCFS.get(0);
		
		printProcessToString(state, CPUProcess.getPID(),time,true);
		
		CPUProcess.getCPUBurst()[0]--;
		if(CPUProcess.getCPUBurst()[0] == 0){  
			if(CPUProcess.getCPUBurst().length - 1 == 0) { //terminate process
			CPUExcutionQ4 += time + "] ";
			Finished += "| P"+CPUProcess.getPID()+"[time:"+time+"] ";
			FinishedProcesses.add(CPUProcess);
			FCFS.remove(CPUProcess);
		}
		else {  //if process has IO that is need to be execute 
			CPUProcess.setState(state);
			
			CPUProcess.setCPUBurst(CPUProcess.getCPUBurst()); 
			CPUProcess.setArrivedIO(time+1);
			//add the process to array list for IO execution
			IOExcution.add(CPUProcess);
			FCFS.remove(CPUProcess);
		}
		return;
		}

	}
//-------------------------------------------------------------------------------------
//This method is for the Round Robin queue
	public static void RRQueueWork(int time,int state,int timeQuantum,double estimatedTime,ArrayList<Process> Q1,
			ArrayList<Process> Q2,ArrayList<Process> IOExcution,ArrayList<Process> FinishedProcesses) {
		
		Process CPUProcess;
		counter++;
		CPUProcess = Q1.get(0);
		printProcessToString(state, CPUProcess.getPID(),time,true);
		CPUProcess.getCPUBurst()[0]--;
		if(CPUProcess.getCPUBurst()[0] == 0){  
			counter = 0;
			if(CPUProcess.getCPUBurst().length - 1 == 0) { //terminate process
				
				///////////////////////////////////////////////////////////////////////
					Finished += "| P"+CPUProcess.getPID()+"[time:"+time+"] ";
					FinishedProcesses.add(CPUProcess);
					Q1.remove(CPUProcess);
				}
				else { 			 //if process has IO that is need to be execute 
					CPUProcess.setState(state);
					CPUProcess.setTimeQuantaForOneCPUBurst(0);
					CPUProcess.setCPUBurst(CPUProcess.getCPUBurst()); 
					CPUProcess.setArrivedIO(time+1);
					//add the process to array list for IO execution
					IOExcution.add(CPUProcess);
					Q1.remove(CPUProcess);
				}
				return;
			}
					
		else if (counter % timeQuantum == 0 && time > 0) { 
			int test = CPUProcess.getTimeQuantaForOneCPUBurst()+1;
			counter = 0;
			CPUProcess.setTimeQuantaForOneCPUBurst(test);
			if(CPUProcess.getTimeQuantaForOneCPUBurst() == numberOfTimeQuanta) {//go to the next queue
				int newState = state + 1;
				///////////////////////////////////////////////////////////////
				
				//////////////////////////////////////////////////////////////
				CPUProcess.setState(newState);
				CPUProcess.setTimeQuantaForOneCPUBurst(0);
				if(state == 1) {
					enteredProcessToQ3 = true;
					CPUProcess.setEstimatedTime(estimatedTime);
				}
				Q2.add(CPUProcess);
				Q1.remove(CPUProcess);
				return;
			}
			//add the process to the end of the queue
			Q1.remove(CPUProcess);
			Q1.add(CPUProcess);
			return;
		}
		
		return;
	}
//-----------------------------------------------------------------------------------------
//In this method we check the state of the process then return it to it's queue depending on  the state 
	public static void returnProcessToTheQueue(Process returnedProcess,ArrayList<Process> RR1,
			ArrayList<Process> RR2,ArrayList<Process> SRTF,ArrayList<Process> FCFS) {
		if(returnedProcess.getState() == 0) 
			RR1.add(returnedProcess);							
		else if(returnedProcess.getState() == 1) 
			RR2.add(returnedProcess);
		else if(returnedProcess.getState() == 2) 
			SRTF.add(returnedProcess);
		else if(returnedProcess.getState() == 3) 
			FCFS.add(returnedProcess);
	}
//--------------------------------------------------------------------------------------
//This method works only if the user want to generate his work randomly 
	public static void start() throws InvalidInputException{
		
		try {
			System.out.println("Please enter the number of Processes: ");
			numberOfProcesses = input.nextInt();
			if(numberOfProcesses <= 0)
				throw new InvalidInputException("Number less or equal zero is not allowed.");
					
			System.out.println("Please enter the max arrival time: ");
			maxArrivalTime = input.nextInt();
			if(maxArrivalTime <= 0)
				throw new InvalidInputException("Number less or equal zero is not allowed.");
					
			System.out.println("Please enter max number of CPU Burst: ");
			maxCPUBurst = input.nextInt();
			if(maxCPUBurst <= 0)
				throw new InvalidInputException("Number less or equal zero is not allowed.");
					
			System.out.println();
					
			System.out.println("Please enter the range for IO burst duration, ");
			System.out.println("Min: ");
			minIO = input.nextInt();
			if(minIO <= 0)
				throw new InvalidInputException("Number less or equal zero is not allowed.");
			System.out.println("Max: ");
			maxIO = input.nextInt();
			if(maxIO <= 0)
				throw new InvalidInputException("Number less or equal zero is not allowed.");
				
			if(minIO >= maxIO)
				throw new InvalidInputException("Max IO must be larger than min IO.");
			
			System.out.println();
					
			System.out.println("Please enter the range for CPU burst duration, ");
			System.out.println("Min: ");
			minCPU = input.nextInt();
			if(minCPU <= 0)
				throw new InvalidInputException("Number less or equal zero is not allowed.");
			System.out.println("Max: ");
			maxCPU = input.nextInt();
			if(maxCPU <= 0)
				throw new InvalidInputException("Number less or equal zero is not allowed.");
			
			if(minCPU >= maxCPU)
				throw new InvalidInputException("Max CPU must be larger than min CPU.");
			
			
		}catch(InvalidInputException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}catch(InputMismatchException e) {
			System.out.println("Invalid input!");
			System.exit(0);
		}
				
	}
//------------------------------------------------------------------------------------------- 
	public static void defineArray(Process []processes,int maxArrivalTime,int maxCPUBurst,int minIO,
			int maxIO,int minCPU,int maxCPU) {
		for(int i = 0; i < processes.length; i++) {
			processes[i] = new Process(maxArrivalTime,maxCPUBurst, minIO, maxIO, minCPU, maxCPU);
		}
	}
//------------------------------------------------------------------------------------------
	public static void writeToFile(Process []processes) {
		System.out.println();
		String fileName;
		System.out.println("Please enter the name of the file (xxx.txt) to store the data in: ");
		fileName = input.next();
		try {
			  FileWriter myWriter = new FileWriter(fileName);
			  BufferedWriter buffer = new BufferedWriter(myWriter);  
			  for(int i = 0; i < processes.length; i++) {
				  buffer.write(processes[i].getPID()+" "+processes[i].getArrivalTime()+" ");
					int counter = 0;
					while(counter < processes[i].getCPUBurst().length) {
						if (counter == (processes[i].getCPUBurst().length - 1))
							buffer.write(processes[i].getCPUBurst()[counter]+"");
						else
							buffer.write(processes[i].getCPUBurst()[counter]+" "+processes[i].getIOBurst()[counter]+" ");
						counter++;
					}
					buffer.newLine();
				}
			 
		      buffer.close();
		      
		    } catch (IOException e) {
		      System.out.println("An error in the file has occurred.");
		      e.printStackTrace();
		    }
		
	}
}
//-----------------------------------------------------------------------------------------------
// Always use try and catch in  the situations of an error for a well performed program