import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

public class taskD
{

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
	{
		//variables to process Customer details		
		private  Text ID = new Text(0);
		private String name;

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)throws IOException
		{
			String line = value.toString();
			String[] splits = line.split(",");
			//Get FileName from reporter
			FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
			String filename = fileSplit.getPath().getName();

			if(filename.contains("mypage"))
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


	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text>
	{
		//variebles to aid the join process
		
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)throws IOException
		{
			String name = "ERROR NAME";
			int countToBeFriendOfOthers = 0;  

			while (values.hasNext())
			{
				String line = values.next().toString();

				//if data from mypage.txt
				if(line.length() > 2)
				{
					name = line;
				}
				else
				{
					countToBeFriendOfOthers++;
				}
			}
			
			output.collect(new Text(name),new Text(countToBeFriendOfOthers));	
		}
	}

	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(taskD.class);
		conf.setJobName("taskD");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.addInputPath(conf, new Path("/hzhou/input/mypage.txt"));
		FileInputFormat.addInputPath(conf, new Path("/hzhou/input/friends.txt"));
		FileOutputFormat.setOutputPath(conf, new Path(args[0]));

		JobClient.runJob(conf);
    }
}