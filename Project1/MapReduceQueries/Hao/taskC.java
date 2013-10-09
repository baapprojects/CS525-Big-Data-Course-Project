import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

/*
 * Task C
 * Mapper:  output -> all records are with the same key=1, so that all records will be sent to a single combiner/reducer at once
 * Combiner can be used in this program [you can just comment the combiner line in main to disable combiner]
 * Reducer: a single reducer deals with all data with a hashtable, after all data received, the hashtable will be sorted DESC, then output the fisrt 10 records
 */
public class taskC
{
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text>
	{
		private  Text ID = new Text();

		public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException
		{
			String line = value.toString();
			String[] splits = line.split(",");
			
			ID.set(splits[2]+",1");
			// set all records key to 1
			output.collect(new IntWritable(1),new Text(ID));
			
			
		}
	}

	public static class Combiner extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, Text>
	{
		Hashtable<Integer, Integer> ht = new Hashtable<Integer, Integer>();

		public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException
		{
			int countOfAccess = 0;  
			while (values.hasNext())
			{
				int currentKey = 0;
				String line = values.next().toString();
				String[] splits = line.split(",");

				// get the real key for each record
				currentKey = Integer.parseInt(splits[0]);
				// get the occurrence for each record
				countOfAccess = Integer.parseInt(splits[1]);

				// update hashtable
				if(!ht.containsKey(currentKey))
				{
					ht.put(currentKey, countOfAccess);
				}
				else
				{
					ht.put(currentKey, ht.get(currentKey)+countOfAccess);
				}
			}
			
			// here, the hashtable is already generated 
			// now, we output the records 
			ArrayList<java.util.Map.Entry<Integer, Integer>> l = new ArrayList(ht.entrySet());
			for(java.util.Map.Entry<Integer, Integer> d:l) 
			{
				output.collect(new IntWritable(1), new Text(String.valueOf(d.getKey())+","+String.valueOf(d.getValue())));	
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, IntWritable>
	{	
		Hashtable<Integer, Integer> ht = new Hashtable<Integer, Integer>();
		public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, IntWritable> output, Reporter reporter)throws IOException
		{
			int countOfAccess = 0;  
			// determine how many records to ouotput
			int topCount = 10;	

			while (values.hasNext())
			{
				int currentKey = 0;
				String line = values.next().toString();
				String[] splits = line.split(",");

				// get the real key for each record
				currentKey = Integer.parseInt(splits[0]);
				// get the occurrence for each record
				countOfAccess = Integer.parseInt(splits[1]);

				// update hashtable
				if(!ht.containsKey(currentKey))
				{
					ht.put(currentKey, countOfAccess);
				}
				else
				{
					ht.put(currentKey, ht.get(currentKey)+countOfAccess);
				}
				
			} 

			// after while loop, we will get a hashtable

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

			// output the first topCount[10] records
			for(java.util.Map.Entry<Integer, Integer> d:l) 
			{
				output.collect(new IntWritable(d.getKey()),new IntWritable(d.getValue()));	
				topCount--;
				if(topCount == 0)
				{
					break;
				}
			}
		}
	}

	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(taskC.class);
		conf.setJobName("taskC");

		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		// you can disable combiner here by commenting the line below
		// with combiner, the CPU time = 14890 
		// without combiner, the CPU time = 17560
		conf.setCombinerClass(Combiner.class);
		conf.setReducerClass(Reduce.class);
		//conf.setNumReduceTasks(1);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.addInputPath(conf, new Path("/hzhou/input/accesslog.txt"));
		FileOutputFormat.setOutputPath(conf, new Path("/hzhou/output/taskCHao"));

		JobClient.runJob(conf);
	}
}