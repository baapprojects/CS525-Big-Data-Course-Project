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
		//private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(LongWritable key, Text value, OutputCollector<Text,Text> output, Reporter reporter) throws IOException 
		{
			FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
      		String filename = fileSplit.getPath().getName();
			String line = value.toString();
		    output.collect(new Text(filename),new Text(line));      			        
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

	    FileInputFormat.setInputPaths(conf, new Path(args[0]));
	    FileOutputFormat.setOutputPath(conf, new Path(args[1]));

	    JobClient.runJob(conf);
	}
}