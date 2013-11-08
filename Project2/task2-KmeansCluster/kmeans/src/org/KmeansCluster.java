package org;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;

public class KmeansCluster extends Configured implements Tool
{
    private static final int MAXITERATIONS = 6;
    private static final double THRESHOLD = 70;


    public static boolean stopIteration(Configuration conf) throws IOException //called in main
    {
        FileSystem fs = FileSystem.get(conf);
        Path pervCenterFile = new Path("/hzhou/input/initK");
        Path currentCenterFile = new Path("/hzhou/output/newCentroid/part-r-00000");
        if(!(fs.exists(pervCenterFile) && fs.exists(currentCenterFile)))
        {
            System.exit(1);
        }
        //check whether the centers have changed or not to determine to do iteration or not
        boolean stop=true;
        String line1;
 
        FSDataInputStream in1 = fs.open(currentCenterFile);
        InputStreamReader isr1 = new InputStreamReader(in1);
        BufferedReader br1 = new BufferedReader(isr1);

        while((line1 = br1.readLine()) != null)
        {
            String []str1 = line1.split(",");
            int isntChange = Integer.parseInt(str1[2].trim());
            if(isntChange < 1)
            {
                stop = false;
                break;
            }

        }
        
        
        //if another iteration is needed, then replace previous controids with current centroids
        if(stop == false)
        {
            fs.delete(pervCenterFile,true);
            if(fs.rename(currentCenterFile, pervCenterFile) == false)
            {
                System.exit(1);
            }
        }
        return stop;
    }
     
    
    public static class ClusterMapper extends Mapper<LongWritable, Text, Text, Text>  //output<centroid,point>
    {
        Vector<Point> centers = new Vector<Point>();
        Point point = new Point();
        int k = 0;
        @Override
        //clear centers
        public void setup(Context context)
        {
            try
            {
				Path[] caches=DistributedCache.getLocalCacheFiles(context.getConfiguration());
				if(caches == null || caches.length <= 0)
				{
					System.exit(1);
				}
				
				BufferedReader br = new BufferedReader(new FileReader(caches[0].toString()));
				Point point;
				String line;
				while((line=br.readLine()) != null)
				{
					point = new Point();
					String[] str = line.split(",");
					for(int i = 0;i < Point.DIMENTION; i++)
					{
						point.arr[i] = Double.parseDouble(str[i]);
					}
					centers.add(point);
					k++;           
				}
				br.close();
            }
			catch(Exception e){}
        }
        @Override
        //output<centroid,point>
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
        {
			
			int index = -1;
			double minDist = Double.MAX_VALUE;
            String[] str = value.toString().split(",");
            for(int i = 0;i < Point.DIMENTION; i++)
            {
                point.arr[i] = Double.parseDouble(str[i]);
			}

            for(int i = 0;i < k; i++)
            {
                double dist = Point.getEulerDist(point, centers.get(i));
                if(dist < minDist)
                {
                    minDist = dist;
                    index = i;
                }
            }
            context.write(new Text(centers.get(index).toString()), new Text(point.toString()));
        }
        
        @Override
        public void cleanup(Context context) throws IOException,InterruptedException 
        {

        }
    }
     
    public static class Combiner extends Reducer<Text, Text, Text, Text> //value=Point_Sum+count
    {      
        @Override
        //update every centroid except the last one
        public void reduce(Text key,Iterable<Text> values,Context context) throws IOException,InterruptedException
        {
			Point sumPoint = new Point();
			String outputValue;
			int count=0;
            while(values.iterator().hasNext())
            {
				String line = values.iterator().next().toString();
                String[] str1 = line.split(":");
                
                if(str1.length == 2)
                {
					count += Integer.parseInt(str1[1]);
				}
				
                String[] str = str1[0].split(",");
                for(int i = 0;i < Point.DIMENTION;i ++)
                {
                    sumPoint.arr[i] += Double.parseDouble(str[i]);
				}
                count++;
            }
			outputValue = sumPoint.toString() + ":" + String.valueOf(count);
            context.write(key, new Text(outputValue));
        }
    }
 
	public static class UpdateCenterReducer extends Reducer<Text, Text, Text, Text> 
    {
		@Override
        public void setup(Context context)
        {

        }
 
        @Override
        public void reduce(Text key,Iterable<Text> values,Context context) throws IOException,InterruptedException
        {
			int count = 0;
			Point sumPoint = new Point();
			Point newCenterPoint = new Point();
			//String outputKey;
            while(values.iterator().hasNext())
            {
                String line = values.iterator().next().toString();
                String[] str = line.split(":");
                String[] pointStr = str[0].split(",");
                count += Integer.parseInt(str[1]);
                for(int i = 0;i < Point.DIMENTION; i++)
                {
                    sumPoint.arr[i] += Double.parseDouble(pointStr[i]);
				}
            }
            for(int i = 0; i < Point.DIMENTION; i++)
            {
                newCenterPoint.arr[i] = sumPoint.arr[i]/count;
			}
            
			String[] str = key.toString().split(",");
			Point preCentroid = new Point();
			for(int i = 0; i < Point.DIMENTION; i++)
            {
				preCentroid.arr[i] = Double.parseDouble(str[i]);
			}
			if(Point.getEulerDist(preCentroid, newCenterPoint) <= THRESHOLD) // compare old and new centroids
            {
				context.write(new Text(newCenterPoint.toString()),new Text(",1"));
			}
			else
			{
				context.write(new Text(newCenterPoint.toString()),new Text(",0"));
			}
        }
        
        @Override
        public void cleanup(Context context) throws IOException,InterruptedException 
        {

        }
    }
    @Override
    public int run(String[] args) throws Exception 
    {
        Configuration conf = getConf();
        FileSystem fs = FileSystem.get(conf);
        Job job = new Job(conf);
        job.setJarByClass(KmeansCluster.class);
        
        FileInputFormat.setInputPaths(job, "/hzhou/input/kmeans");
        Path outDir = new Path("/hzhou/output/newCentroid");
        fs.delete(outDir,true);
        FileOutputFormat.setOutputPath(job, outDir);
         
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setMapperClass(ClusterMapper.class);
        job.setCombinerClass(Combiner.class);
        job.setReducerClass(UpdateCenterReducer.class);
        job.setNumReduceTasks(1);//
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
         
        return job.waitForCompletion(true)?0:1;
    }
    
    
    public static void main(String[] args) throws Exception 
    {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
         
        Path dataFile = new Path("/hzhou/input/initK");
        DistributedCache.addCacheFile(dataFile.toUri(), conf);
 
        int iteration = 1;
        int success = 1;
        do 
        {
            success ^= ToolRunner.run(conf, new KmeansCluster(), args);
            iteration++;
        } while (success == 1 && iteration < MAXITERATIONS && (!stopIteration(conf)));
         
		
		// for final output(just a mapper only task)
		
        Job job = new Job(conf);
        job.setJarByClass(KmeansCluster.class);
        
        FileInputFormat.setInputPaths(job, "/hzhou/output/newCentroid/part-r-00000");
        Path outDir = new Path("/hzhou/output/final");
        fs.delete(outDir,true);
        FileOutputFormat.setOutputPath(job, outDir);
         
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setMapperClass(ClusterMapper.class);
        job.setNumReduceTasks(0);
        job.setOutputKeyClass(Point.class);
        job.setOutputValueClass(Point.class);
         
        job.waitForCompletion(true);
        
    }
}