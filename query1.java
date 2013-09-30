import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class query1 
{
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> 
	{

		public void map(LongWritable key, Text value, OutputCollector<Text,Text> output, Reporter reporter) throws IOException 
		{
			//Get FileName from reporter
			FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
			String filename = fileSplit.getPath().getName();

			//String line = value.toString();
			output.collect(new Text(filename),value);  			
		}
	}


	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(query1.class);
		conf.setJobName("query1");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		//FileInputFormat.setInputPaths(conf, new Path(args[0]));
		//FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		//The input is a directory [So, you'd know not just file can be mapper input, but directory]
		//Before this code running, you should put more than one file in the directory "/tmp/" in HDFS
		FileInputFormat.setInputPaths(conf, new Path("/tmp/"));
		FileOutputFormat.setOutputPath(conf, new Path("/tmp/output"));

		JobClient.runJob(conf);
	}
}