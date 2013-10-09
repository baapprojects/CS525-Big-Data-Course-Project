import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
Task C:
Find top 10 interesting facebook pages, those that got the most
accesses based on accesslog dataset.

Usage: programname directory1 directory2
directory1: directory where the file accesslog.txt is.
directory2: output directory

Output:
top 10 pages'
PageID, Access count

*/

public class taskC
{

	public static class Map extends Mapper<LongWritable, Text, IntWritable, IntWritable>
	{
		private final static IntWritable one = new IntWritable(1);
		private IntWritable whatpage = new IntWritable(0);
		
		@Override
		public void map(LongWritable key, Text value, Context context)
		throws IOException, InterruptedException
		{
			String line = value.toString();
			String[] splits = line.split(",");
			whatpage.set(Integer.parseInt(splits[2]));
			context.write(whatpage, one);;
		}
	}


	public static class Reduce extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>
	{		
		int topK = 10;
		int[] tops = new int[topK + 1]; // variable to store top 10 PageID
		int[] topHits = new int[topK + 1]; // variable to store top 10 access counts

		@Override
		protected void setup(Context context)
    throws IOException, InterruptedException {
		  for (int i = 0; i < topK; i++) topHits[i] = 0;
		}
		
		/*
		reduce function is calculating the total hits of a page and inserting the pageid and count
		in the array of top 10 pages.
		It does not output anything to the output file. Instead I used the cleanup method to output
		the top 10 pageID and counts
		*/
		@Override
		public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) 
		throws IOException, InterruptedException
		{
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			
			// next 7 lines are for sorting the top 10 elements
      int k; 
			for (k = topK - 1; k >= 0; k--)
      if (sum > topHits[k])
				{ tops[k + 1] = tops[k]; topHits[k + 1] = topHits[k]; }
      else 
				break;
      if (k < topK - 1){ tops[k + 1] = key.get(); topHits[k + 1] = sum; }
			
		}

		// cleanup method outputs the top 10 pageIDs and their counts
		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException 
		{
			for (int i = 0; i < topK; i++) 
				context.write(new IntWritable(tops[i]), new IntWritable(topHits[i]));
		}
	}

	public static void main(String[] args) throws Exception 
	{
		Job job = new Job();
    job.setJarByClass(taskC.class);
    job.setJobName("taskC");
    FileInputFormat.addInputPath(job, new Path(args[0] + "/accesslog.txt"));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    job.setNumReduceTasks(1);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);
    
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}