package edu.upenn.mkse212.hw3;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class IterMapper extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable line, Text nodePointsLabels, Context context) {
		String s = nodePointsLabels.toString();
		String[] nPL = s.split("\t");
		String node = nPL[0];
		String[] pL = nPL[1].split(" ");
		String p = pL[0];
		String l = pL[1];
		
		String[] points = p.split(":");
		
		if (!l.equals("none")) {
			String[] labels = l.split(":");
			Double count = (double) points.length;
			for (String label : labels) {
				String[] tmp = label.split(";");
				String to = tmp[0];
				Double rank = Double.parseDouble(tmp[1].toString());
				Double retRank = rank/count;
				for (String point : points) {
					try {
						context.write(new Text(point), new Text(to + ";" + retRank.toString()));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		}
		
		try {
			context.write(new Text(node), new Text("list:" + points.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
