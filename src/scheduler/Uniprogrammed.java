package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Uniprogrammed  extends SchedulingAlgorithm{

	public Uniprogrammed(ArrayList<Process> processes, boolean verbose) {
		super(processes, verbose);
	}
	
	public void run(){
		setup();
		int cycle = 0;
		int finishedProcesses = 0;
		int cpuUsed = 0;
		int ioUsed = 0;
		Process blocked = null;
		
		if (verbose) printCurrentCycle(cycle);
		Process running = ready.poll();
		running.setBurstTime();
		if (verbose) System.out.println("Find burst when choosing ready process to run " + running.randomNumber);
		cycle++;
		
		
		while (finishedProcesses < processes.size()){
			if (verbose) printCurrentCycle(cycle);
			
			for (Process process: ready){
				process.waitingTime++;
			}
			
			if (running.status == Status.RUNNING){
				cpuUsed++;
				running.runCPUBurst();
				if (running.totalCPUTime == 0){
					finishedProcesses++;
					running.terminateProcess(cycle);
					running = checkForNextProcess();
				} else if (running.CPUBurstTime == 0){
					running.setIOTime();				
				}
			} else {
				ioUsed++;
				running.runIOBurst();
				if (running.IOBurstTime == 0){
					running.setBurstTime();
					if (verbose) System.out.println("Find burst when choosing ready process to run " + running.randomNumber);
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
		
		System.out.println("The scheduling algorithm used is Uniprogrammed");
		printAllProcesses();
		printSummary(cycle-1, cpuUsed, ioUsed);
	}
	
	public Process checkForNextProcess(){
		Process next = null;
		
		if (!ready.isEmpty()){
			next = ready.poll();
			next.setBurstTime();
		}
		return next;	
	}

}
