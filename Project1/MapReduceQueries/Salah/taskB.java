import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

/*
Task B:
Reports for each country, how many of its citizens have facebook page.

Usage: programname directory1 directory2
directory1: directory where the file mypage.txt is.
directory2: output directory

Output:
Country Name, # of users

*/

public class taskB
{
	/*
	the mapper outputs the country name and a count
	*/

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>
	{
	      private final static IntWritable one = new IntWritable(1);
	      private Text country = new Text();
	      
	      public void map(LongWritable key, Text value, OutputCollector<Text,IntWritable> output, Reporter reporter) throws IOException {
	        String line = value.toString();
	        String[] splits = line.split(",");
					// get country name
	        country.set(splits[2]);
       		output.collect(country, one);
	      }
	}

	/*
	the reducer is aggregating the total count for a country
	to a variable called sum and outputs country name and total users.
	*/
	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>
	{		
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter)throws IOException
		{
			int sum = 0;
			while (values.hasNext()) {
				sum += values.next().get();
			}
			output.collect(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(taskB.class);
		conf.setJobName("taskB");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]+"/mypage.txt"));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
    }
}