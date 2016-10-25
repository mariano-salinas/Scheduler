package scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SchedulingAlgorithm {
	ArrayList<Process> blocked;
	Queue<Process> ready;
	Queue<Process> unstarted;
	boolean verbose;
	
	public SchedulingAlgorithm(){
	}
	
	public void setup(ArrayList<Process> processes, Comparator<Object> comp){
		initializeLists(processes);
		printInputs(processes, comp);
		RandomNumberGenerator.reset();
	}
	
	public void initializeLists(ArrayList<Process> processes){
		blocked =  new ArrayList<Process>();
		ready = new ConcurrentLinkedQueue<Process>();
		unstarted =  new ArrayDeque<Process>();
		
		for (int i = 0; i < processes.size(); i++){
			Process currentProcess = processes.get(i);
			if (currentProcess.arrivalTime == 0){
					ready.add(currentProcess);
					currentProcess.status = Status.READY;
			}
			else {
				unstarted.add(currentProcess);
			}
		}
	}
	
	public void printCurrentCycle(ArrayList<Process> processes, int cycle){
		System.out.print(String.format("Before cycle %-2d %-3s", cycle, ":"));
		for (Process process: processes){
			if (process.status == Status.RUNNING){
				System.out.print(String.format("%-7s %-7d", process.status, process.CPUBurstTime));
			} else if (process.status == Status.BLOCKED){
				System.out.print(String.format("%-7s %-7d", process.status, process.IOBurstTime));
			} else{
				System.out.print(String.format("%-14s ", process.status));
			}
		}
		System.out.println("");
	}
	
	public void printAllProcesses(ArrayList<Process> processes){
		for (int i = 0; i < processes.size(); i++){
			System.out.println("\nProcess " + i);
			System.out.println(processes.get(i));
		}
	}
	
	public void printInputs(ArrayList<Process> processes, Comparator<Object> comp){
		int size = processes.size();
		System.out.print("The original input was: " + size + " ");
		for (Process process: processes){
			System.out.print(process.getParameters() + " ");
		}
		Collections.sort(processes, comp);
		System.out.print("\nThe (sorted) input is: " + size + " ");
		for (Process process : processes){
			System.out.print(process.getParameters() + " ");
		}
		System.out.print("\n");
	}
	
	public void printSummary(ArrayList<Process> processes, int finishingTime, int cpuUsed, int ioUsed){
		System.out.println("Summary Data:");
		System.out.println("Finishing time: " + finishingTime);
		System.out.printf("CPU utilization %.6f\n", (double)cpuUsed/finishingTime);
		System.out.printf("IO utilization %.6f\n", (double)ioUsed/finishingTime);
		System.out.printf("Throughput: %.6f per hundred cycles\n", (double)(processes.size() * 100)/(finishingTime));
		
		int totalTurnAround = 0;
		int totalWaiting = 0;
		for (Process process: processes){
			totalTurnAround += process.turnAroundTime;
			totalWaiting += process.waitingTime;
		}
		System.out.printf("Average turnaround time: %.6f\n", (double)totalTurnAround/processes.size());
		System.out.printf("Average waiting time: %.6f\n\n", (double)totalWaiting/(processes.size()));
	}
}
