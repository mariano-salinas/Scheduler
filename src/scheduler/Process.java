package scheduler;

public class Process implements Comparable<Process>{
	int arrivalTime;
	int totalCPUTime;
	int B;
	int M;
	int CPUBurstTime;
	int IOBurstTime;
	int index;
	Enum status;
	int finishingTime = 0;
	int turnAroundTime = 0;
	int totalIOTime;
	int waitingTime;
	int randomNumber;
	
	public Process(int arrivalTime, int B, int totalCPUTime, int M, int index){
		this.arrivalTime = arrivalTime;
		this.B = B;
		this.totalCPUTime = totalCPUTime;
		this.M = M;
		this.index = index;
		this.status = Status.UNSTARTED;
	}
	
	public void runCPUBurst(){
		totalCPUTime--;
		CPUBurstTime--;
	}
	
	public void runIOBurst(){
		IOBurstTime--;
		totalIOTime++;
	}

	public void setBurstTime() {
		this.status = Status.RUNNING; 
		this.CPUBurstTime = RandomNumberGenerator.getRandomNumber(this.B);
		this.randomNumber = RandomNumberGenerator.rand;
	}
	
	public void setIOTime() {
		this.status = Status.BLOCKED;
		this.IOBurstTime = RandomNumberGenerator.getRandomNumber(this.B);
		this.randomNumber = RandomNumberGenerator.rand;
	}
	
	public void terminateProcess(){
		this.status = Status.TERMINATED;
	}
	
	public void readyProcess(){
		this.status = Status.READY;
	}
	
	public int compareTo(Process p) {
		if(this.arrivalTime != p.arrivalTime){
			return this.arrivalTime > p.arrivalTime? 1:-1;
		}
		return new Integer(this.index).compareTo(p.index);
	}
	
	public String toString(){
		StringBuilder process = new StringBuilder();
		process
			.append("Process " + this.index + ":" +"\n")
			.append("(A,B,C,M) = (" + this.arrivalTime + "," + this.B + "," + this.totalCPUTime + "," + this.M + "\n")
			.append("Finishing time: " + this.finishingTime)
			.append("Turnaround time " + this.turnAroundTime)
			.append("I/O time: " + this.totalIOTime)
			.append("Waiting time: " + this.waitingTime);
		
		return process.toString();
		
	}
}
