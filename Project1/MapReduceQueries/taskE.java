import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

public class taskE
{
	public static class initMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
	{
		
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException
		{
			String line = value.toString();
			String[] splits = line.split(",");

			// key : personID (byWho)
			// value : pageID
			output.collect(new Text(splits[1]+","+splits[2]),new Text());
		}
	}
	
	
	public static class getSumReduce extends MapReduceBase implements Reducer<Text, Text, Text, Text>
	{

		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException
		{
			int sum = 0;

			String line = key.toString();
			
			String[] splits = line.split(",");
			String personID = splits[0];

			while (values.hasNext())
			{
				String noSenseHere = values.next().toString();

				sum++;
			}

			output.collect(new Text(),new Text(personID+","+String.valueOf(sum)));	
			//output.collect(new Text(),new Text(String.valueOf(sum)));	
			//output.collect(new Text(),new Text(line));
		}
	}
	
	
	public static class newKeyMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
	{	
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException
		{

			String line = value.toString();
			String[] splits = line.split(",");

			//key -> personID
			//Valeu -> access amount for distinct page     //Valeu -> distinct pageID : access amount for this page
			output.collect(new Text(splits[0].trim()),new Text(splits[1]));
		}
	}
	
	public static class outPutReduce extends MapReduceBase implements Reducer<Text, Text, Text, Text>
	{
		
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException
		{
			int totalAccess = 0;
			int distinctPageAmount = 0;
			while (values.hasNext())
			{
				distinctPageAmount++;

				totalAccess += Integer.parseInt(values.next().toString());
			}

			output.collect(key,new Text(String.valueOf(totalAccess)+" , "+String.valueOf(distinctPageAmount)));	
		}
	}
	

	public static void main(String[] args) throws Exception 
	{
		//JOB1
		JobConf conf = new JobConf(taskE.class);
		conf.setJobName("taskE-1");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		conf.setMapperClass(initMap.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(getSumReduce.class);
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		FileInputFormat.setInputPaths(conf, new Path("/hzhou/smallInput/accesslog.txt"));
		FileOutputFormat.setOutputPath(conf, new Path("/hzhou/outputE/tmp"));
		JobClient.runJob(conf);	

		//JOB2
		
		JobConf conf1 = new JobConf(taskE.class);
		conf1.setJobName("taskE-2");

		conf1.setOutputKeyClass(Text.class);
		conf1.setOutputValueClass(Text.class);
		conf1.setMapperClass(newKeyMap.class);
		conf1.setReducerClass(outPutReduce.class);
		conf1.setInputFormat(TextInputFormat.class);
		conf1.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf1, new Path("/hzhou/outputE/tmp"));
		FileOutputFormat.setOutputPath(conf1, new Path("/hzhou/outputE/E1"));
		JobClient.runJob(conf1);
		

	}
}
