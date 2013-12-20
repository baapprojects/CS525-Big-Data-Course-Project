###Issues we faced:
1. The code works well on Local Mode, but very unstably on distributed Storm. We first thought it may be induced by input file. In this case, we tried many ways of file inputing:
	1. Use hardcode input file path: `reader = new FileReader("/home/sup1/Desktop/stream1.txt");` [Working on Local Mode only]        
	2. Use program argument string as input file path: `reader = new FileReader(conf.get("inputFile").toString());` [Working on Local Mode only]     
	3. Use Internet input file [Working on Local Mode only]:   

			url  = new URL("http://web.cs.wpi.edu/~hzhou/stream1.txt" );
			urlConn = url.openConnection();
			inStream = new InputStreamReader(urlConn.getInputStream());
	4. The only working ustable way is that we hardcode the input file as an string array inside, which doesn't always work.    
	5. We are still trying our best to fix this problem...
	
###How to generate input test data set:
We use our program to deal with `Auction` processing. Its flow is as the image below. The problem is that our system will deal with multiple autions at the sam e time. So, when generaing the dataset, I used multi-threads, and each thread represents an auction.     
![](doc.jpg)