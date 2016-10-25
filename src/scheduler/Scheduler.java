package scheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Scheduler {
	ArrayList<Process> processes;

	public static void main(String[] args) {
		boolean verbose = false;
		String fileName = args[0];
		if (args.length == 2){
			if (args[1].equals("--verbose")){
				verbose = true;
			} else {
				System.out.println("Unrecognized second argument");
				System.exit(1);
			}
		}
		if (args.length > 2){
			System.out.println("Too many arguments");
			System.exit(1);
		}
		Scanner input = createScanner(fileName);
		ArrayList<Process> processes = readFileProcesses(input);
		
		
		System.out.println("arguments " + Arrays.toString(args));
		
		FCFS fcfs = new FCFS(clone(processes), verbose);
		fcfs.run();
		Uniprogrammed uniprogrammed = new Uniprogrammed(clone(processes), verbose);
		uniprogrammed.run();
		SJF sjf = new SJF(clone(processes), verbose);
		sjf.run();
		RR rr = new RR(clone(processes), verbose);
		rr.run();
		

	}
	
	/**
	 * Creates an input reader from the file name given in the command line argument
	 * @param fileName
	 * @return Scanner to read the file
	 */
	public static Scanner createScanner(String fileName){
		BufferedReader br;
		Scanner input = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			input = new Scanner(br);
		} catch (FileNotFoundException e) {
			System.out.println("No such file or directory");
			System.exit(1);
		}
		
		return input;
	}
	
	public static ArrayList<Process> readFileProcesses(Scanner input){
		ArrayList<Process> processes = new ArrayList<Process>();
		int numModules = input.nextInt();
		String line = input.nextLine();
		String[] list = line.split("\\),\\(|\\)|\\(");
		int id = 0;
		for (String element: list){
			String[] splitElements = element.split(" ");
			if (allValuesAreNumbers(splitElements) && splitElements.length == 4){
				int arg1 = Integer.parseInt(splitElements[0]);
				int arg2 = Integer.parseInt(splitElements[1]);
				int arg3 = Integer.parseInt(splitElements[2]);
				int arg4 = Integer.parseInt(splitElements[3]);
				processes.add(new Process(arg1, arg2, arg3, arg4, id));
				id++;
			}
		}
		
		return processes;
	}
	
	public static boolean allValuesAreNumbers(String[] arr){
		for (String element: arr){
			try{
				Integer.parseInt(element);
			} catch(NumberFormatException e){
				return false;
			}
		}
		
		return true;
	}
	
	public static ArrayList<Process> clone(ArrayList<Process> processes){
		ArrayList<Process> processesClone = new ArrayList<Process>();
		for (Process process: processes){
			processesClone.add(process.clone());
		}
		return processesClone;
		
	}

}
