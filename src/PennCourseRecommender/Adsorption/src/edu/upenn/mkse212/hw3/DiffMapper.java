package edu.upenn.mkse212.hw3;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DiffMapper extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable line, Text nodePointsLabels, Context context) {
		String s = nodePointsLabels.toString();
		String[] nPL = s.split("\t");
		String node = nPL[0];
		String[] pL = nPL[1].split(" ");
		String labels = pL[1];
		try {
			context.write(new Text(node), new Text(labels));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
