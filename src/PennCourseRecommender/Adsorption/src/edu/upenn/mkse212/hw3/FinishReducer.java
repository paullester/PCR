package edu.upenn.mkse212.hw3;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FinishReducer extends Reducer<Text, Text, Text, Text> {
	public void reduce(Text username, Iterable<Text> courseRanks, Context context) {
		for (Text t : courseRanks) {
			try {
				context.write(username, t);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
