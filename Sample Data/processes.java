import java.util.Arrays;
import java.io.*;
import java.util.*;
import java.util.Arrays;

//For writting to a file
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class processes
{
	//if x is process number
	//x,0--> Arrival //x,1--> burst
	public static int[][] arivalBurst;

	public static void main( String args[] )
	{
		processes();
	}

	public static void runsjf(int runFor, int processCount)
	{


		//Sorted arival, select and finished times
		// [P_T][P] P_T: row:  0 = process # P, 1 = time for process P
		// [P_T][P] P: column: time for process 'P'
		// next sorted process 	[0][0]
		// next sorted time 	[1][0]
		Integer[][] burstSortedT = new Integer[2][processCount];
		Integer[][] arivedSortedT = new Integer[2][processCount];
		//Integer[] arrivedProcesses = new Integer[processCount]

		//Create an empty array list with an initial capacity
   		ArrayList<Integer> arrivedProcesses = new ArrayList<Integer>(processCount);
   		//arrlist.add(20); list will contain arrived processes
   		//arrlist.contains(30);	return true or false if it contains it

		int nowTime = 0; int process = 0;


		// use 1 for burst, see method declaration
		sortTimeWithP(burstSortedT, processCount, 1);
		// use 0 for burst, see method declaration
		sortTimeWithP(arivedSortedT, processCount, 0);

		//Print sorted burst/arrival times 	DEBUG
		System.out.println("Burst:"+Arrays.deepToString(burstSortedT));
		System.out.println("Arrived:"+Arrays.deepToString(arivedSortedT));

		try {

			File file = new File("myTestFile.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			int currentBurstT = 0; int currentP = 0;
			int newBurstT = 0;
			int prevSelectT = 0;
			int prevSelectP = 1;

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			///////////////////////////////////////////////////////
			// 	Output to file here
			///////////////////////////////////////////////////////
			for(int t = nowTime; t <= runFor; t++)
			{
				//Keep track of all arrived processes
				process = pThatArrivedAtThisT(arivedSortedT, t, processCount);

				if(process != 0)	//A process has arrived
				{
					arrivedProcesses.add(process);
					printArrived(bw, t, process);

					//Get burst time for recently arrived process
					if( burstTimeForP(burstSortedT, process, processCount) != 0)
						currentBurstT = burstTimeForP(burstSortedT, process, processCount);

					//Check if this is the lowest burst and is worthy of being selected
					//If we get process = 0 this just won't go. That shouldn't happen though
					if(process == shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount) )
					{
						printSelected(bw, t, process, currentBurstT);
						updateBTime(burstSortedT, currentBurstT, prevSelectP, processCount, t, prevSelectT);
						prevSelectP = process;
						prevSelectT = t;

						//DEBUG watch out for negatives
						currentBurstT = burstTimeForP(burstSortedT, process, processCount);

						//Check if we can finish running this process
						if( weCanFinishThis(arivedSortedT, processCount, process, t, currentBurstT) )
						{
							printFinished(bw, t+currentBurstT, process);
							updateBTime(burstSortedT, currentBurstT, prevSelectP, processCount, t+currentBurstT, prevSelectT);
							t = t+currentBurstT; //Update time 
						}

					} //There is another arrived process with a shorter burst
					else
					{	//DEBUG check my usage of tempP if bug pops up
						int tempP = shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
						printSelected(bw, t, tempP, currentBurstT);
						updateBTime(burstSortedT, currentBurstT, prevSelectP, processCount, t, prevSelectT);
						prevSelectP = process;
						prevSelectT = t;
					}

					//Print sorted burst/arrival times 	DEBUG
					System.out.println("Burst:"+Arrays.deepToString(burstSortedT));
					System.out.println("Arrived:"+Arrays.deepToString(arivedSortedT));

					//Check if all Ps arrived And at least one still has burst
					//If so sequentially print processes with shortest burst untill all are finished
					int greatestBurst = greatestP_BFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
					//System.out.println("We bout to do something! sb_"+greatestBurst); //DEBUG
					if(allPsHaveArrived(arrivedProcesses, processCount) && greatestBurst > 0)
					{
						while(greatestBurst > 0)
						{
							greatestBurst = greatestP_BFromArrivedPs(arrivedProcesses, burstSortedT, processCount);

							//System.out.println("We bout to do something! sb_"+greatestBurst); //DEBUG
							int shortestBurst = shortestP_BFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
							process = greatestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);

							currentBurstT = burstTimeForP(burstSortedT, process, processCount);

							//newBurstT = burstTimeForP(myArray, process, processCount) - (t - prevSelectT); DEBUG
							//prevSelectT is messed up
							printSelected(bw, t, process, currentBurstT);
							updateBTimeToZero(burstSortedT, process, processCount);
							printFinished(bw, t+currentBurstT, process);
							t = t+currentBurstT;

							greatestBurst = greatestP_BFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
						}

						while(t < runFor )
						{
							bw.write("Time "+t+": IDLE\n");
							t++;
						}

						bw.write("Finished at time "+runFor);
/*
						//Print sorted burst/arrival times 	DEBUG
						System.out.println("Recursive _t"+t+" p_"+process);
						System.out.println("Burst:"+Arrays.deepToString(burstSortedT));
						System.out.println("Arrived:"+Arrays.deepToString(arivedSortedT));
*/
					}

				}
				
			}

			//////////////////////////////////////////////////////
			// 	End output to file
			///////////////////////////////////////////////////////
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		//Print Desired Output to text file
		//printToFileFCFS(arivedSortedT, selectedSortedT, finishedSortedT, sortedTimes, runFor, processCount);

	}

	public static boolean allPsHaveArrived(ArrayList<Integer> arrivedProcesses, int processCount)
	{
		if(arrivedProcesses.size() == processCount)
		{
			System.out.println("All Processes have arrived"); //DEBUG
			return true;
		}
		return false;
	}

	public static boolean weCanFinishThis( Integer[][] myArray, int processCount, int process, int t, int currentBurstT)
	{
		//Once selected, check if you can finish by 
		//seeing if there is an arrival before your burst deminishes
		int remainingBurst = currentBurstT;

		//If nothing arrives between now and the end of your burst we can finish
		for(int i = 0; i < processCount; i++)
		{
			if( (myArray[1][i] > t) && (myArray[1][i] < (remainingBurst + t)) )
			{
				System.out.println("a_"+myArray[1][i]+" r_"+remainingBurst+" t_"+t);
				return false;
			}
		}
		return true;
	}

	public static void updateBTime( Integer[][] myArray, int currentBurstT, int  process, int  processCount, int t, int prevSelectT)
	{
		int newBurstT = burstTimeForP(myArray, process, processCount) - (t - prevSelectT);
		/* DEBUG I might need this later
		if(newBurstT < 0)
			return newBurstT; 
			*/

		for( int i = 0; i < processCount; i++)
		{
			if(process == myArray[0][i])
				myArray[1][i] = newBurstT;
		}
	}

	public static void updateBTimeToZero( Integer[][] myArray, int  process, int  processCount)
	{

		for( int i = 0; i < processCount; i++)
		{
			if(process == myArray[0][i])
				myArray[1][i] = 0;
		}
	}

	//Process with shortest burst from arrived burst
	public static int shortestB_PFromArrivedPs(ArrayList<Integer> arrivedProcesses, Integer[][] myArray, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if(arrivedProcesses.contains(myArray[0][i]))
				return myArray[0][i];
		}
		return 0;
	}

	//Shortest Bursts arrived burst
	public static int shortestP_BFromArrivedPs(ArrayList<Integer> arrivedProcesses, Integer[][] myArray, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if(arrivedProcesses.contains(myArray[0][i]))
				return myArray[1][i];
		}
		return 0;
	}
	//Greatest Bursts arrived from arrived processes
	public static int greatestP_BFromArrivedPs(ArrayList<Integer> arrivedProcesses, Integer[][] myArray, int processCount)
	{
		for(int i = processCount - 1; i > 0; i--)
		{
			if(arrivedProcesses.contains(myArray[0][i]))
				return myArray[1][i];
		}
		return 0;
	}
	//Process with greatest Bursts from arrived Processes
	public static int greatestB_PFromArrivedPs(ArrayList<Integer> arrivedProcesses, Integer[][] myArray, int processCount)
	{
		for(int i = processCount - 1; i > 0; i--)
		{
			if(arrivedProcesses.contains(myArray[0][i]))
				return myArray[0][i];
		}
		return 0;
	}

	public static int pThatArrivedAtThisT(Integer[][] myArray, int t, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if( myArray[1][i] == t )
				return myArray[0][i];
		}
		return 0;
	}

	public static int burstTimeForP(Integer[][] myArray, int p, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if( myArray[0][i] == p )
				return myArray[1][i];
		}
		return 0;
	}

	///////////////////////////////////////////////////////////////////////
	//Sort double array A[P][T], y = Time associated with process P
	//Sorted according to T
	//myArray = end product sorted, 	dataArray = data we are sorting
	///////////////////////////////////////////////////////////////////////
	public static void sortTimeWithP(Integer[][] myArray, Integer[][] dataArray, int processCount)
	{
		for(int p =1; p <= processCount; p++ )
		{
			myArray[0][p-1] = p;				  //process
			myArray[1][p-1] = dataArray[p][1]; //time for that process
		}

		bubbleSort(myArray);
	}
	//Method overloading: used if sorting arivalBurst: 
	// Use ArrivalOrBurst = 0 (arrival); ArrivalOrBurst = 1 (burst)
	public static void sortTimeWithP(Integer[][] myArray, int processCount, int ArrivalOrBurst)
	{
		//////////arivalBurst/////////////////////
		//if x is process number
		//x,0--> Arrival //x,1--> burst
		///////////////////////////////////////
		if( ArrivalOrBurst == 1)
		{
			for(int p =1; p <= processCount; p++ )
			{
				myArray[0][p-1] = p;				  //process
				myArray[1][p-1] = arivalBurst[p][1]; //burst for that process
			}
		}
		else if( ArrivalOrBurst ==  0)
		{
			for(int p =1; p <= processCount; p++ )
			{
				myArray[0][p-1] = p;				  //process
				myArray[1][p-1] = arivalBurst[p][0]; //arrival for that process
			}
		}

		bubbleSort(myArray);
	}

	public static void runfcfs(int runFor, int processCount)
	{
		int timeData = processCount*3;
		int arrival = 0;	int burst = 0;
		int selected = 0;
		int finished = 0;
		int prevSelected = 0;
		int prevFinished = 0;

		//timeList[x][y] x --> process y.0 arrival, y.1 selected, y.2 finished
		Integer[][] timeList = new Integer[processCount+1][3];
		//Sorted timeList times
		Integer[] sortedTimes = new Integer[processCount*3];

		//Sorted arival, select and finished times
		// [X][P] row:  0 = process # P, 1 = time for process P
		// [P][P] column: time for process 'P'
		Integer[][] arivedSortedT = new Integer[2][processCount];
		Integer[][] selectedSortedT = new Integer[2][processCount];
		Integer[][] finishedSortedT = new Integer[2][processCount];

		///////////////////////////////////////////
		//Put data in timelist array
		//(arrived,selected, finished) --> timeList
		//timeList[x][y] y.0 arrival,y.1 selected,y.2 finished
		///////////////////////////////////////////
		for( int p = 1; p <= processCount; p++)
		{
			// for p
			//arrival = arivalBurst[p][0];
			timeList[p][0] = arivalBurst[p][0];
			burst = arivalBurst[p][1];

			//selected = prevFinished;
			timeList[p][1] = prevFinished;

			//finished = prevFinished + burst;
			timeList[p][2] = prevFinished + burst;

			prevFinished = timeList[p][2];
		}

		int k = 0;

		//put times in single array
		for(int p =1; p <= processCount; p++ )
		{
			for(int j = 0; j < 3; j++)
			{
				sortedTimes[k] = timeList[p][j];

				k = k+1;
			}
			arivedSortedT[0][p-1] = p;				//process
			arivedSortedT[1][p-1] = timeList[p][0]; //time for that process
			selectedSortedT[0][p-1] = p;
			selectedSortedT[1][p-1] = timeList[p][1];
			finishedSortedT[0][p-1] = p;
			finishedSortedT[1][p-1] = timeList[p][2];
		}
		//Sort double arrays
		bubbleSort(arivedSortedT);
		bubbleSort(selectedSortedT);
		bubbleSort(finishedSortedT);

		//Sort single array
		Arrays.sort(sortedTimes);

		//Print Desired Output to text file
		printToFileFCFS(arivedSortedT, selectedSortedT, finishedSortedT, sortedTimes, runFor, processCount);
	}

	public static void printToFileFCFS(Integer[][] arivedSortedT, Integer[][] selectedSortedT, Integer[][] finishedSortedT, Integer[] sortedTimes, int runFor, int processCount)
	{
		try {

			File file = new File("myTestFile.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			///////////////////////////////////////////////////////
			// 	Output to file here
			///////////////////////////////////////////////////////
			//Using arivedSortedT, selectedSortedT, finishedSortedT


			//My algorithm 
			/*
			1.	P1 arrived; print P1
			2.	Print all that arrive before (P1-1) is finished
			3.	print (P1-1) finished
			4.	print P1 selected
			5.	print all that arrive before P1 Finished
			6.	print P1 finished
			7.	go to next arrived in the queue and start over
			*/
			
			int tA = 0, tS = 0, tF = 0;
			int pA = 0, pS = 0, pF = 0;
			int prevFinishedT = 0; int prevFinishedP = 0;


			printHeaderFCFS(processCount, bw);

			/* DEBUG
			System.out.println(Arrays.deepToString(arivedSortedT));
			System.out.println(Arrays.deepToString(selectedSortedT));
			System.out.println(Arrays.deepToString(finishedSortedT));
			*/
			////////////////////////////////////////////////////////
			//Print all that have arrived been selected or finished
			///////////////////////////////////////////////////////
			while(pF != 999 )	//999 is my error code ("I still think in c")
			{
				//1.	P1 arrived; print P1 arrived
				pA = findNextValidP(arivedSortedT);
				if(pA != 999)	//If we still have arrived items to sort out
				{
					tA = getTimeForP(arivedSortedT, pA);
					printArrived(bw, tA, pA);
					invalidateItem(arivedSortedT);
					//2.	Print all that arrive before (P1-1) is finished
					pA = findNextValidP(arivedSortedT);
					tA = getTimeForP(arivedSortedT, pA);
					while( prevFinishedT > tA)
					{
						printArrived(bw, tA, pA);
						invalidateItem(arivedSortedT);
						pA = findNextValidP(arivedSortedT);
						tA = getTimeForP(arivedSortedT, pA);			
					}
					//3.	print (P1-1) finished (still have to invalidate later)
					if(prevFinishedP > 0)
					{
						printFinished(bw, prevFinishedT, prevFinishedP);
					}
				}
				//4.	print P1 selected
				pS = findNextValidP(selectedSortedT);
				if( pS != 999 )
				{
					tS = getTimeForP(selectedSortedT, pS);
					printSelected(bw, tS, pS, arivalBurst[pS][1]);
					invalidateItem(selectedSortedT);
			
					//5.	print all that arrive before P1 Finished
					pA = findNextValidP(arivedSortedT);
					if( pA != 999 )
					{
						tA = getTimeForP(arivedSortedT, pA);
						pF = findNextValidP(finishedSortedT);
						tF = getTimeForP(finishedSortedT, pA);
						while(tA < tF)
						{
							printArrived(bw, tA, pA);
							invalidateItem(arivedSortedT);
							pA = findNextValidP(arivedSortedT);
							if(pA == 999 ) //all items in array have been used
							{
								break;
							}
							tA = getTimeForP(arivedSortedT, pA);
							pF = findNextValidP(finishedSortedT);
							tF = getTimeForP(finishedSortedT, pA);
						}
					}
				}
				//6.	print P1 finished
				pF = findNextValidP(finishedSortedT);
				if( pF != 999 )
				{
					tF = getTimeForP(finishedSortedT, pF);
					
					printFinished(bw, tF, pF);
					invalidateItem(finishedSortedT);
					prevFinishedT = tF;
					prevFinishedP = pF;
				}
				else
				{
					pF = 999;	//probably not necessary
					break;
				}
			}// end of print a s f while loop

			//Print the time when everything finished
			printFinishedAt(bw, finishedSortedT);

			int wait = 0;
			int turnaround = 0;
			int p = 0;

			//Print wait and turnaround times
			for(int i = 0; i < arivedSortedT[0].length; i++)
			{
				p = i;
				wait = selectedSortedT[1][p] - arivedSortedT[1][p];
				turnaround = finishedSortedT[1][p] - arivedSortedT[1][p];
				printWaitTurnaroundT(bw, p+1, wait, turnaround);
			}
			//////////////////////////////////////////////////////
			// 	End output to file
			///////////////////////////////////////////////////////
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}// end of file write method

	public static void printArrived(BufferedWriter bw, int time, int process)
	{
		try {
			bw.write("Time "+time+": P"+process+" arrived\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printSelected(BufferedWriter bw, int time, int process, int burst)
	{
		try {
			bw.write("Time "+time+": P"+process+" selected (burst "+burst+")\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printFinished(BufferedWriter bw, int time, int process)
	{
		try {
			bw.write("Time "+time+": P"+process+" finished\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printFinishedAt(BufferedWriter bw, Integer[][] myArray)
	{
		try {
			bw.write("Finished at time "+myArray[1][myArray[0].length-1]+"\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printWaitTurnaroundT(BufferedWriter bw, int p, int wait, int turnaround)
	{
		try {
			bw.write("P"+p+" wait "+wait+" turnaround "+turnaround+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printHeaderFCFS(int processCount, BufferedWriter bw)
	{
		try {
			bw.write(processCount+" processes\n");
			bw.write("Using First Come First Served\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printArrivedPsBeforePFinishedFCFS(Integer[][] myArray)
	{
		//Do stuff
		int i = 0;
	}

	public static int findNextValidP(Integer[][] myArray )
	{
		int currentProcess = 999; ///999 is invalid
		int P = 0;
		while( currentProcess == 999 )
		{
			P++;
			if(P > myArray[0].length)
			{
				return 999; // this means everything is invalid
			}
			currentProcess = myArray[0][P-1];
		}
		return currentProcess;
	}

	public static void invalidateItem(Integer[][] myArray)
	{
		int currentProcess = 999; ///999 is invalid
		int P = 0;
		//Find next valid item
		while( currentProcess == 999 )
		{
			P++;
			currentProcess = myArray[0][P-1];
		}

		//Invalidate that item
		myArray[0][currentProcess-1] = 999;
	}

	public static int getTimeForP(Integer[][] myArray, int P)
	{
		return myArray[1][P-1];
	}

			/*DEBUG
			bw.write(Arrays.deepToString(arivedSortedT));
			bw.write(Arrays.deepToString(selectedSortedT));
			bw.write(Arrays.deepToString(finishedSortedT));
			bw.write("AllSortTs("+Arrays.toString(sortedTimes)+")");
			*/
			/* DEBUG how to write to a file
			bw.write(processCount+" processes\n");
			bw.write("Using First Come First Served\n\n");
			int P=1;
			bw.write("Time "+arivedSortedT[1][P-1]+": P"+arivedSortedT[0][P-1]+" arrived\n");
			bw.write("Time "+selectedSortedT[1][P-1]+": P"+selectedSortedT[0][P-1]+" selected (burst "+arivalBurst[P][0]+")");
			*/
			//If P2 arrives before P1 is finished then print it



	public static void processes()
	{
       try 
       {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter the file name with extention : ");
            File file = new File(input.nextLine());

            input = new Scanner(file);

            int processCount = 0;	int numProcess = 0;	int runFor = 0;	int quantum = 0;
            String use = new String("");	String numProcessStr = new String("");	            
            int arrival = 'A';	int burst = 'B';
            char numChar = 'x';

	        while (input.hasNextLine()) 
	        {
	            String line = input.nextLine();
	            String[] values = line.split(" ");

	            for(int i = 0; i < values.length; i++)
	            {
	            	if(values[i].equals("#"))
	            		break;
	            	if(values[i].equals("processcount"))
	            	{
	              		//if x is process number
	            		//x,0--> Arrival //x,1--> burst
	            		processCount = Integer.parseInt( values[i+1] );		            		
	            		arivalBurst = new int[processCount+1][2]; 	//starts at index 1
	            	}												//Declared as global variable
	            	if(values[i].equals("runfor"))
	            		runFor = Integer.parseInt( values[i+1] );
					if(values[i].equals("use"))
					{	
	            		use = values[i+1];
					}
	            	if(values[i].equals("quantum"))
	            		quantum = Integer.parseInt( values[i+1] );
	            	if(values[i].equals("name"))
	            	{
	            		//get process number as int
	            		//numProcessStr = values[i+1]///numChar = numProcessStr.charAt(1);//
	            		processCount = Character.getNumericValue(numChar);

	            		//Number of Process element
	            		processCount = Character.getNumericValue( values[i+1].charAt(1) );
	            		//Arrival
	            		arrival = Integer.valueOf(values[i+3] );
	            		//burst
	            		burst = Integer.valueOf( values[i+5] );

	            		arivalBurst[processCount][0] = arrival;		//starts at index 1
	            		arivalBurst[processCount][1] = burst; 		//starts at index 1          		
	            	}	                		               			               
	            }
	  
	        } //End of while loop

	        //DEBUG OUTPUT
	        /*
            System.out.println("p_"+processCount+" r_"+runFor+" u_"+use);
            System.out.println(" q_"+quantum+" proCount_"+processCount);
            System.out.println(" arrival_"+arrival+" burst_"+burst);
            System.out.println(Arrays.deepToString(arivalBurst));
            */
            input.close();

            ///////////////////////////
            //process information here
            ///////////////////////////

            //Choose Job logic
            if(use.equals("fcfs"))
            {
            	System.out.println("use fcfs!!!");	//DEBUG
            	runfcfs(runFor, processCount);
            }
            if(use.equals("sjf"))
            {
            	System.out.println("use sjf!!!");	//DEBUG
            	runsjf( runFor, processCount);
            }

        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }

	}

	private static void bubbleSort(Integer[][] intArray) 
	{

    int n = intArray.length;
	    int temp = 0;
	    int tempP = 0;
	   
	    for(int i=0; i < n; i++){
	            for(int j=1; j < (n-i); j++){
	                   
	                    if(intArray[1][j-1] > intArray[1][j]){
	                            //swap the elements!
	                            temp = intArray[1][j-1];
	                            intArray[1][j-1] = intArray[1][j];
	                            intArray[1][j] = temp;

	                            //now swap the p values
	                            tempP = intArray[0][j-1];
	                            intArray[0][j-1] = intArray[0][j];
	                            intArray[0][j] = tempP;
	                    }
	                   
	            }
	    }
	}

}

		/* DEBUG LINE 81
		//print Arrays before sort
		System.out.println("Arrays before sort arrived,Selected,Finished");
		System.out.println(Arrays.deepToString(arivedSortedT));
		System.out.println(Arrays.deepToString(selectedSortedT));
		System.out.println(Arrays.deepToString(finishedSortedT));
		//Sort double arrays
		bubbleSort(arivedSortedT);
		bubbleSort(selectedSortedT);
		bubbleSort(finishedSortedT);
		//print arrays after sort
		System.out.println("Arrays after sort arrived,Selected,Finished");
		System.out.println(Arrays.deepToString(arivedSortedT));
		System.out.println(Arrays.deepToString(selectedSortedT));
		System.out.println(Arrays.deepToString(finishedSortedT));
		System.out.println("AllSortTs("+Arrays.toString(sortedTimes)+")");
		*/

/*DEBUG
            System.out.println("p_"+processCount+" r_"+runFor+" u_"+use);
            System.out.println(" q_"+quantum+" proCount_"+processCount);
            System.out.println(" arrival_"+arrival+" burst_"+burst);
            System.out.println(Arrays.deepToString(arivalBurst));
*/


/* generic print to file
	public static void printToFile(Integer[][] arivedSortedT, Integer[][] selectedSortedT, Integer[][] finishedSortedT, Integer[] sortedTimes)
	{
		try {

			File file = new File("myTestFile.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Wasaaa");
			///////////////////////////////////////////////////////
			// 	Output to file here
			///////////////////////////////////////////////////////

			//////////////////////////////////////////////////////
			// 	End output to file
			///////////////////////////////////////////////////////
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}// end of file write method

*/

	/*
				int[][] ia = new int[5][6];
			System.out.println(ia.length);
			System.out.println(ia[0].length);
	*/