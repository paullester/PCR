package edu.upenn.mkse212.hw3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class SocialRankDriver 
{
  public static void main(String[] args) throws Exception  {    
	Job job = new Job();
	job.setJarByClass(SocialRankDriver.class);
    if(args[0].equals("init")) {
    	deleteDirectory(args[2]);
    	forInit(args[1],args[2], args[3], job);
    } else if(args[0].equals("iter")){
    	deleteDirectory(args[2]);
    	forIter(args[1],args[2],args[3],job);
    } else if(args[0].equals("diff")){
    	deleteDirectory(args[3]);
    	forDiff(args[1],args[2],args[3],job);
    } else if(args[0].equals("finish")) {
    	deleteDirectory(args[2]);
    	forFinish(args[1],args[2],job);
    } else if (args[0].equals("diff2")) { 
    	deleteDirectory(args[2]);
    	forDiff2(args[1],args[2],job);
    } else if(args[0].equals("composite")) {
    	forComposite(args[1],args[2],args[3],args[4],args[5], args[6], job);
    } else {
    	System.err.println("Impropper usage");
    	System.exit(-1);
    }
  }
  
  private static void forInit(String in, String out, String red, Job job) throws IOException {
	  System.out.println("Susan Greenberg (susangr)");
	  
	  job.setJarByClass(SocialRankDriver.class);
	  job.setNumReduceTasks(Integer.parseInt(red));
	  
	  FileInputFormat.addInputPath(job, new Path(in));
	  FileOutputFormat.setOutputPath(job, new Path(out));
	  
	  job.setMapperClass(InitMapper.class);
	  job.setReducerClass(InitReducer.class);
	  
	  job.setMapOutputKeyClass(Text.class);
	  job.setMapOutputValueClass(Text.class);
	  
	  job.setOutputKeyClass(Text.class);
	  job.setOutputValueClass(Text.class);
  }
  
  private static void forIter(String in, String out, String red, Job job) 
		  throws IOException {
	  System.out.println("Susan Greenberg (susangr)");
	  
	  job.setJarByClass(SocialRankDriver.class);
	  job.setNumReduceTasks(Integer.parseInt(red));
	  
	  FileInputFormat.addInputPath(job, new Path(in));
	  FileOutputFormat.setOutputPath(job, new Path(out));
	  
	  job.setMapperClass(IterMapper.class);
	  job.setReducerClass(IterReducer.class);
	  
	  job.setMapOutputKeyClass(Text.class);
	  job.setMapOutputValueClass(Text.class);
	  
	  job.setOutputKeyClass(Text.class);
	  job.setOutputValueClass(Text.class);
  }
  
  private static void forDiff(String in1, String in2, String out, Job job) 
		  throws Exception {
	  System.out.println("Susan Greenberg (susangr)");
	  
	  job.setJarByClass(SocialRankDriver.class);
	  job.setNumReduceTasks(1);
	  
	  deleteDirectory(out);
	  
	  FileInputFormat.addInputPath(job, new Path(in1));
	  FileInputFormat.addInputPath(job, new Path(in2));
	  FileOutputFormat.setOutputPath(job, new Path(out));
	  
	  job.setMapperClass(DiffMapper.class);
	  job.setReducerClass(DiffReducer.class);
	  
	  job.setMapOutputKeyClass(Text.class);
	  job.setMapOutputValueClass(Text.class);
	  
	  job.setOutputKeyClass(Text.class);
	  job.setOutputValueClass(Text.class);
  }
  
  private static void forDiff2(String in, String out, Job job) throws Exception {
	  job.setJarByClass(SocialRankDriver.class);
	  job.setNumReduceTasks(1);
	  
	  deleteDirectory(out);
	  
	  FileInputFormat.addInputPath(job, new Path(in));
	  FileOutputFormat.setOutputPath(job, new Path(out));
	  
	  job.setMapperClass(DiffMapper2.class);
	  job.setReducerClass(DiffReducer2.class);
	  
	  job.setMapOutputKeyClass(Text.class);
	  job.setMapOutputValueClass(Text.class);
	  
	  job.setOutputKeyClass(Text.class);
	  job.setOutputValueClass(Text.class);
  }
  
  private static void forFinish(String in, String out, Job job) 
		  throws Exception {
	  System.out.println("Susan Greenberg (susangr)");
	  
	  job.setJarByClass(SocialRankDriver.class);
	  job.setNumReduceTasks(1);
	  
	  deleteDirectory(out);
	  
	  FileInputFormat.addInputPath(job, new Path(in));
	  FileOutputFormat.setOutputPath(job, new Path(out));
	  
	  job.setMapperClass(FinishMapper.class);
	  job.setReducerClass(FinishReducer.class);
	  
	  job.setMapOutputKeyClass(Text.class);
	  job.setMapOutputValueClass(Text.class);
	  
	  job.setOutputKeyClass(Text.class);
	  job.setOutputValueClass(Text.class);
  }
  
  private static void forComposite(String in, String out, String inter1, 
		  String inter2, String diff, String red, Job job) throws Exception {
	  System.out.println("Susan Greenberg (susangr)");
	  
	  deleteDirectory(out);
	  deleteDirectory(inter1);
	  deleteDirectory(inter2);
	  deleteDirectory(diff);
	  
	  Job init = new Job();
	  forInit(in,out,red,init);
	  init.waitForCompletion(true);
	  
	  Job iter1 = new Job();
	  forIter(out,inter1,red,iter1);
	  iter1.waitForCompletion(true);
	  
	  Job iter2 = new Job();
	  forIter(inter1,inter2,red,iter2);
	  iter2.waitForCompletion(true);
	  
	  Job diffJ = new Job();
	  forDiff(inter1,inter2,diff,diffJ);
	  diffJ.waitForCompletion(true);
	  
	  Job diffJ2 = new Job();
	  forDiff2(diff,inter1,diffJ2);
	  diffJ2.waitForCompletion(true);
	  
	  int count = 2;
	  
	  while(readDiffResult(inter1) > 30) {
		  Job iter3 = new Job();
		  forIter(inter2,inter1,red,iter3);
		  iter3.waitForCompletion(true);
		  count++;
		  
		  Job iter4 = new Job();
		  forIter(inter1,inter2,red,iter4);
		  iter4.waitForCompletion(true);
		  count++;
		  
		  Job iter5 = new Job();
		  forIter(inter2,inter1,red,iter5);
		  iter5.waitForCompletion(true);
		  count++;
		  
		  Job iter6 = new Job();
		  forIter(inter1,inter2,red,iter6);
		  iter6.waitForCompletion(true);
		  count++;
		  
		  Job diff1 = new Job();
		  forDiff(inter1,inter2,diff,diff1);
		  diff1.waitForCompletion(true);
		  
		  Job diff2 = new Job();
		  forDiff2(diff,inter1,job);
		  diff2.waitForCompletion(true);
	  }
	  
	  Job finish = new Job();
	  forFinish(inter2, out, finish);
	  finish.waitForCompletion(true);
	  
	  System.out.println("Converged after: " + count + " iterations");
  }

  // Given an output folder, returns the first double from the first part-r-00000 file
  static double readDiffResult(String path) throws Exception 
  {
    double diffnum = 0.0;
    Path diffpath = new Path(path);
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(URI.create(path),conf);
    
    if (fs.exists(diffpath)) {
      FileStatus[] ls = fs.listStatus(diffpath);
      for (FileStatus file : ls) {
	if (file.getPath().getName().startsWith("part-r-00000")) {
	  FSDataInputStream diffin = fs.open(file.getPath());
	  BufferedReader d = new BufferedReader(new InputStreamReader(diffin));
	  String diffcontent = d.readLine();
	  diffnum = Double.parseDouble(diffcontent);
	  d.close();
	}
      }
    }
    
    fs.close();
    return diffnum;
  }
  


  static void deleteDirectory(String path) throws Exception {
    Path todelete = new Path(path);
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(URI.create(path),conf);
    
    if (fs.exists(todelete)) 
      fs.delete(todelete, true);
      
    fs.close();
  }

}
