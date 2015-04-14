package edu.upenn.mkse212.hw3;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DiffReducer2 extends Reducer<Text, Text, Text, Text> {
	public void reduce(Text foo, Iterable<Text> diff, Context context) {
		Double max = 0.0;
		for (Text t : diff) {
			Double d = Double.parseDouble(t.toString());
			if (d > max) {
				max = d;
			}
		}
		try {
			context.write(new Text(max.toString()), null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
