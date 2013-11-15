package org;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;

public class KmeansCluster extends Configured implements Tool
{
	public static class ClusterMapper extends Mapper<LongWritable, Text, Text, Text>  //output<centroid,point>
	{
		Hashtable<Integer, String> TermDic = new Hashtable<Integer, String>();

		@Override
		public void setup(Context context)
		{
			try
			{
				Path[] caches=DistributedCache.getLocalCacheFiles(context.getConfiguration());
				if(caches == null || caches.length <= 0)
				{
					System.exit(1);
				}
				
				BufferedReader br = new BufferedReader(new FileReader(caches[0].toString()));
				String line;
				while((line=br.readLine()) != null)
				{
					String[] splitStr = line.split(":");
					int key;
					try
					{
						key  = Integer.parseInt(splitStr[1].trim());
						String value = splitStr[0];
						TermDic.put(key,value);
					}
					catch(Exception E)
					{}
 
				}
				br.close();
			}
			catch(Exception e){}
		}

		@Override
		//output<centroid,point>
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
		{
			int count = 1;
			String output = "";
			String center = value.toString().trim();
			String[] splitStr = center.split(",");
			for(String term : splitStr)
			{
				String[] point = term.split(":");
				int index = Integer.parseInt(point[0].trim());
				String weight = point[1].trim();
				output += TermDic.get(index)+":"+weight+"\n";
				count++;
				if(count > 10)
				{
					break;
				}
			}
			output += "\n\n";
			context.write(new Text(output), new Text());
		}
		
		@Override
		public void cleanup(Context context) throws IOException,InterruptedException 
		{
			// do nothing here
		}
	}
	
	
	@Override
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void main(String[] args) throws Exception 
	{
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		
		// set the path for cache, which will be loaded in ClusterMapper
		Path dataFile = new Path("/task3/wordDict");
		DistributedCache.addCacheFile(dataFile.toUri(), conf);
 
		Job job = new Job(conf);
		job.setJarByClass(KmeansCluster.class);
		
		FileInputFormat.setInputPaths(job, "/task3/centroids");
		Path outDir = new Path("/task3/topTerms-10");
		fs.delete(outDir,true);
		FileOutputFormat.setOutputPath(job, outDir);
		 
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapperClass(ClusterMapper.class);
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		 
		job.waitForCompletion(true);
		
	}



}