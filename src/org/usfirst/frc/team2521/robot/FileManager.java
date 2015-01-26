package org.usfirst.frc.team2521.robot;

import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	public static String getFormattedDate() {
		SimpleDateFormat dateAsString = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
		Date now = new Date();
		String stringDate = dateAsString.format(now);
		return stringDate;
	}
	
	public void createLog(String pathPrefix, String content) {
		BufferedWriter writer = null; 
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
	
	public double[] fileToArray(String filename, int fileLength) {
		List<String> recordList;
		int listSize = 1;
		double[] recordArray = null;
		try {
			recordList = Files.readAllLines(Paths.get(filename), Charset.defaultCharset()); // this should work only if it is actually creating new lines
			for (int iii = 0; iii <= fileLength; iii++) {
				recordArray[iii] = Double.valueOf(recordList.get(iii));
			}
		} catch (IOException e) {}	
		return recordArray;
	}
	
}
