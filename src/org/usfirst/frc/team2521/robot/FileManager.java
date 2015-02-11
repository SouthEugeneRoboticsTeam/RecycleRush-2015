package org.usfirst.frc.team2521.robot;

import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import edu.wpi.first.wpilibj.Timer;



public class FileManager {
	BufferedWriter writer = null;
	public static String getFormattedDate() {
		SimpleDateFormat dateAsString = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
		Date now = new Date();
		String stringDate = dateAsString.format(now);
		return stringDate;
	}
	
	public void createLog(String pathPrefix, String content) {
		if (writer == null) {
			String path = (pathPrefix + RobotMap.DATE + ".csv");
			File file = new File(path);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				
				}
			}
	    	try {
				writer = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				
			} 
		}
		try {
			if (writer != null) {
			writer.write((content));
			writer.flush();
			}
		} catch (IOException ex) {}
	} //*/
	
	public double[] txtFileToArray(String filename, int fileLength) {
		List<String> recordList;
		int listSize = 1;
		double[] recordArray = null;
		try {
			recordList = Files.readAllLines(Paths.get(filename), Charset.defaultCharset()); // this should work only if it is actually creating new lines
			for (int iii = 0; iii < fileLength; iii++) {
				recordArray[iii] = Double.valueOf(recordList.get(iii));
			}
		} catch (IOException e) {}	
		return recordArray;
	}
	
	public double[] csvFileToArray(String filename) {
		
		FileReader fr = null;
		String[] sArray = null;
		try {
			fr = new FileReader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		try {
			sArray = br.readLine().split(", ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		int fileLength = sArray.length;
		double[] dArray = new double[fileLength];
		for (int i = 0; i < fileLength; i++){
			dArray[i] = Double.parseDouble(sArray[i]);
		}
		return dArray;
	}
	
}
