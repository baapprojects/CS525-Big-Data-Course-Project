import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

/*
Task H:
Find the percentage of users from each country who have visited a 
particular page. It is one of the very important query that facebook
has to do regularly for their advertisers. Advertisers wants to know
about the geographical distribution of the users that visited their 
page. Based on that, they can fine tune their business logic for the 
targeted region.

Usage: programname directory1 directory2 PageID
directory1: directory where the file mypage.txt and accesslog.txt are.
directory2: output directory
PageID: the advertiser is assumed to be one of the facebook users.

Output:
country name, percent of users
*/

public class taskH
{
		/*
		This is the first mapper that outputs the country name 
		for each facebook user,	except for the Page ID for whom 
		we are going to find the geographical distribution of the users.
		*/

		public static class MypageMap extends Mapper<LongWritable, Text, IntWritable, Text>
	{
		private final static IntWritable one = new IntWritable(1);
		private IntWritable myID = new IntWritable(0);
		private String countryName, fileTag ="M,";
		
		
		@Override
		public void map(LongWritable key, Text value, Context context)
		throws IOException, InterruptedException
		{
			Configuration conf = context.getConfiguration();
			int forWhom = Integer.parseInt(conf.get("ForWhom"));
			String line = value.toString();
			String[] splits = line.split(",");
			// get page id
			myID.set(Integer.parseInt(splits[0]));
			countryName = splits[2];
			/*
			if page id is not the page id of the advertiser,
			print the page id and the country name. those
			country name will be used for joining at the reducer.
			*/
			if (myID.get() != forWhom )
				context.write( myID , new Text(fileTag + countryName));
		}
	}

	/*
	this is the second mapper that outputs the hit counts
	of the advertiser's page. it actually outputs who accessed
	the page and a count. this outputs are aggregated at the 
	reducer and joined with the output of the first mapper to find 
	the country name of the user who accessed.
	*/
	public static class AccessLogMap extends Mapper<LongWritable, Text, IntWritable, Text>
	{
		private IntWritable whatpage = new IntWritable(0);
		private IntWritable byWho = new IntWritable(0);
		private String fileTag ="A,";

		@Override
		public void map(LongWritable key, Text value, Context context)
		throws IOException, InterruptedException
		{
			Configuration conf = context.getConfiguration();
			int forWhom = Integer.parseInt(conf.get("ForWhom"));
			String line = value.toString();
			String[] splits = line.split(",");
			// get id that accessed a page
			byWho.set(Integer.parseInt(splits[1]));
			// get id of the page that was accessed
			whatpage.set(Integer.parseInt(splits[2]));
			/*
			if the person is not the advertiser and the page
			accessed is the advertiser's page then output
			who accessed the page and a count.
			*/
			if (byWho.get() != forWhom && whatpage.get() == forWhom)
				context.write(byWho, new Text(fileTag + "1"));
		}
	}

	/*
	the reduce function is calculating the total hits of a page and 
	inserting the pageid and count in an associative array.
	Since we need total number of access to calculate the percentage
	of users in a particular country we are actually using country name
	as the key of the associative array and we are incrementing the value
	once for each unique user.
	It does not output anything to the output file. Instead I used 
	the cleanup method to output the country names and percent of users
	to the advertiser's page.
	*/

	public static class Reduce extends Reducer<IntWritable, Text, Text, Text>
	{		
		Map<String, Integer> map = new HashMap<String, Integer>();
		int total=0;
		
		@Override
		public void reduce(IntWritable key, Iterable<Text> values, Context context) 
		throws IOException, InterruptedException
		{
			String country="";
			int count=0;
			for (Text value : values) {
				String line = value.toString();
				String splits[] = line.split(",");
				
				/*
				One person can access a page many times. so for efficiency
				and faster execution we put a check whether whether a country 
				and a count for a user is found. if an entry for a user is 
				finalysed we will not check for any other access record for
				that user.
				*/
				if ( country != "" && count ==1)
					break;
				
				// getting country name from the output of the first mapper
				if(splits[0].equals("M")){
					country = splits[1];
				}
				/* checking whether that user accessed the advertiser's page
				from the output of the second mapper. From the second mapper
				we filtered pageid or user id and a count of those users who
				accessed advrtiser's page.
				So, if some user did not accessed the advertiser's page there 
				will be no record of that user in the output of the second mapper.
				So there will be no match in the following condition and that
				user will be discarded.
				*/
				else if(splits[0].equals("A")){
					count = 1;// splits[1];
				}
			}
			
			/*
			After processing all the values for a key which is a user id or
			page id, if a user accessed the advertiser's page, then the country
			variable and count variable will not be empty. when both are not empty
			we are sure that the user we are investigaing has accessed the 
			advertiser's page and we can add the user's coountry to the associative
			array for country. if that country already exists in the associative
			array, we just increment its count by one indicating that we found another
			unique user from that country.
			*/
			if ( country != "" && count ==1)
			{
				total++;
				if(!map.containsKey(country))
					map.put(country, 1);
				else
					map.put(country, map.get(country)+1);
			}
		}

		// cleanup method outputs the country names with percent of users from that country
		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException 
		{
			
			for (Map.Entry<String, Integer> entry : map.entrySet())
			{
				// the result variable is used to calculate the percentage of users from a country
				String result = String.format("%.2f", (double) entry.getValue() * 100.0 / total );
				context.write(new Text(entry.getKey()),new Text(result));
			}
		}
	}

	public static void main(String[] args) throws Exception 
	{
		Configuration conf = new Configuration();
		conf.set("ForWhom", args[2]);
	
		Job job = new Job(conf);
    job.setJarByClass(taskH.class);
    job.setJobName("taskH");

		MultipleInputs.addInputPath(job, new Path(args[0]+"/mypage.txt"), TextInputFormat.class, MypageMap.class);
		MultipleInputs.addInputPath(job, new Path(args[0]+"/accesslog.txt"), TextInputFormat.class, AccessLogMap.class);

    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    job.setNumReduceTasks(1); // using only one reducer

    job.setReducerClass(Reduce.class);

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}