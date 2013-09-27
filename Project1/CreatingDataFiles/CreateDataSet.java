import java.io.FileWriter;
import java.util.Random;

public class CreateDataSet 
{
	public static void CreateMyPage() 
	{
		int ID = 0;
		String name = null;
		String nationality = null;
		int coutryCode = 0;
		String hobby = null;

		int randomLength = 0;
		String lineRecordString;

		try 
		{ 
			 FileWriter fw = new FileWriter("mypage.txt"); 
			 while(ID<50000)
			 {
				 ID++;
				 //random length for name (10-20)
				 randomLength=new Random().nextInt(10)+10;
				 name=getRandomString(randomLength);
				 //random length for nationality (10-20)
				 randomLength=new Random().nextInt(10)+10;
				 nationality=getRandomString(randomLength);

				 coutryCode=new Random().nextInt(9)+1;
				 //random length for hobby (10-20)
				 randomLength=new Random().nextInt(10)+10;
				 hobby=getRandomString(randomLength);
				 
				 lineRecordString=String.valueOf(ID)+","+name+","+nationality+","+String.valueOf(coutryCode)+","+hobby+"\r\n";
				 fw.write(lineRecordString);  			 
			 }
			 fw.close(); 
		} 
		catch (Exception e) 
		{ 
			//Do nothing here
		} 
	}
	
	public static void CreateFriends() 
	{
		int friendRel = 0;
		int personID = 0;
		int myFriend = 0;
		int dataOfFriendship = 0;
		String desc = null;

		int randomLength = 0;
		String lineRecordString;

		try 
		{ 
			 FileWriter fw = new FileWriter("friends.txt"); 
			 while(friendRel<5000000)
			 {
				 friendRel++;
				 personID = new Random().nextInt(49999)+1;
				 myFriend = new Random().nextInt(49999)+1;
				 dataOfFriendship = new Random().nextInt(999999)+1; 

				 //random length for desc (20-50)
				 randomLength=new Random().nextInt(30)+20;
				 desc=getRandomString(randomLength);
				 
				 lineRecordString=String.valueOf(friendRel)+","+String.valueOf(personID)+","+String.valueOf(myFriend)+","+String.valueOf(dataOfFriendship)+","+desc+"\r\n";
				 fw.write(lineRecordString);  			 
			 }
			 fw.close(); 
		} 
		catch (Exception e) 
		{ 
			//Do nothing here
		} 
	}

	public static void CreateAccessLog() 
	{
		int accessID = 0;
		int byWho = 0;
		int whatPage = 0;
		String typeOfAccess = null;
		int accessTime = 0;

		int randomLength = 0;
		String lineRecordString;

		try 
		{ 
			 FileWriter fw = new FileWriter("accesslog.txt"); 
			 while(accessID<5000000)
			 {
				 accessID++;
				 byWho = new Random().nextInt(49999)+1;
				 whatPage = new Random().nextInt(49999)+1;

				 //random length for typeOfAccess (20-50)
				 randomLength=new Random().nextInt(30)+20;
				 typeOfAccess=getRandomString(randomLength);

				 accessTime = new Random().nextInt(999999)+1; 

				 lineRecordString=String.valueOf(accessID)+","+String.valueOf(byWho)+","+String.valueOf(whatPage)+","+typeOfAccess+","+String.valueOf(accessTime)+"\r\n";
				 fw.write(lineRecordString);  			 
			 }
			 fw.close(); 
		} 
		catch (Exception e) 
		{ 
			//Do nothing here
		} 
	}

	//Generate a random string with defined length
	public static String getRandomString(int length) 
	{ 
	    String base = "abcdefghijklmnopqrstuvwxyz";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) 
	    {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		CreateMyPage();
		CreateFriends();
		CreateAccessLog();
	}


}
