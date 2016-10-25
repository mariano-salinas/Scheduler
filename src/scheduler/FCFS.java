package scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FCFS extends SchedulingAlgorithm{
	ArrivalComparator comp = new ArrivalComparator();
	ArrayList<Process> processes;
	boolean verbose;
	
	public FCFS(ArrayList<Process> processes, boolean verbose){
		super();
		this.processes = processes;
		this.verbose = verbose;
	}
	
	public void run(){
		setup(processes, comp);
		int cycle = 0;
		int finishedProcesses = 0;
		int cpuUsed = 0;
		int ioUsed = 0;
		
		if (verbose) printCurrentCycle(processes, cycle);
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
				if (running.totalCPUTime == 0){
					finishedProcesses++;
					running.terminateProcess(cycle);
					running = null;
				} else if (running.CPUBurstTime == 0){
					running.setIOTime();
					blocked.add(running);
					running = null;
				}
			}
			
			Collections.sort(blocked, comp);
			Iterator<Process> iter = blocked.iterator();

			while (iter.hasNext()) {
			    Process blockedProcess = iter.next();
				if (blockedProcess.IOBurstTime == 0){
					blockedProcess.readyProcess();
					ready.add(blockedProcess);
					iter.remove();
				}
			}
			
			while (!unstarted.isEmpty() && unstarted.peek().arrivalTime == cycle){
				Process current = unstarted.poll();
				current.readyProcess();
				ready.add(current);
			}
			
			if (running == null){
				running = checkForNextProcess();
			}			
			cycle++;
		}
		
		System.out.println("The scheduling algorithm used is First Come First Serve");
		printAllProcesses(processes);
		printSummary(processes, cycle-1, cpuUsed, ioUsed);
	}
	
	public Process checkForNextProcess(){
		Process next = null;

		if (!ready.isEmpty()){
			next = ready.poll();
			next.setBurstTime();
			if (verbose) System.out.println("Find burst when choosing ready process to run " + next.randomNumber);
		}
		return next;	
	}
}
