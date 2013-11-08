import java.io.FileWriter;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class DataSet
{
	public static void main(String[] args)
	{
		String filePath = "tokens.txt";   // input file [store pruned tweets: remove unreadable symbols and lower case all characters]
		DataInputStream dis;
		int index =1;
		try
		{
			dis = new DataInputStream(new BufferedInputStream( new FileInputStream(filePath)));
			String fileContent = new String();
			String formatContent = new String();

			/*
			 * output each tweets into an independent file (one line one tweets)
			 */
			while((fileContent = dis.readLine()) != null)
			{
					
				FileWriter fw = new FileWriter(String.valueOf(index)); 
				fw.write(fileContent);
			 	fw.close();
			 	index++;
			 	
				/*
				// This part is used for test
				if(index > 500)
				{
				    break;
				}
				*/

			}
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