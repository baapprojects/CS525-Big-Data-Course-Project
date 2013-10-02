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

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
	{
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)throws IOException
		{
			String tag = null;
			String keyID = null;
			String refID = null;

			String line = value.toString();
			String[] splits = line.split(",");
			//Get FileName from reporter
			FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
			String filename = fileSplit.getPath().getName();

			if(filename.contains("friends"))
			{
				tag = "F";
				keyID = splits[1];
				refID = splits[2]; //friend ID
			} 
			else
			{
				tag = "A";
				keyID = splits[2];
				refID = splits[1]; //by Who
			} 

			output.collect(new Text(keyID),new Text(tag +","+ refID));
			
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, IntWritable>
	{
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, IntWritable> output, Reporter reporter)throws IOException
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

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Combiner.class);
		//conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.addInputPath(conf, new Path("/hzhou/smallInput/accesslog.txt"));
		FileInputFormat.addInputPath(conf, new Path("/hzhou/smallInput/friends.txt"));
		FileOutputFormat.setOutputPath(conf, new Path("/hzhou/outputF/F1"));

		JobClient.runJob(conf);
    }
}