package scheduler;

public class Process implements Comparable<Process>{
	int arrivalTime;
	int totalCPUTime;
	int totalCPU;
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
	int prevCPUBurstTime;
	
	public Process(int arrivalTime, int B, int totalCPUTime, int M, int index){
		this.arrivalTime = arrivalTime;
		this.B = B;
		this.totalCPU = totalCPUTime;
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
		this.prevCPUBurstTime = this.CPUBurstTime;
		this.randomNumber = RandomNumberGenerator.rand;
	}
	
	public void setIOTime() {
		this.status = Status.BLOCKED;
		this.IOBurstTime = this.prevCPUBurstTime*this.M;
	}
	
	public void terminateProcess(){
		this.status = Status.TERMINATED;
		this.turnAroundTime = this.finishingTime - this.arrivalTime;
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
	
	public String getParameters(){
		return "(" + this.arrivalTime + "," + this.B + "," + this.totalCPU + "," + this.M + ")";
	}
	
	public String toString(){
		StringBuilder process = new StringBuilder();
		process
			.append("(A,B,C,M) = " + this.getParameters() + "\n")
			.append("Finishing time: " + this.finishingTime  + "\n")
			.append("Turnaround time " + (this.finishingTime - this.arrivalTime) + "\n")
			.append("I/O time: " + this.totalIOTime + "\n")
			.append("Waiting time: " + this.waitingTime + "\n");
		
		return process.toString();
		
	}
}
