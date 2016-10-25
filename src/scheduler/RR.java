package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

public class RR extends SchedulingAlgorithm{
	ArrivalComparator comp = new ArrivalComparator();
	int quantum = 2;
	PriorityQueue<Process> tempQueue;
	ArrayList<Process> processes;
	boolean verbose;

	public RR(ArrayList<Process> processes, boolean verbose) {
		super();
		this.processes = processes;
		this.verbose = verbose;
	}
	
	public void run(){
		int cycle = 0;
		if (verbose) printCurrentCycle(processes, cycle);
		
		setup(processes, comp);
		setQuantumTime();
		
		tempQueue = new PriorityQueue(1000,comp);
		int finishedProcesses = 0;
		int cpuUsed = 0;
		int ioUsed = 0;
		
		Process running = ready.poll();
		running.setBurstTime();
		if (verbose) System.out.println("Find burst when choosing ready process to run " + running.randomNumber);

		cycle++;
		while (finishedProcesses < processes.size()){
			if (verbose) printCurrentCycle(processes, cycle);

			if (!blocked.isEmpty()) ioUsed++;
			for (Process process: blocked){
				process.runIOBurst();
			}
			
			for (Process process: ready){
				process.waitingTime++;
			}
			
			if (running != null){
				cpuUsed++;
				running.runCPUBurst();
				running.currentQuantumTime -=1;
				if (running.totalCPUTime == 0){
					finishedProcesses++;
					running.terminateProcess(cycle);
					running = null;
				} else if (running.CPUBurstTime == 0){
					running.currentQuantumTime = quantum;
					running.setIOTime();//may need to add quantum
					blocked.add(running);
					running = null;
				} else if (running.currentQuantumTime == 0){
					running.currentQuantumTime = quantum;
					running.readyProcess();
					tempQueue.add(running);
					running = null;
				}

			}
			
			Iterator<Process> iter = blocked.iterator();

			while (iter.hasNext()) {
			    Process blockedProcess = iter.next();
				if (blockedProcess.IOBurstTime == 0){
					blockedProcess.readyProcess();
					tempQueue.add(blockedProcess);
					iter.remove();
				}
			}
			
			while (!unstarted.isEmpty() && unstarted.peek().arrivalTime == cycle){
				Process current = unstarted.poll();
				current.readyProcess();
				tempQueue.add(current);
			}
			while (!tempQueue.isEmpty()){
				ready.add(tempQueue.poll());
			}
			
			if (running == null){
				running = checkForNextProcess();
			}			
			cycle++;
		}
		
		System.out.println("The scheduling algorithm used is Round Robin");
		printAllProcesses(processes);
		printSummary(processes, cycle-1, cpuUsed, ioUsed);
	}
	
	public void setQuantumTime(){
		for (Process process: processes){
			process.isQuantum = true;
			process.currentQuantumTime = quantum;
		}
	}
	
	public Process checkForNextProcess(){
		Process next = null;

		if (!ready.isEmpty()){
			next = ready.poll();
			next.status = Status.RUNNING;
			if (verbose && next.CPUBurstTime == 0) System.out.println("Find burst when choosing ready process to run " + next.randomNumber);
			if (next.CPUBurstTime == 0) next.setBurstTime();
		}
		return next;	
	}

}
