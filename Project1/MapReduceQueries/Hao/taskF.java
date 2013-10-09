import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

// Misson of Task F: Get the person who does not care his friends so much
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

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text>
	{
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)throws IOException
		{
			Set<String> setF = new HashSet<String>();
			Set<String> setA = new HashSet<String>();

			while (values.hasNext())
			{
				String line = values.next().toString();
				String[] splits = line.split(",");

				// determine the source of this record
				if(splits[0].contains("F"))
				{
					// record comes from friends.txt, then add this to setF
					setF.add(splits[1]);
				}
				else
				{
					// record comes from accesslog.txt, then add this to setA
					setA.add(splits[1]);
				}
				
			}
			// Now I have get two sets: setF & setA
			// just output the records which are in setF, but not in setA
			for(String setFStr : setF)
			{
				boolean isElementFoundInSetA = false;
				for(String setAStr : setA)
				{
					if(setFStr.equals(setAStr))
					{
						isElementFoundInSetA = true;
						break;
					}
				}

				if(isElementFoundInSetA == false)
				{
					output.collect(key,new Text(setFStr));	
				}
			}

			
		}
	}

	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(taskF.class);
		conf.setJobName("taskF");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.addInputPath(conf, new Path("/hzhou/input/accesslog.txt"));
		FileInputFormat.addInputPath(conf, new Path("/hzhou/input/friends.txt"));
		FileOutputFormat.setOutputPath(conf, new Path("/hzhou/outputF/F1"));

		JobClient.runJob(conf);
    }
}