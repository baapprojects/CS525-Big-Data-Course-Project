import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
//import org.apache.hadoop.mapred.lib.MultipleInputs;

public class taskD
{

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text>
	{
		//ID will be the key of mapper output
		private  IntWritable ID = new IntWritable(0);
		//Store the name attribute from mypage.txt
		private String name;

		public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException
		{
			//get value from input file, default: one line one record
			String line = value.toString();
			//divide the value by ",", so that we can detailed attributes later
			String[] splits = line.split(",");

			//Get FileName from reporter
			FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
			String filename = fileSplit.getPath().getName();

			if(filename.contains("mypage"))
			{
				//Deal with data from mypage.txt, get ID and name
				ID.set(Integer.parseInt(splits[0])); // get ID
				name = splits[1];					 // get name
				output.collect(ID,new Text(name));   // output them
			}
			else
			{
				//Deal with data from friend.txt, get myFriend attribute only
				ID.set(Integer.parseInt(splits[2]));  //get myFriend 
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

				//if data from mypage.txt, then we get the name attribute (as the lenght of name is no shorter than 10)
				if(line.length() > 5)
				{
					// if the length of value is longer than 5, it means that the value is the name attribute
					name = line;
					output.collect(key,new Text(name));	//This line cannot be moved outside while block
				}
				else
				{
					//if length of value is shotrter than 5, it means some guys add this personID as a friend
					//if records are from myFriend.txt, just add the countToBeFriendOfOthers by 1
					countToBeFriendOfOthers++;
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

			//it seems that I always lost one record in the reducer side, I think I do something wrong here in while loop
			while (values.hasNext())
			{
				String line = values.next().toString();

				//if data from mypage.txt
				//get name, becuase we know the length of name is between 10 and 20
				if(line.length() > 9)
				{
					name = line;
				}
				else
				{
					//if the value field is not name attribute, it must be the count
					countToBeFriendOfOthers = countToBeFriendOfOthers + Integer.parseInt(line);
				}
			}
			
			output.collect(new Text(name),new IntWritable(countToBeFriendOfOthers));	
		}
	}

	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(taskD.class);
		conf.setJobName("taskD");

		//set the output key/value type of mapper (if combiner defined, this should be type for combiner)
		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(Text.class);

		//assign map/combiner/reducer with specified classes
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Combiner.class);
		conf.setReducerClass(Reduce.class);

		//use default input/output format
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		//set input files path
		FileInputFormat.addInputPath(conf, new Path("/hzhou/smallInput/mypage.txt"));
		FileInputFormat.addInputPath(conf, new Path("/hzhou/smallInput/friends.txt"));
		//set output file path
		FileOutputFormat.setOutputPath(conf, new Path(args[0]));

		JobClient.runJob(conf);
    }
}