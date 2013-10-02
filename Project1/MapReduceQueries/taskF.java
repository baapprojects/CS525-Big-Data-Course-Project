import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

public class taskF
{

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text>
	{
		//variables to process Customer details		
		private IntWritable ID = new IntWritable(0);
		private String name;

		public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException
		{
			String line = value.toString();
			String[] splits = line.split(",");
			//Get FileName from reporter
			FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
			String filename = fileSplit.getPath().getName();

			if(filename.contains("friends"))
			{
				//Deal with mypage.txt, get ID and name
				ID.set(Integer.parseInt(splits[0]));
				name = splits[1];
				output.collect(ID,new Text(name));
			}
			else
			{
				//Deal with friend.txt, get MyFriend is fine
				ID.set(Integer.parseInt(splits[2]));
				output.collect(ID,new Text("1"));
			}
			
		}
	}

	public static class Combiner extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, Text>
	{
		public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException
		{
			int countToBeFriendOfOthers = 0;  
			String name = null;
			while (values.hasNext())
			{
				String line = values.next().toString();

				//if data from mypage.txt
				if(line.length() > 5)
				{
					name = line;
					output.collect(key,new Text(name));	//This line cannot be moved outside while block, and I don't know why
				}
				else
				{
					countToBeFriendOfOthers++;//= countToBeFriendOfOthers + Integer.parseInt(line);
				}
			}
			output.collect(key,new Text(String.valueOf(countToBeFriendOfOthers)));	
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, Text, IntWritable>
	{		
		public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<Text, IntWritable> output, Reporter reporter)throws IOException
		{
			String name = "ERROR NAME";
			int countToBeFriendOfOthers = 0;  

			while (values.hasNext())
			{
				String line = values.next().toString();

				//if data from mypage.txt
				if(line.length() > 9)
				{
					name = line;
				}
				else
				{
					countToBeFriendOfOthers = countToBeFriendOfOthers + Integer.parseInt(line);
				}
			}
			
			output.collect(new Text(name),new IntWritable(countToBeFriendOfOthers));	
		}
	}

	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(taskF.class);
		conf.setJobName("taskF");

		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Combiner.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.addInputPath(conf, new Path("/hzhou/smallInput/accesslog.txt"));
		FileInputFormat.addInputPath(conf, new Path("/hzhou/smallInput/friends.txt"));
		FileOutputFormat.setOutputPath(conf, new Path(args[0]));

		JobClient.runJob(conf);
    }
}