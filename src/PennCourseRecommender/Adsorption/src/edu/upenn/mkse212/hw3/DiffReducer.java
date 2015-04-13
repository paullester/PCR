package edu.upenn.mkse212.hw3;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DiffReducer extends Reducer<Text, Text, Text, Text> {
	public void reduce(Text node, Iterable<Text> labels, Context context) {
		Double max = 0.0;
		HashMap<String, Double> map = new HashMap<String, Double>();
		for (Text t : labels) {
			String s = t.toString();
			String[] tmp = s.split(":");
			for (String l : tmp) {
				String[] label = l.split(";");
				String username = label[0];
				Double rank = Double.parseDouble(label[1].toString());
				if (map.containsKey(username)) {
					Double diff = Math.abs(rank - map.get(username));
					if (diff > max) {
						max = diff;
					}
				} else {
					map.put(username, rank);
				}
			}
		}
		
		try {
			context.write(new Text(node), new Text(max.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
