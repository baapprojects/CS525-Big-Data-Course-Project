package org;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;

public class KmeansCluster extends Configured implements Tool
{
	public static class ClusterMapper extends Mapper<LongWritable, Text, Text, Text>  //output<centroid,point>
	{
		@Override
		public void setup(Context context)
		{
			
		}

		@Override
		//output<centroid,point>
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
		{
			
			String[] str = value.toString().split(",");

			
			// output the nearest centroid as key, and the piont as value
			context.write(new Text(), new Text());
		}
		
		@Override
		public void cleanup(Context context) throws IOException,InterruptedException 
		{
			// do nothing here
		}
	}
	
	// do aggregation in local side
	public static class Combiner extends Reducer<Text, Text, Text, Text> 
	{	  
		@Override
		//update every centroid except the last one
		public void reduce(Text key,Iterable<Text> values,Context context) throws IOException,InterruptedException
		{
			
			while(values.iterator().hasNext())
			{
				
			}
			context.write(key, new Text());
		}
	}
 
	public static class UpdateCenterReducer extends Reducer<Text, Text, Text, Text> 
	{
		@Override
		public void setup(Context context)
		{
			// do nothing here
		}
 
		@Override
		public void reduce(Text key,Iterable<Text> values,Context context) throws IOException,InterruptedException
		{
		
			while(values.iterator().hasNext())
			{
				
			}

			
			context.write(new Text(),new Text());

		}
		
		@Override
		public void cleanup(Context context) throws IOException,InterruptedException 
		{
			// do nothing here
		}
	}
	@Override
	public int run(String[] args) throws Exception 
	{
		return 0;
	}
	
	
	public static void main(String[] args) throws Exception 
	{
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		
		Job job = new Job(conf);
		job.setJarByClass(KmeansCluster.class);
		
		FileInputFormat.setInputPaths(job, "/hzhou/input/kmeans");
		Path outDir = new Path("/hzhou/output/final");
		fs.delete(outDir,true);
		FileOutputFormat.setOutputPath(job, outDir);
		 
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapperClass(ClusterMapper.class);
		 
		job.waitForCompletion(true);
		
	}
}