import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 
 * @Description: One auction just sell out one item now
 * @author hzhou@wpi.edu
 *
 */
public class Auction extends Thread
{

    private int auctionID;
    private int enrollID;
    private int itemID;
    private int itemPrice;
    private int timeStamp;
    private static FileWriter fw;
    
    Auction(int auctionID)
    {
    	this.auctionID = auctionID;
    	this.timeStamp = 1;
    	enrollID = 1;
        itemID = 1;
        
    	try 
    	{ 
    		fw = new FileWriter("strom.txt"); 
    	} 
    	catch (Exception e) {}
    }
    
    private void auctionBegin()
    {
    	try 
    	{ 
    		fw.write("auctionBegin " + this.timeStamp++ + " " + this.timeStamp++ +" auctionID " + this.auctionID + " nothing 0 nothing 0 nothing 0\r\n");
    	} 
    	catch (Exception e) {}
    }
    
    private void auctionEnrollment()
    {
    	int enrollCount = new Random().nextInt(2)+1;
    	
    	//System.out.println(enrollCount);
    	for(int i = 0; i < enrollCount; i++)
    	{
    		int begin = timeStamp;
        	int end = timeStamp + 1;
        	timeStamp = timeStamp + 2;
    		try 
        	{
        		fw.write("enrollmentBegin " + begin + " " + end +" auctionID " + this.auctionID + " enrollID " + this.enrollID + " nothing 0 nothing 0\r\n");
        		fw.write("authAttempt " + begin + " " + end +" auctionID " + this.auctionID + " enrollID " + this.enrollID + " nothing 0 nothing 0\r\n");
        		fw.write("bidderEnrolled " + begin + " " + end +" auctionID " + this.auctionID + " enrollID " + this.enrollID + " nothing 0 nothing 0\r\n");
        	} 
        	catch (Exception e) {}
    	}
    }
    
    private void auctionItemDes()
    {
    	int timeCost = new Random().nextInt(7)+10;
    	int end = timeStamp + timeCost;
    	this.itemPrice = new Random().nextInt(20);
    	try 
    	{
    		fw.write("itemDescription " + timeStamp + " " + end +" auctionID " + this.auctionID + " nothing 0 itemID  " + this.itemID + " price "+itemPrice+"\r\n");
    	} 
    	catch (Exception e) {}
    	timeStamp = end;
    }
    
    private void auctionBid()
    {
    	int hammerBeatTimes;
    	while(true)
    	{
	    	hammerBeatTimes = new Random().nextInt() % 4;
	    	try 
	    	{
	    		itemPrice += 10;
	    		fw.write("bid " + timeStamp++ + " " + timeStamp++ +" auctionID " + this.auctionID + " nothing 0 itemID  " + this.itemID + " price "+itemPrice+"\r\n");
	    	} 
	    	catch (Exception e){}
	    	
	    	for(int i = 0; i < hammerBeatTimes; i++)
	    	{
	    		try 
		    	{
		    		fw.write("hammerBeat " + timeStamp + " " + timeStamp++ +" auctionID " + this.auctionID + " nothing 0 itemID  " + this.itemID + " nothing 0\r\n");
		    	} 
		    	catch (Exception e) {}
	    	}
	    	
	    	if(hammerBeatTimes == 3)
	    	{
	    		break;

	    	}
    	}
    }

    private void auctionSellAndEnd()
    {
    	try 
    	{
    		fw.write("sell " + this.timeStamp++ + " " + this.timeStamp++ +" auctionID " + this.auctionID + " nothing 0 itemID "+ this.itemID +" nothing 0\r\n");
    		fw.write("auctionEnd " + this.timeStamp++ + " " + this.timeStamp++ +" auctionID " + this.auctionID + " nothing 0 nothing 0 nothing 0\r\n");
	    } 
		catch (Exception e) 
		{
			// do nothing here
		}
    }
    
    public void run() 
    {
    	auctionBegin();
    	auctionEnrollment();
    	auctionItemDes();
    	auctionBid();
    	auctionSellAndEnd();
    }
    
    public static void close()
    {
    	try 
    	{ 
    		 System.out.println("File is closed here!");
    		 fw.close(); 
    	} 
    	catch (Exception e) {}
    }
    public static void main(String[] args) throws InterruptedException 
    {
    	List<Auction> a = new ArrayList<Auction>();
    	
    	for(int i = 0; i < 5; i++)
    	{
    		Auction auc = new Auction(i+1);
            a.add(auc);
    	}
    	
    	for(Auction auc: a)
    	{
    		  auc.start();
    	}
    	
    	for(Auction auc: a)
    	{
    		  auc.join();
    	}
    	//Auction daemonThread = new Auction(0);
    	//daemonThread.setDaemon(true);
    	//daemonThread.start();
    	
    	// flush and close file
    	Auction.close(); 
    }
}
