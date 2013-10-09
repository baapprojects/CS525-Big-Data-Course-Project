import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
//import org.apache.hadoop.mapred.lib.MultipleInputs;

public class taskA
{

public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text>
{
//ID will be the key of mapper output
private IntWritable ID = new IntWritable(0);
//Store the name attribute from mypage.txt
private String name;
private String hobby;
private String nationality;

public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter)throws IOException
{
//get value from input file, default: one line one record
String line = value.toString();
//divide the value by ",", so that we can detailed attributes later
String[] splits = line.split(",");

//Get FileName from reporter
//FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
//String filename = fileSplit.getPath().getName();
nationality=splits[2];

if(nationality.contains("Thailand"))
{
//Deal with data from mypage.txt, get ID and name
ID.set(Integer.parseInt(splits[0])); // get ID
name = splits[1];	// get name
hobby = splits[4];       //get hobby
output.collect(ID,new Text(name+","+hobby)); // output them
}


}
}


public static void main(String[] args) throws Exception
{
JobConf conf = new JobConf(taskA.class);
conf.setJobName("taskA");

//set the output key/value type of mapper (if combiner defined, this should be type for combiner)
conf.setOutputKeyClass(IntWritable.class);
conf.setOutputValueClass(Text.class);

//assign map/combiner/reducer with specified classes
conf.setMapperClass(Map.class);
//conf.setCombinerClass(Combiner.class);
//conf.setReducerClass(Reduce.class);

//use default input/output format
conf.setInputFormat(TextInputFormat.class);
conf.setOutputFormat(TextOutputFormat.class);

//set input files path
FileInputFormat.addInputPath(conf, new Path("/ywang/input/mypage.txt"));
//FileInputFormat.addInputPath(conf, new Path("/ywang/input/friends.txt"));
//set output file path
FileOutputFormat.setOutputPath(conf, new Path(args[0]));

JobClient.runJob(conf);
    }
}
