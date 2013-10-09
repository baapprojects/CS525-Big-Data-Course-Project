import java.io.FileWriter;
import java.util.Random;

public class CreateDataSet 
{

	// define dataset size here
	static final int myPageSize=50000; //myPageSize=50000;
	static final int friendsSize=5000000; //friendsSize=5000000;
	static final int accessLogSize=10000000; //accessLogSize=10000000;


	//List of Country, amount 243
	private static String[] _country = new String[]{"Aaland Islands", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica", "Cote D'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea-bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territory", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City State (Holy See)", "Venezuela", "Viet Nam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Zambia", "Zimbabwe"};
	
	//List of Country, amount 218, I get the list from here: http://en.wikipedia.org/wiki/List_of_hobbies
	private static String[] _hobby = new String[]{"Amateur radio", "Audiophilia", "Bboying", "Baton twirling", "Blogging", "Carving wood animals", "caricatures", "figures", "Chainmail making", "Computer programming", "Conlanging", "Cooking", "Coloring", "Crocheting", "Creative writing", "Dance", "Drawing", "Fantasy Football", "Fishkeeping", "Foreign language learning", "Gaming ", "Genealogy", "Genetic genealogy", "Herpetoculture", "Home Movies", "Homebrewing", "Inline Skating", "Jewelry making", "Juggling", "Knapping", "Knitting", "Lapidary", "Lego Building", "Locksport", "Magic", "Musical instruments", "Origami", "Painting", "Pottery", "RC cars", "Reading", "Skateboarding", "Scrapbooking", "Sculpting", "Sewing", "Singing", "Taxidermy", "Video Gaming", "Woodworking", "Worldbuilding", "Writing", "Yoga", "Yo-yoing", "Air sports", "BASE jumping", "Bungee jumping", "Bird watching", "Board sports", "Backpacking", "Basketball", "Bonsai", "Camping", "Canoeing", "Cosplay", "Cycling", "Driving", "Foraging", "Gardening", "Geocaching", "Graffiti", "Golf", "Hunting", "Hiking", "Hooping", "Jogging", "Kayaking", "Kiteboarding", "LARPing", "Machining", "Metal detecting", "Motor sports", "Mountain biking", "Mushroom Hunting ", "Mycology", "Nordic skating", "Parkour", "Photography", "Rock climbing", "Roller skating", "Rugby", "Running", "Sailing", "Sand castle building", "Sculling or Rowing", "Skiing", "Skydiving", "Surfing", "Swimming", "Tai Chi", "Urban exploration", "Vehicle restoration", "Water sports", "Collection hobbies", "Antiquing", "Art collecting", "Book collecting", "Card collecting", "Coin collecting", "Deltiology ", "Element collecting", "Stamp collecting", "Stone collecting", "Vintage Books", "Vintage car", "Vintage clothing", "Record collecting", "Modelling", "Antiquities", "Auto audiophilia", "Flower collecting and pressing", "Fossil hunting", "Insect collecting", "Leaf collecting and pressing", "Metal detecting", "Mineral collecting", "Seaglass collecting", "Seashell collecting", "Rock stacking", "Competition hobbies", "Badminton", "Bowling", "Boxing", "Chess", "Color Guard", "Cheerleading", "Indoor Cricket", "Cubing", "Bridge", "Billiards", "Darts", "Dancing", "Fencing", "Gaming", "Go", "Gymnastics", "Martial arts", "Poker", "Programming ", "Table football", "Handball", "Weightlifting", "Lodge Dodge Pong", "Volleyball", "Airsoft", "American football", "Archery", "Association football", "Australian Football League", "Auto racing", "Baseball", "Basketball", "Climbing", "Cricket", "Cycling", "Disc golf", "Dog sport", "Exhibition Drill", "Equestrianism", "Figure skating", "Fishing", "Footbag", "Golfing", "Ice hockey", "Judo", "Jukskei", "Kart racing", "Model aircraft making and flying", "Paintball", "Radio-controlled car racing", "Racquetball", "Roller Derby", "Rugby league football", "Running", "Shooting sport", "Skateboarding", "Squash", "Speed skating", "Surfing", "Slot car racing", "Swimming", "Table tennis", "Target shooting", "Tennis", "Touch football", "Tour skating", "Volleyball", "Observation hobbies", "Audiophilia", "Microscopy", "Reading", "Shortwave listening", "Videophilia", "Outdoors", "Aircraft spotting", "Amateur astronomy", "Amateur geology", "Astrology", "Bird watching", "Herping", "Bus spotting", "College football", "Gongoozling", "Geocaching", "Meteorology", "People watching", "Trainspotting", "Travel", "International Science Olympiad"};

	//private static int _countryLength = 243;
	//private static int _hobbyLength = 218;

	

	/*
	ID: integer 1 to 50000, owner of the page
	Name: 10 to 20 char length random string
	Nationality: character sequence, we choose real country names
	CountryCode: unique integers for each country
	Hobby: random sequence of chars, we took from wikipedia
	
	*/
	
	public static void CreateMyPage() 
	{
		int _countryLength =_country.length;
		int _hobbyLength =_hobby.length;
		int ID = 0;
		String name = null;
		String nationality = null;
		int countryCode = 0;
		String hobby = null;
		int randomLength = 0;
		String lineRecordString;

		try 
		{ 
			 FileWriter fw = new FileWriter("mypage.txt"); 
			 while(ID<myPageSize)
			 {
				 ID++;
				 //random length for name (10-20)
				 randomLength=new Random().nextInt(10)+10;
				 name=getRandomString(randomLength);

				 
				 //generate random index for _country
				 countryCode=new Random().nextInt(_countryLength);
				 nationality=_country[countryCode];
				 countryCode++;
				 
				 //countryCode=new Random().nextInt(_countryLength-1)+1;

				 ////generate random index for _hobby
				 randomLength=new Random().nextInt(_hobbyLength);
				 hobby = _hobby[randomLength];
				 
				 lineRecordString=String.valueOf(ID)+","+name+","+nationality+","+String.valueOf(countryCode)+","+hobby+"\r\n";
				 fw.write(lineRecordString);  			 
			 }
			 fw.close(); 
		} 
		catch (Exception e) 
		{ 
			//Do nothing here
		} 
	}
	
	
	/*
	FriendRel: unique integer from 1 to 5,000,000
	PersonID: ID as interger from 1 to 50000
	MyFriend: Reference ID as integer from 1 to 50,000
	DateofFriendship: random integer from 1 to 1,000,000
	Desc: random string of length 20 to 50 char
	*/
	
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
			 while(friendRel<friendsSize)
			 {
				 friendRel++;
				 personID = new Random().nextInt(myPageSize)+1;
				 myFriend = new Random().nextInt(myPageSize)+1;
				 dataOfFriendship = new Random().nextInt(1000000)+1; 

				 //random length for desc (20-50)
				 randomLength=new Random().nextInt(31)+20;
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

	
	/*
	AccessID: unique integer from 1 to 10,000,000
	ByWho: reference ID of person that aceessed, integer from 1 to 50,000
	WhatPage: reference ID of person that owns the page, integer from 1 to 50,000
	TypeOfAccess: random string of length 20 to 50 characters
	AccessTime: random integer from 1 to 1,000,000
	*/
	
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
			 while(accessID<accessLogSize)
			 {
				 accessID++;
				 byWho = new Random().nextInt(myPageSize)+1;
				 whatPage = new Random().nextInt(myPageSize)+1;

				 //random length for typeOfAccess (20-50)
				 randomLength=new Random().nextInt(31)+20;
				 typeOfAccess=getRandomString(randomLength);

				 accessTime = new Random().nextInt(1000000)+1; 

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
	    String base = "abcdefghijklmnopqrstuvwxyz ";   
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
		//CreateMyPage();
		//CreateFriends();
		CreateAccessLog();

	}


}
