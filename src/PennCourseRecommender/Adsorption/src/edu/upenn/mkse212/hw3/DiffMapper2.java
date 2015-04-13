package edu.upenn.mkse212.hw3;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DiffMapper2 extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable line, Text nodeDiff, Context context) {
		String s = nodeDiff.toString();
		String[] nD = s.split("\t");
		String diff = nD[1];
		try {
			context.write(new Text("foo"), new Text(diff));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
