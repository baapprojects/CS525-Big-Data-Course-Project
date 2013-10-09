import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

/*
Task G:
List of all people that have set up a facebook page, but have lost interest,i.e., after some
initial time unit they have never accessed facebook again.

Usage: programname directory1 directory2
directory1: directory where the files mypage.txt and accesslog.txt are.
directory2: output directory

Output:
memberID, Member Name, first access time, last access time


*/


public class taskG{
	
	/*
	MypageMap: mapper to map mypage.txt
	outputs: ID , M, Name
	M is a tag to identify at the reducer that the input came from mypage.txt
	*/

	public static class MypageMap extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text>{
    
		private  IntWritable myID = new IntWritable(0);
		private String name, fileTag ="M,";
		
		public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException{
			String line = value.toString();
			String[] splits = line.split(",");
			myID.set(Integer.parseInt(splits[0]));
			name = splits[1];
			output.collect(myID,new Text(fileTag + name));
		}
	}

	/*
	AccessLogMap: mapper to map accesslog.txt
	outputs: ID , A, AccessTime
	A is a tag to identify at the reducer that the input came from accesslog.txt
	*/
	
	public static class AccessLogMap extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text>{
		//variables to process Transaction details	
		private IntWritable myID = new IntWritable(0);
		// to keep the programming simple we assumed that every access time is a minute
		private String accessTime,fileTag ="A,";
		
		public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException{
			String line = value.toString();
			String[] splits = line.split(",");
			myID.set(Integer.parseInt(splits[1]));
			accessTime = splits[4];
			output.collect(myID,new Text(fileTag + accessTime));
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, Text>{

		private String name, accessTime, minTime, maxTime;
		public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException{
			minTime = "1000000";
			maxTime = "0";
			while (values.hasNext()){
				String line = values.next().toString();
				String splits[] = line.split(",");
				
				/*
				since there were two input files of different infornation
				we tagged the output of the mappers so that we can identify
				those in the reducer. "M" is used to identify mapper outputs
				from MyPage dataset and "A" is used to identify output from
				the AccessLog dataset. The key which is the ID is same for
				both types of output, so that we can join them at reducer.
				*/
				if(splits[0].equals("M")){
					name = splits[1];
				}
				else if(splits[0].equals("A")){
					accessTime = splits[1];
					if(Integer.parseInt(accessTime) < Integer.parseInt(minTime))
					{
						minTime = accessTime;
					}
					if(Integer.parseInt(accessTime) > Integer.parseInt(maxTime))
					{
						maxTime = accessTime;
					}
				}
			}
			/*
			Here is the logic to find the uninterested people:
			people whose initial access time and last access time 
			only differed by the threshold nunber of days
			( 10*24*60= 14400 minutes, assuming that each
			each access time unit is a minute).
			if the difference of last access time and first access 
			time is within threshold time, then they used facebook 
			only for the initial threshold period 
			and never accessed again.
			*/
			if( (Integer.parseInt(maxTime) - Integer.parseInt(minTime)) <= 14400 )
			{
				Text text = new Text();
				text.set(name + "," + minTime + "," + maxTime);
				output.collect(key,text);	
			}
		}
	}

	public static void main(String[] args) throws Exception {
      JobConf conf = new JobConf(taskG.class);
      conf.setJobName("taskG");

      conf.setOutputKeyClass(IntWritable.class);
      conf.setOutputValueClass(Text.class);

      //conf.setMapperClass(MypageMap.class);
			//conf.setMapperClass(AccessLogMap.class);
      //conf.setCombinerClass(Reduce.class);
      conf.setReducerClass(Reduce.class);

      conf.setInputFormat(TextInputFormat.class);
      conf.setOutputFormat(TextOutputFormat.class);
	
      MultipleInputs.addInputPath(conf, new Path(args[0]+"/mypage.txt"), TextInputFormat.class, MypageMap.class);
      MultipleInputs.addInputPath(conf, new Path(args[0]+"/accesslog.txt"), TextInputFormat.class, AccessLogMap.class);
      FileOutputFormat.setOutputPath(conf, new Path(args[1]));

      JobClient.runJob(conf);
    }
}
