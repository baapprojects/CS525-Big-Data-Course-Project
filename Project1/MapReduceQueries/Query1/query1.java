import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
	
public class query1 
{
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text> 
	{
	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();
	      
	    public void map(LongWritable key, Text value, OutputCollector<IntWritable,Text> output, Reporter reporter) throws IOException 
	    {
	    	//Get one line from input file, here will be customers.txt
	        String line = value.toString();
	        //Divide this line by ",""
	        String[] splits = line.split(",");
	        //Get countryCode
	        int courtryCodeValue=Integer.parseInt(splits[3]);

	        if(courtryCodeValue>=2 && courtryCodeValue<=6)
	        {
	        	//Set one variable
	        	one.set(courtryCodeValue);
	        	//Set word variable
	        	word.set(line);
	        	//set one as key, word as value
	        	output.collect(one, word);
	        }	        			        
	    }
	}
	
		
    public static void main(String[] args) throws Exception 
    {
      JobConf conf = new JobConf(query1.class);
      conf.setJobName("query1");


      //for map
      conf.setOutputKeyClass(IntWritable.class);
      conf.setOutputValueClass(Text.class);

      conf.setMapperClass(Map.class);

      conf.setInputFormat(TextInputFormat.class);
      conf.setOutputFormat(TextOutputFormat.class);

      FileInputFormat.setInputPaths(conf, new Path(args[0]));
      FileOutputFormat.setOutputPath(conf, new Path(args[1]));

      JobClient.runJob(conf);
    }
}
		   


