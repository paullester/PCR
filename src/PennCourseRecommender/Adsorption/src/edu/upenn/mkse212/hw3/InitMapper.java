package edu.upenn.mkse212.hw3;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class InitMapper extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable lineNumber, Text line, Context context) {
		String s = line.toString();
		String[] arr = s.split("\t ");
		
		Text username = new Text(arr[0]);
		
		String[] c = arr[1].split(" ");
		
		// Space is is delimiter for class to label
		// : is delimiter for label to label
		// : is delimiter between username and weight
		String label = " " + username.toString() + ";1.0";
		
		String classes = "";
		
		for (int i = 0; i < c.length; i++) {
			classes = classes + c[i] + ":";
			Text thisClass = new Text("class;" + c[i]);
			try {
				context.write(thisClass, username);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		classes = classes + label;
		
		Text classAndLabel = new Text(classes);

		try {
			context.write(username, classAndLabel);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
