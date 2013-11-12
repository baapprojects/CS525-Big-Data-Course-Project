package org;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.List;
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
		vec1 = Point.getTweetValue(vec1);
		vec2 = Point.getTweetValue(vec2);
		
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
	
	public static String getTweetValue(String vec)
	{
		if(vec.contains("{") && vec.contains("}"))
		{
			vec  = vec.split("\\{")[1];
			vec  = vec.split("\\}")[0];
			return vec;
		}
		else
		{
			return vec;
		}
		
	}
	
	public static String getSum(ArrayList<String> vecs)
	{	
		Set<String> dimensionSet = new HashSet<String>();
		Hashtable<String, Double> htVec = new Hashtable<String, Double>();
		StringBuilder sum = new StringBuilder();
		
		// load data into hashTable
		for(String vec : vecs )
		{
			vec = Point.getTweetValue(vec);
			String[] dimensions = vec.split(",");
			for(String dimension : dimensions )
			{
				String[] point = dimension.split(":");
				String key = point[0].trim();
				dimensionSet.add(key);
				if(!htVec.containsKey(key))
				{
					htVec.put(key,Double.parseDouble(point[1]));
				}
				else
				{
					htVec.put(key,htVec.get(key)+Double.parseDouble(point[1]));
				}
			}
		}
	
		// calculate the distance
		for(String key : dimensionSet)
		{
			sum.append(key+":"+String.valueOf(htVec.get(key)) + ",");
        }
		if(sum.length() > 0)
		{
			sum.setLength(sum.length() - 1);
		}
		
		return sum.toString();
	}
	
	
	public static String getTweetIndex(String vec)
	{
		String index  = vec.split(":")[0];
		return index.trim();
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

                double dist =  Point.getManhtDist(fileContent, formatContent);
                
                System.out.println(dist);
                System.out.println(Point.getTweetIndex(fileContent));
                
                ArrayList<String> vecs = new ArrayList<String>();
                System.out.println("1--"+Point.getSum(vecs));
                vecs.add(formatContent);
                System.out.println("2--"+Point.getSum(vecs));
                //fileContent = Point.getTweetValue(fileContent);
                vecs.add(fileContent);
                System.out.println("3--"+Point.getSum(vecs));
                System.out.println(formatContent);
                System.out.println(fileContent);
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
