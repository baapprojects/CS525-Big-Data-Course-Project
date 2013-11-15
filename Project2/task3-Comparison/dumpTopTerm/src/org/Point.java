package org;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;
import java.text.DecimalFormat;

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
		DecimalFormat df = new DecimalFormat("0.0000");
		
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
			sum.append(key+":"+df.format(htVec.get(key)) + ",");
        }
		if(sum.length() > 0)
		{
			sum.setLength(sum.length() - 1);
		}
		
		return sum.toString();
	}
	
	public static String getSum(String vec1, String vec2)
	{	
		Set<String> dimensionSet = new HashSet<String>();
		Hashtable<String, Double> htVec1 = new Hashtable<String, Double>();
		Hashtable<String, Double> htVec2 = new Hashtable<String, Double>();
		StringBuilder sum = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.0000");
		
		// load data into hashTable
		vec1 = Point.getTweetValue(vec1);
		vec2 = Point.getTweetValue(vec2);
		if(!vec1.equals(""))
		{
			String[] dimensions = vec1.split(",");
			for(String dimension : dimensions )
			{
				String[] point = dimension.split(":");
				String key = point[0].trim();
				dimensionSet.add(key);
				htVec1.put(key,Double.parseDouble(point[1]));
				htVec2.put(key,0.0);
			}
		}
		if(!vec2.equals(""))
		{
			String[] dimensions = vec2.split(",");
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
		}
		// calculate the distance
		for(String key : dimensionSet)
		{
			sum.append(key+":"+df.format(htVec1.get(key) + htVec2.get(key)) + ",");
        }
		if(sum.length() > 0)
		{
			sum.setLength(sum.length() - 1);
		}
		
		return sum.toString();
	}
	
	public static String getNewCentroid(String vectorSum, int count)
	{
		StringBuilder sum = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.0000");
		
		// load data into hashTable
		if(!vectorSum.equals(""))
		{
			String[] dimensions = vectorSum.split(",");
			for(String dimension : dimensions )
			{
				String[] point = dimension.split(":");
				String key = point[0].trim();
				sum.append(key+":"+df.format(Double.parseDouble(point[1])/count) + ",");
			}
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

	public static String getTopTerms(String vec, int count)
	{
		StringBuilder topTerms = new StringBuilder();
		Set<String> dimensionSet = new HashSet<String>();
		Hashtable<String, Double> htVec = new Hashtable<String, Double>();
		DecimalFormat df = new DecimalFormat("0.0000");
		
		// load data into hashTable
		
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
		
		int topCount = dimensions.length > count ? count : dimensions.length;

		for(int i = 0; i < topCount; i++)
		{
			String index = "";
			double value = Double.MIN_VALUE;
			for(String key : dimensionSet)
			{
				if(index.equals(""))
				{
					index = key;
					value = htVec.get(key);
				}
				else
				{
					if(value < htVec.get(key))
					{
						index = key;
						value = htVec.get(key);
					}
				}
			}
			
			dimensionSet.remove(index);
			topTerms.append(index+":"+df.format(htVec.get(index)) + ",");
		}
		if(topTerms.length() > 0)
		{
			topTerms.setLength(topTerms.length() - 1);
		}

		return topTerms.toString();
	}
	
}
