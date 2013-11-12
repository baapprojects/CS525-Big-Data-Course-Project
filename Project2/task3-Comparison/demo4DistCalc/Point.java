package org;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;

/*
 * This is a point class
 */
public class Point 
{ 
	public Point()
	{

	}
	 
	// get Manhant distance between two tweets
	public static double getManhtDist(String vec1,String vec2)
	{
		double dist=0.0;
		
		Set<String> dimensionSet = new HashSet<String>();
		Hashtable<String, Double> htVec1 = new Hashtable<String, Double>();
		Hashtable<String, Double> htVec2 = new Hashtable<String, Double>();
		
		// load data into hashTable
		String[] dimensions = vec1.split(",");
		for(String dimension : dimensions )
		{
			String[] point = dimension.split(":");
			String key = point[0].trim();
			dimensionSet.add(key);
			htVec1.put(key,Double.parseDouble(point[1]));
			htVec2.put(key,0.0);
		}
		
		dimensions = vec2.split(",");
		for(String dimension : dimensions )
		{
			String[] point = dimension.split(":");
			String key = point[0].trim();
			dimensionSet.add(key);
			htVec2.put(key,Double.parseDouble(point[1]));
			if(!htVec1.containsKey(key))
			{
				htVec1.put(key,0.0);
			}
		}
		
		// calculate the distance
		for(String key : dimensionSet)
		{
			dist += Math.abs(htVec1.get(key) - htVec2.get(key));
        }
		
		
		return dist;
	}
	
	public static String getVectorvalue(String vec)
	{
		vec  = vec.split("\\{")[1];
		vec  = vec.split("\\}")[0];
		return vec;
	}
	
	public static void main(String[] args)
	{
		String filePath = "centroids";
        DataInputStream dis;
        try
        {
                dis = new DataInputStream(new BufferedInputStream( new FileInputStream(filePath)));
                String fileContent = new String();
                String formatContent = new String();
                // get two vectors about tweets
                fileContent = dis.readLine() ;
                formatContent = dis.readLine() ;
                // parse these two vectors
                fileContent = Point.getVectorvalue(fileContent);
                formatContent = Point.getVectorvalue(formatContent);
                double dist =  Point.getManhtDist(fileContent, formatContent);
                
                System.out.println(dist);
                System.out.println(fileContent);
                System.out.println(formatContent);
                dis.close();
        }
        catch(FileNotFoundException e)
        {
                e.printStackTrace();
        }
        catch(IOException e)
        {
                e.printStackTrace();
        }
		
	}
}
