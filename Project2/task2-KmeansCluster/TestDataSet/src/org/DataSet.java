package org;

import java.io.FileWriter;
import java.util.Random;

public class DataSet 
{
	private static void CreateDataSet() 
	{
		int count=1;
		float x;
		float y;
		String lineRecordString;
		try 
		{
			 	
			// generate the centroids
			FileWriter fwCentroids = new FileWriter("initK"); 
			while(count<=10)
			{
				x=new Random().nextFloat()*10000;
				y=new Random().nextFloat()*10000;
				lineRecordString=String.valueOf(x)+","+String.valueOf(y)+"\r\n";	 
				fwCentroids.write(lineRecordString);
				count++;
			}
			fwCentroids.close(); 
			
			// generate point for KmeansCluster
			count=1;
			FileWriter fwTestPoints = new FileWriter("kmeans"); 

			// Make 6000000 smaller when you do the test, as it is time-consuming, when the dataSet is very large
			// The first step it to make your algorithm work, it doesn't hurt with small dataSet

			while(count<=6000000)
			{
				x=new Random().nextFloat()*10000;
				y=new Random().nextFloat()*10000;
				lineRecordString=String.valueOf(x)+","+String.valueOf(y)+"\r\n";
				fwTestPoints.write(lineRecordString);
				count++;
			}
			fwTestPoints.close(); 
		} 
		catch (Exception e) 
		{ 
			// do nothing here
		} 
	}
	public static void main(String[] args) 
	{
		CreateDataSet();
		System.out.println("Done!");
	}

}
