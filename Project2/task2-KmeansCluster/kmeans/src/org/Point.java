package org;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.io.Writable;

public class Point implements Writable 
{
	
	public static final int DIMENTION = 2;
	public double arr[];
	 
	public Point()
	{
		arr = new double[DIMENTION];
		for( int i = 0;i < DIMENTION; ++i)
		{
			arr[i]=0;
		}
	}
	 
	public static double getEulerDist(Point vec1,Point vec2)
	{
		if(!(vec1.arr.length == DIMENTION && vec2.arr.length == DIMENTION))
		{
			System.exit(1);
		}
		double dist=0.0;
		for(int i=0;i<DIMENTION;++i)
		{
			dist+=(vec1.arr[i]-vec2.arr[i])*(vec1.arr[i]-vec2.arr[i]);
		}
		return Math.sqrt(dist);
	}
	public static double getManhtDist(Point vec1,Point vec2)
	{
		if(!(vec1.arr.length==DIMENTION && vec2.arr.length==DIMENTION))
		{
			System.exit(1);
		}
		double dist=0.0;
		for(int i=0;i<DIMENTION;++i)
		{
			dist += Math.abs(vec1.arr[i]-vec2.arr[i]);
		}
		return dist;
	}
	 
	public void clear()
	{
		for(int i = 0;i < arr.length;i++)
		{
			arr[i] = 0.0;
		}
	}
	 
	public String toString()
	{
		DecimalFormat df = new DecimalFormat("0.0000");
		String rect = String.valueOf(df.format(arr[0]));
		for(int i = 1;i < DIMENTION; i++)
		{
			rect += "," + String.valueOf(df.format(arr[i]));
		}
		return rect;
	}
 
	@Override
	public void readFields(DataInput in) throws IOException 
	{
		String str[] = in.readUTF().split(",");
		for(int i = 0; i < DIMENTION; ++i)
		{
			arr[i] = Double.parseDouble(str[i]);
		}
	}
 
	@Override
	public void write(DataOutput out) throws IOException 
	{
		out.writeUTF(this.toString());
	}
}
