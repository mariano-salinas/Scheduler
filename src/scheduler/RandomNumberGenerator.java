package scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RandomNumberGenerator {
	private static ArrayList<Integer> randomNums = new ArrayList<Integer>();
	private static int count = 0;
	static int rand;
	
	static{
		File file = new File("random-numbers.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine())!=null){
				randomNums.add(Integer.parseInt(line.trim()));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void reset(){
		count = 0;
	}
	
	public static int getRandomNumber(int max){
		rand = randomNums.get(count++);
		return 1+(rand%max);
	}
}
