package scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;

public class FCFS extends SchedulingAlgorithm{
	ArrayList<Process> processes;
	ArrayList<Process> unsortedProcesses;
	Queue<Process> blocked;
	Queue<Process> ready;
	Queue<Process> unstarted;
	boolean verbose;
	
	public FCFS(ArrayList<Process> processes, boolean verbose){
		this.processes = processes;
		this.verbose = verbose;
	}
	
	public void setup(){
		ComparatorByArrivalTime comp = new ComparatorByArrivalTime();
		unsortedProcesses = (ArrayList<Process>)processes.clone();
		Collections.sort(processes, comp);
		initializeLists();
	}
	
	
	public void runAlgorithm(){
		int cycle = 0;
		printCurrentCycle(cycle);
		setup();
		
		int finishedProcesses = 0;
		int finishingTime = 0;
		int turnAroundTime = 0;
		int waitingTime = 0;
		Process running = ready.poll();
		running.setBurstTime();
		cycle++;
		
		while (finishedProcesses < processes.size()){
			if (running != null)System.out.println("Find burst when choosing ready process to run " + running.randomNumber);
			printCurrentCycle(cycle);
			if (running != null){
				running.runCPUBurst();
				if (running.totalCPUTime == 0){
					finishedProcesses++;
					running.terminateProcess();
					running.finishingTime = cycle;
					running = checkForNextProcess();
				} else if (running.CPUBurstTime == 0){
					running.setIOTime();
					blocked.add(running);
					running = checkForNextProcess();
				}
			}
			
			while (!blocked.isEmpty() && blocked.peek().IOBurstTime == 0){
				Process current = blocked.poll();
				current.readyProcess();
				ready.add(current);
			}
			
			while (!unstarted.isEmpty() && unstarted.peek().arrivalTime == cycle){
				Process current = unstarted.poll();
				current.readyProcess();
				ready.add(current);
			}
			
			for (Process process: blocked){
				process.runIOBurst();
			}
			
			if (running == null){
				running = checkForNextProcess();
			}			
			cycle++;
		}
		
		printAllProcesses();
	}
	
	public Process checkForNextProcess(){
		Process running = ready.poll();//must check if null
		if (running != null) 
			running.setBurstTime();
		
		return running;
		
	}
	
	public void printCurrentCycle(int cycle){
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
	
	public void printAllProcesses(){
		for (Process process: unsortedProcesses){
			System.out.println("Process " + process.index);
			System.out.println("Finishing time: " + process.finishingTime);
			System.out.println("Total IO time: " + process.totalIOTime);
			System.out.println("Waiting time: " + process.waitingTime);
		}
	}
	
	public void initializeLists(){
		blocked =  new ArrayDeque<Process>();
		ready = new ArrayDeque<Process>();
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
}
