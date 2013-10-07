import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

public class taskC
{

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text>
	{
		//variables to process Customer details		
		private  IntWritable ID = new IntWritable(0);
		private String name;

		public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException
		{
			String line = value.toString();
			String[] splits = line.split(",");
			
			ID.set(Integer.parseInt(splits[2]));
			output.collect(ID,new Text());
			
			
		}
	}

	public static class Combiner extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, Text>
	{
		public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException
		{
			int countOfAccess = 0;  
			while (values.hasNext())
			{
				String line = values.next().toString();

				countOfAccess++;
			}
			output.collect(new IntWritable(1),new Text(key.toString()+","+String.valueOf(countOfAccess)));	
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, IntWritable>
	{	
		
		public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, IntWritable> output, Reporter reporter)throws IOException
		{
			int countOfAccess = 0;  
			Hashtable<Integer, Integer> ht = new Hashtable<Integer, Integer>();
			int topCount = 10;	
			while (values.hasNext())
			{
				int currentKey = 0;
				String line = values.next().toString();
				String[] splits = line.split(",");

				currentKey = Integer.parseInt(splits[0]);
				countOfAccess = Integer.parseInt(splits[1]);

				if(!ht.containsKey(currentKey))
				{
					ht.put(currentKey, countOfAccess);
				}
				else
				{
					ht.put(currentKey, ht.get(currentKey)+countOfAccess);
				}
				
			} // after while loop, I will get a hashtable

			//Transfer as List and sort it
			ArrayList<java.util.Map.Entry<Integer, Integer>> l = new ArrayList(ht.entrySet());
			//sort the hashtable, find this part code here: http://stackoverflow.com/questions/5176771/sort-hashtable-by-values
			
			Collections.sort(l, new Comparator<java.util.Map.Entry<Integer, Integer>>()
			{
				public int compare(java.util.Map.Entry<Integer, Integer> o1, java.util.Map.Entry<Integer, Integer> o2) 
				{
					return o2.getValue().compareTo(o1.getValue());
				}
			});

			for(java.util.Map.Entry<Integer, Integer> d:l) 
			{
				//System.out.println("\t"+d.getKey()+" : "+d.getValue());
				output.collect(new IntWritable(d.getKey()),new IntWritable(d.getValue()));	
				topCount--;
				if(topCount == 0)
				{
					//output.collect(new IntWritable(d.getKey()),new IntWritable(d.getValue()));	
					break;
				}
			}
			/*
			Enumeration<Integer> names; 
			int str; 
				
			names = ht.keys(); 
			while(names.hasMoreElements()) 
			{ 
				str = names.nextElement(); 
				output.collect(new IntWritable(str),new IntWritable(ht.get(str)));	
				//System.out.println(str + ": " + ht.get(str)); 
			}
			*/
		}
	}

	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(taskC.class);
		conf.setJobName("taskC");

		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Combiner.class);
		conf.setReducerClass(Reduce.class);
		conf.setNumReduceTasks(1);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.addInputPath(conf, new Path("/hzhou/smallInput/accesslog.txt"));
		FileOutputFormat.setOutputPath(conf, new Path("/hzhou/smallOutput/C_reducer_top10_Ver2"));

		JobClient.runJob(conf);
	}
}