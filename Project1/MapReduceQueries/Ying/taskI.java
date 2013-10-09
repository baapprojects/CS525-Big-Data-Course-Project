import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class taskI
{

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text,Text, Text>
	{
		private String nationality = null;

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)throws IOException
		{
			//get value from input file, default: one line one record
			String line = value.toString();
			//divide the value by ",", so that we can detailed attributes later
			String[] splits = line.split(",");

			nationality = splits[2];	// get nationality
			output.collect(new Text(nationality),new Text("1")); // output them
			
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text>
	{	
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)throws IOException
		{
			int countToBenationality = 0;
			//it seems that I always lost one record in the reducer side, I think I do something wrong here in while loop
			while (values.hasNext())
			{
				String line = values.next().toString();

				countToBenationality += Integer.parseInt(line) ;
				
			}

			output.collect(key,new Text(String.valueOf(countToBenationality)));	
		}
	}

	public static void main(String[] args) throws Exception
	{
		JobConf conf = new JobConf(taskI.class);
		conf.setJobName("taskI");

		//set the output key/value type of mapper (if combiner defined, this should be type for combiner)
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		//assign map/combiner/reducer with specified classes
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		//use default input/output format
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		//set input files path
		FileInputFormat.addInputPath(conf, new Path("/hzhou/input/mypage.txt"));
		//set output file path
		FileOutputFormat.setOutputPath(conf, new Path("/hzhou/output/taskI"));

		JobClient.runJob(conf);

	}
}
