import java.io.FileWriter;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class IOExample
{
        public static void main(String[] args)
        {
                String filePath = "tokens2.txt";
                DataInputStream dis;
                int index =1;
                try
                {
                        dis = new DataInputStream(new BufferedInputStream( new FileInputStream(filePath)));
                        String fileContent = new String();
                        String formatContent = new String();
                        while((fileContent = dis.readLine()) != null)
                        {
                        		
                                FileWriter fw = new FileWriter(String.valueOf(index)); 
								fw.write(fileContent);                       	    
                         		fw.close(); 
                         		index++;
                                if(index > 500)
                                {
                                    break;
                                }

                        }
                        //System.out.println(formatContent);
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