package edu.upenn.mkse212.hw3;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.util.Map;
import java.util.HashMap;

public class IterReducer extends Reducer<Text, Text, Text, Text> {
	public void reduce(Text node, Iterable<Text> valueList, Context context) {
		Map<String, Double> countRank = new HashMap<String, Double>();
		String list = "";
		Double totalRank = 0.0;
		for (Text t : valueList) {
			String s = t.toString();
			String[] arr = s.split(":");
			if (arr[0].equals("list")) {
				list = arr[1].toString();
			} else {
				String[] arr2 = s.split(";");
				if (countRank.containsKey(arr2[0])) {
					Double rank = countRank.get(arr2[0]);
					totalRank += Double.parseDouble(arr2[1].toString());
					rank += Double.parseDouble(arr2[1].toString());
					countRank.put(arr2[0], rank);
				} else {
					Double rank = Double.parseDouble(arr2[1].toString());
					totalRank += rank;
					countRank.put(arr2[0], rank);
				}
			}
		}
		String labels = "";
		for (String label : countRank.keySet()) {
			Double normRank = countRank.get(label) / totalRank;
			labels += label + ";" + normRank + ":";
		}
		
		try {
			context.write(node, new Text(list + " " + labels));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
