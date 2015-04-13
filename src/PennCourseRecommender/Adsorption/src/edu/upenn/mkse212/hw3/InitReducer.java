package edu.upenn.mkse212.hw3;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InitReducer extends Reducer<Text, Text, Text, Text> {
	public void reduce(Text node, Iterable<Text> relations, Context context) {
		String s = node.toString();
		String[] test = s.split(";");
		if (test[0].equals("class")) {
			String usernames = "";
			for (Text t : relations) {
				usernames += t.toString() + ":";
			}
			usernames += " none";
			try {
				context.write(new Text(test[1]), new Text(usernames));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			String out = "";
			for (Text t : relations) {
				out += t.toString();
			}
			try {
				context.write(node, new Text(out));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
