package scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

public class SJF extends SchedulingAlgorithm{
	ArrivalComparator arrivalComp = new ArrivalComparator();
	TimeRemainingComparator priorityComp = new TimeRemainingComparator();
	ArrayList<Process> processes;
	boolean verbose;
	
	public SJF(ArrayList<Process> processes, boolean verbose) {
		super();
		this.processes = processes;
		this.verbose = verbose;
	}
	
	public void run(){
		setup(processes, arrivalComp);
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
		
		System.out.println("The scheduling algorithm used is Shortest Job First");
		printAllProcesses(processes);
		printSummary(processes, cycle-1, cpuUsed, ioUsed);
	}
	
	public void initializeLists(ArrayList<Process> processes){
		blocked =  new ArrayList<Process>();
		ready = new PriorityQueue<Process>(1000, priorityComp);
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
