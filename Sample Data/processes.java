import java.util.Arrays;
import java.io.*;
import java.util.*;
import java.util.Arrays;

//For writting to a file
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

////////////////////////////////////////////////////////////
/* METHODS
javap processes

  public static int[][] arivalBurst;
  public processes();
  public static void main(java.lang.String[]);
  public static void runsjf(int, int);
  public static boolean weFinishedPrevSelectedP(java.lang.Integer[][], int, int);
  public static boolean nextIsSelect(java.util.ArrayList<java.lang.Integer>, java.lang.Integer[][], int, int);
  public static void debug_pSelected(int, int, int, java.lang.Integer[][], java.lang.Integer[][], java.lang.Integer[][], java.lang.Integer[][], int, int, int);
  public static int pSelected(java.io.BufferedWriter, int, int, int, java.lang.Integer[], java.lang.Integer[][], java.lang.Integer[][], java.lang.Integer[][], java.lang.Integer[][], int, int, int);
  public static void insertFirstSelectedData(java.lang.Integer[][], int, int, int);
  public static void insertFinishedData(java.lang.Integer[][], int, int, int);
  public static boolean isPFinished(java.lang.Integer[][], int, int);
  public static void initSelectedData(java.lang.Integer[][], int);
  public static void initFinishedData(java.lang.Integer[][], int);
  public static boolean allPsHaveArrived(java.util.ArrayList<java.lang.Integer>, int);
  public static boolean weCanFinishThis(java.lang.Integer[][], int, int, int, int);
  public static void updateBTime(java.lang.Integer[][], int, int, int, int);
  public static void updateBTimeToZero(java.lang.Integer[][], int, int);
  public static int shortestB_PFromArrivedPs(java.util.ArrayList<java.lang.Integer>, java.lang.Integer[][], int);
  public static int shortestP_BFromArrivedPs(java.util.ArrayList<java.lang.Integer>, java.lang.Integer[][], int);
  public static int greatestP_BFromArrivedPs(java.util.ArrayList<java.lang.Integer>, java.lang.Integer[][], int);
  public static int greatestB_PFromArrivedPs(java.util.ArrayList<java.lang.Integer>, java.lang.Integer[][], int);
  public static int pThatArrivedAtThisT(java.lang.Integer[][], int, int);
  public static int burstTimeForP(java.lang.Integer[][], int, int);
  public static void sortTimeWithP(java.lang.Integer[][], java.lang.Integer[][], int);
  public static void sortTimeWithP(java.lang.Integer[][], int, int);
  public static void runfcfs(int, int);
  public static void printToFileFCFS(java.lang.Integer[][], java.lang.Integer[][], java.lang.Integer[][], java.lang.Integer[], int, int);
  public static void printArrived(java.io.BufferedWriter, int, int);
  public static void printSelected(java.io.BufferedWriter, int, int, int);
  public static void printFinished(java.io.BufferedWriter, int, int);
  public static void printFinishedAt(java.io.BufferedWriter, java.lang.Integer[][]);
  public static void printWaitTurnaroundT(java.io.BufferedWriter, int, int, int);
  public static void printHeaderFCFS(int, java.io.BufferedWriter);
  public static void printHeaderSJF(int, java.io.BufferedWriter);
  public static void printArrivedPsBeforePFinishedFCFS(java.lang.Integer[][]);
  public static int findNextValidP(java.lang.Integer[][]);
  public static void invalidateItem(java.lang.Integer[][]);
  public static int getTimeForP(java.lang.Integer[][], int);
  public static void processes();

*/
////////////////////////////////////////////////////////////


public class processes
{
	//if x is process number
	//x,0--> Arrival //x,1--> burst
	public static int[][] arivalBurst;

		public static int lastSelectedP;
		public static int lastSelectedT;
		public static int rrIndex;

	public static void main( String args[] )
	{
		processes();
	}

	public static void runrr(int runFor, int processCount, int q)
	{
		Integer[][] arivedSortedT = new Integer[2][processCount];
		Integer[][] burstSortedT = new Integer[2][processCount];
		Integer[][] burstSortedTQ = new Integer[2][processCount];
		Integer[][] firstSelected = new Integer[2][processCount];
		Integer[][] finished = new Integer[2][processCount];
		//Integer[] arrivedProcesses = new Integer[processCount]

		//I'm certain there's a better way to do this but me in rush
		initSelectedData(firstSelected, processCount);
		initFinishedData(finished, processCount);

		//Create an empty array list with an initial capacity
   		ArrayList<Integer> arrivedProcesses = new ArrayList<Integer>(processCount);
   		ArrayList<Integer> selectedProcesses = new ArrayList<Integer>(processCount);
   		ArrayList<Integer> finishedProcesses = new ArrayList<Integer>(processCount);
  		//arrlist.add(20); list will contain arrived processes
   		//arrlist.contains(30);	return true or false if it contains it
   		//Create a hash table map the index to processes
   		Hashtable<Integer, Integer> rrIndexHash = new Hashtable<Integer, Integer>();
   		Hashtable<Integer, Integer> rrIndexHashIP = new Hashtable<Integer, Integer>();


   		//insertFirstSelectedData

   		//Do not sort these again 
		// use 0 for Arrived, see method declaration
		sortTimeWithP(arivedSortedT, processCount, 0);
		bubbleSortFinal(arivedSortedT);
		// use 1 for burst, see method declaration 
		sortTimeWithP(burstSortedTQ, processCount, 1);	//DEBUG dont think i need this but i could be wrong
		sortBurstParalelWitArrived(burstSortedTQ, arivedSortedT, processCount);

		//Map process to index
		for(int i = 0; i < arivedSortedT.length; i++)
		{
			rrIndexHash.put(arivedSortedT[0][i], i);
		}
		//Map index to process
		for(int i = 0; i < arivedSortedT.length; i++)
		{
			rrIndexHashIP.put(i, arivedSortedT[0][i]);
		}

		// arivedSortedT[0][i] will give us the key from the index

		//DEBUG
		//System.out.println("BurstSorted"+Arrays.deepToString(burstSortedTQ));	//DEBUG		
		//System.out.println("ArrivedSorted"+Arrays.deepToString(arivedSortedT));	//DEBUG		

		//Hard read variables
		//q = 4;			//DEBUG
				//DEBUG need P and burst time
				//System.out.println("BurstSorted"+Arrays.deepToString(burstSortedTQ));
				//System.out.println("arivedSortedT"+Arrays.deepToString(arivedSortedT));				//DEBUG
				///printHash(rrIndexHash);	//DEBUG
				//System.out.println(" P_"+process+" BT_"+burstTime+" rrIndex_"+rrIndex);				//DEBUG

					//DEBUG
					//System.out.println("BurstTimeArray"+Arrays.deepToString(burstSortedTQ));	//DEBUG
					//System.out.println(" P_"+process+" t_"+t+" time_"+time+" q_"+q+" lsp_"+lastSelectedP+" lst_"+lastSelectedT);	//DEBUG
					//System.out.println(" nb1_"+nb1);					//DEBUG
					//System.out.println(" nb2_"+nb2);	//DEBUG	

		int time = 0;

		rrIndex = 0;

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

			printHeaderRR( processCount, bw );

			lastSelectedP = 0;
			lastSelectedT = 0;
			int  tempHashIndex = 0;

			int burstTime = 0; int process = 0; int nextAvailrrIndex = 0; int nextAvailableP = 0; int tempIndex = 0;
			int specialCase = 0; int burstHasTleft = 1;
			boolean newTime = false;

			while( time < runFor )
			{
				//If all Ps have finish jump out of for loop
				if(allrrPsFinished(burstSortedTQ, processCount, process ) )
					break;

				specialCase = qRun(bw, rrIndexHash, arrivedProcesses, selectedProcesses, arivedSortedT, burstSortedTQ, firstSelected, time, q, processCount, specialCase);

				//Print arrived// Specifically, arrived selected arrived. the first arrived is our special case
				//We increase time here because we alreaddy  selected and arrived for prev q
				if(specialCase == 1)
				{
					if(newTime == false)
						time = qTime(time, q);

					if(newTime == true)
						;

					newTime = false;
				}					

				//Prep for printing Select
				process = rrIndexHashIP.get(rrIndex); //rrIndex = process - 1
				burstTime = burstTimeForP(burstSortedTQ, process, processCount);
				
				if(arrivedProcesses.contains(process))
				{
					printSelected(bw, time, process, burstTime);
					//keep track of first selected processes
					insertFirstSelectedDataRR(selectedProcesses, firstSelected, process, time, processCount);
					selectedProcesses.add(process);

					//Update burst time
					/*Q */		int nb1 = burstTimeForP(burstSortedTQ, process, processCount) - q;
					/*Finish*/ 	int nb2 = 10000;  //we are using nb1 no need to compare DEBUG_Thought
					//If this returns 0 the P burst has finished
					burstHasTleft = updateBTimeQ( burstSortedTQ, process, processCount, nb1, nb2);
					if( burstHasTleft == 0 )
					{
						newTime = true;
						time = time + burstTime;
						printFinished(bw, time, process);
						//update finished Ps
						finishedProcesses.add(process);
						finished[0][rrIndex] = process;
						finished[1][rrIndex] = time;
					}

					rrIndex++;
					rrIndex = (rrIndex >= processCount)?0:rrIndex;					
				}
				else if( !arrivedProcesses.contains(process) )
				{
					//get next index thats arrived
					tempIndex = nextAvailableArrivedP(rrIndexHash, arrivedProcesses, rrIndex, processCount);

					if(tempIndex == 999)
					{
						//Print IDLE untill you find next available P
						System.out.println("there ar no other arrived Ps");

					}
					else	////Select next P in the que thats that arrived with tempIndex we derived earlier
					{	
						//prep for select
						rrIndex = tempIndex;
						process = rrIndexHashIP.get(rrIndex); //rrIndex = process - 1
						burstTime = burstTimeForP(burstSortedTQ, process, processCount);

						//Print select
						printSelected(bw, time, process, burstTime);
						//keep track of first selected processes
						insertFirstSelectedDataRR(selectedProcesses, firstSelected, process, time, processCount);
						selectedProcesses.add(process);
						//Update burst time
						/*Q */		int nb1 = burstTimeForP(burstSortedTQ, process, processCount) - q;
						/*Finish*/ 	int nb2 = 10000;  //we are using nb1 no need to compare DEBUG_Thought
						//If this returns 0 the P burst has finished
						burstHasTleft = updateBTimeQ( burstSortedTQ, process, processCount, nb1, nb2);
						if( burstHasTleft == 0 )
						{
							newTime = true;
							time = time + burstTime;
							printFinished(bw, time, process);
							//update finished Ps
							finishedProcesses.add(process);
							finished[0][rrIndex] = process;
							finished[1][rrIndex] = time;
						}

						rrIndex++;
						rrIndex = (rrIndex >= processCount)?0:rrIndex;	
					}
				}


				System.out.println();
			}//END of while loop// ALL Ps finished

			while(time < runFor )
			{
				bw.write("Time "+time+": IDLE\n");
				time++;
			}

			//Everything fineshed next we print turnaround and wait 
			bw.write("Finished at time "+time+"\n\n");


			int wait = 0;
			int turnaround = 0;
			int p = 0;

			bubbleSortFinal(finished);
			bubbleSortP(firstSelected);
			bubbleSortP(arivedSortedT);
			bubbleSortP(finished);

			//DEBUG
			System.out.println("firstSelected"+Arrays.deepToString(firstSelected));
			System.out.println("arivedSortedT"+Arrays.deepToString(arivedSortedT));				//DEBUG
			System.out.println("finished"+Arrays.deepToString(finished));				//DEBUG
			
			//Print wait and turnaround times
			for(int i = 0; i < arivedSortedT[0].length; i++)
			{
				p = i;
				wait = firstSelected[1][p] - arivedSortedT[1][p];
				turnaround = finished[1][p] - arivedSortedT[1][p];
				printWaitTurnaroundT(bw, p+1, wait, turnaround);
			}
			


			//////////////////////////////////////////////////////
			// 	End output to file
			///////////////////////////////////////////////////////
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}	

	}//END RUN RR

	public static void insertFirstSelectedDataRR(ArrayList<Integer> arrivedProcesses, Integer[][] myArray, int process, int t, int processCount)
	{ //firstSelected

		if(arrivedProcesses.contains(process))
		{
			return;	
		}
		for(int i = 0; i < processCount; i++)
		{
			if(myArray[0][i] == 0)
			{
				myArray[0][i] = process;
				myArray[1][i] =	t;
				break;
			}
		}
	}

	public static boolean allrrPsFinished( Integer[][] burstSortedTQ, int processCount, int process )
	{
		//Check that all arived Ps finished, if no arrived Ps it shouldn't go in for loop
		for(int p = 0; p < processCount; p++ )
		{
			if( burstSortedTQ[1][p] > 0 )
				return false;
		}
		return true;
	}

	// returns rrIndexHash of nextAvailableArrivedP
	public static int nextAvailableArrivedP(Hashtable<Integer, Integer> rrIndexHash, ArrayList<Integer> arrivedProcesses, int rrIndex, int processCount)
	{
		for(int i = 1; i < processCount; i++ )
		{
			if( arrivedProcesses.contains(rrIndex + i) )
			{
				//System.out.println("nIndex_"+(rrIndex + i));
				//System.out.println("rIndex_"+(rrIndex + i)%processCount);				
				return ( (rrIndex + i)%(processCount) ); //processCount = index - 1
			}
		}
		return 999;
	}

	public static int qRun(BufferedWriter bw, Hashtable<Integer, Integer> rrIndexHash, ArrayList<Integer> arrivedProcesses, ArrayList<Integer> selectedProcesses, Integer[][] arivedSortedT, Integer[][] burstSortedTQ, Integer[][] firstSelected, int time, int q, int processCount, int specialCase)
	{
		int t = time;

		try 
		{
			bw.write(""); //might not need the try catch

			//t will return +1 greater than intuition
			for(t = time; t <time + q; t++)
			{
				System.out.println(" t_"+t);	//DEBUG

				//If process arrived at 0 print arrived and selected. 
				//From now on this prints last arrived befor next selected DEBUG_Thoughts
				if( t == 0 && specialCase == 0 )
				{
					if( pThatArrivedAtThisT(arivedSortedT, t, processCount) != 0 )
					{
						//Keep track of all arrived processes
						int process = pThatArrivedAtThisT(arivedSortedT, t, processCount);					
						arrivedProcesses.add(process);

						//Print arrived then selected
						printArrived(bw, t, process);
						int burstTime = burstTimeForP(burstSortedTQ, process, processCount);
						//Print selected 
						printSelected(bw, t, process, burstTime);
						//keep track of first selected processes
						insertFirstSelectedDataRR(selectedProcesses, firstSelected, process, time, processCount);
						selectedProcesses.add(process);

						//Update burst time
						/*Q */		int nb1 = burstTimeForP(burstSortedTQ, process, processCount) - q;
						/*Finish*/ 	int nb2 = 10000;  //we are using nb1 no need to compare DEBUG_Thought
						//If this returns 0 the P burst has finished
						int burstHasTleft = updateBTimeQ( burstSortedTQ, process, processCount, nb1, nb2);
						if( burstHasTleft == 0 )
						{
							//DEBUG_Thought   make sure you update time later 
							printFinished(bw, time, process);
						}

						rrIndex++;
						rrIndex = (rrIndex >= processCount)?0:rrIndex;
						//lastSelectedP = process;
						//lastSelectedT = t;

						specialCase = 1;
						//return 1;	//this makes specialCase = 1
					}
				}
				
				//Print process arived
				if( pThatArrivedAtThisT(arivedSortedT, t, processCount) != 0 )
				{
					//If we are not on first iteration
					if(specialCase == 0)
					{
						//Keep track of all arrived processes
						int process = pThatArrivedAtThisT(arivedSortedT, t, processCount);					
						arrivedProcesses.add(process);

						printArrived(bw, t, process);

					}  //If we are on first iteration
					else if(specialCase == 1 && t > 0)
					{
						//Keep track of all arrived processes
						int process = pThatArrivedAtThisT(arivedSortedT, t, processCount);					
						arrivedProcesses.add(process);

						printArrived(bw, t, process);					
					}

				}

			}//END time for loop


		} catch (IOException e) {
			e.printStackTrace();
		}

		return specialCase;
	}


	public static int qTime(int time, int q)
	{
		return time + q;
	}

	public static void sortBurstParalelWitArrived(Integer[][] burstSortedTQ, Integer[][] arivedSortedT, int processCount)
	{
		int p;
		int pTemp = 0;
		int tTemp = 0;

		for(int i = 0; i < processCount; i++)
		{
			p = arivedSortedT[0][i];

			for(int j = 0; j < processCount; j++)
			{
				//We found eq p & t from arrived
				if(burstSortedTQ[0][j] == p)
				{

					//If it wasn't by coincidence already in correct possition
					if( i != j )
					{
						//hold the values we will replace in a temp
						pTemp = burstSortedTQ[0][i];
						tTemp = burstSortedTQ[1][i];

						//Put burst in appropriate sorted possition
						burstSortedTQ[0][i] = burstSortedTQ[0][j];
						burstSortedTQ[1][i] = burstSortedTQ[1][j];

						//Dont loose data
						burstSortedTQ[0][j] = pTemp;
						burstSortedTQ[1][j] = tTemp;	
					}				
				}
			}
		}
	}

	//Updating burst time for previous selected numProcess 										//see meth call
	public static int updateBTimeQ( Integer[][] burstSortedT, int  process, int  processCount, int nb1, int nb2)
	{
		int newBurstT = (nb1<nb2)?nb1:nb2;

		if(newBurstT <= 0)
			newBurstT = 0;

		for( int i = 0; i < processCount; i++)
		{
			if(process == burstSortedT[0][i])
				burstSortedT[1][i] = newBurstT;
		}

		return newBurstT;
	}

	public static void printHash(Hashtable<Integer, Integer> rrIndexHash)
	{
		// Get a set of the entries
		Set set = rrIndexHash.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements

		

		while(i.hasNext()) {
		 Map.Entry me = (Map.Entry)i.next();
		 System.out.print("[k,v]");
		 System.out.print(me.getKey() + ":");
		 System.out.print(me.getValue() + ",");
		}		

		System.out.println();
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
		Integer[][] firstSelected = new Integer[2][processCount];
		Integer[][] finished = new Integer[2][processCount];
		//Integer[] arrivedProcesses = new Integer[processCount]

		//I'm certain there's a better way to do this but me in rush
		initSelectedData(firstSelected, processCount);
		initFinishedData(finished, processCount);

		//Create an empty array list with an initial capacity
   		ArrayList<Integer> arrivedProcesses = new ArrayList<Integer>(processCount);
   		//arrlist.add(20); list will contain arrived processes
   		//arrlist.contains(30);	return true or false if it contains it

		int nowTime = 0; int process = 0;

		boolean ranAllPsArrivedIf = false;
		//finishedPreviousSelected[0] = 1 if you finished previous selected P
		//finishedPreviousSelected[0] = 0 if you !finished previous selected P
		Integer[] finishedPreviousSelected = new Integer[1];
		finishedPreviousSelected[0] = 0;

		// use 1 for burst, see method declaration
		sortTimeWithP(burstSortedT, processCount, 1);
		// use 0 for burst, see method declaration
		sortTimeWithP(arivedSortedT, processCount, 0);
		bubbleSortFinal(burstSortedT);
		bubbleSortFinal(arivedSortedT);

		//Print sorted burst/arrival times 	DEBUG
		//System.out.println("Burst:"+Arrays.deepToString(burstSortedT));
		//System.out.println("Arrived:"+Arrays.deepToString(arivedSortedT));

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
			int lastKnownT = 0;

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			///////////////////////////////////////////////////////
			// 	Output to file here
			///////////////////////////////////////////////////////
			printHeaderSJF(processCount, bw);

			for(int t = nowTime; t <= runFor; t++)
			{

				if( pThatArrivedAtThisT(arivedSortedT, t, processCount) != 0 )
				{
					//Keep track of all arrived processes
					process = pThatArrivedAtThisT(arivedSortedT, t, processCount);					
					arrivedProcesses.add(process);

					int tempP = shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);

			/*if*/	if(tempP == process)				//process arrived
					{	//System.out.println("if1");	//DEBUG
						printArrived(bw, t, process);

						currentBurstT = burstTimeForP(burstSortedT, process, processCount);

						//DEBUG what if previous select t is a finished t?
						t = pSelected(bw, t, process, currentBurstT, finishedPreviousSelected, arivedSortedT, burstSortedT, firstSelected, finished, prevSelectP, prevSelectT, processCount);
						prevSelectP = tempP;
						prevSelectT = t;
						lastKnownT = t;
					}
		/*else*/	else								//Arrived !shortestB_PFromArrivedPs
					{	//System.out.println("else");	//DEBUG
						//DEBUG watch out for negatives		//tempP could == process here
						printArrived(bw, t, process);

						lastKnownT = t;
					}

				}//END OF ARRIVED PROCESS
                

			bubbleSortFinal(burstSortedT);
			bubbleSortFinal(arivedSortedT);

			}//END OF FOR LOOP

			//System.out.println("Burst:"+Arrays.deepToString(burstSortedT));
			bubbleSortFinal(burstSortedT);
			bubbleSortFinal(arivedSortedT);
			//System.out.println("Burst:"+Arrays.deepToString(burstSortedT));


			int t = lastKnownT;
			int greatestBurst = 1; //arbitrarily picked number !0
			process = shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
			currentBurstT = burstTimeForP(burstSortedT, process, processCount);

			//Get prevSelectP and go to finished array to see if the last process finished
			// /*boolean*/ = weFinishedPrevSelectedP(finished, processCount, prevSelectP)
			int firstRun = 1;

			boolean finishedPrevSelectedP = weFinishedPrevSelectedP(finished,processCount, prevSelectP);

			if( finishedPrevSelectedP )
			{
				//Check if all Ps arrived And at least one still has burst
				//If so sequentially print processes with shortest burst untill all are finished
				greatestBurst = greatestP_BFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
				//System.out.println("We bout to do something! sb_"+greatestBurst); //DEBUG
				if(allPsHaveArrived(arrivedProcesses, processCount) && (greatestBurst > 0) )
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
						//keep track of first selected processes
						insertFirstSelectedData(firstSelected, process, t, processCount);

						updateBTimeToZero(burstSortedT, process, processCount);

						printFinished(bw, t+currentBurstT, process);
						insertFinishedData(finished, process, t+currentBurstT, processCount);
						t = t+currentBurstT;

						greatestBurst = greatestP_BFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
					}

					while(t < runFor )
					{
						bw.write("Time "+t+": IDLE\n");
						t++;
					}

					bw.write("Finished at time "+runFor+"\n\n");

					bubbleSortP(firstSelected);
					bubbleSortP(arivedSortedT);
					bubbleSortP(finished);


					for(int i = 0; i < arivedSortedT[0].length; i++)
					{
						int p = i;
						//System.out.println("P_"+(p+1)+" B_"+arivalBurst[p+1][1]+" f_"+finished[1][p]+" A_"+arivedSortedT[1][p]);  // DEBUG
						int wait = finished[1][p] - arivedSortedT[1][p] - arivalBurst[p+1][1];
						int turnaround = wait + arivalBurst[p+1][1];
						printWaitTurnaroundT(bw, p+1, wait, turnaround);
					}						
				}//END OF ALL P'S HAVE ARRIVED IF
			}//END finishedPrevSelectedP

			if( !finishedPrevSelectedP )
			{
				//When last process running didn't finish
				while(currentBurstT > 0 && t <= runFor)
				{
					bubbleSortFinal(burstSortedT);
					bubbleSortFinal(arivedSortedT);

					//System.out.println("begin bt_"+currentBurstT+" t_"+t+" P_"+process+"runFor"+runFor);	//DEBUG

					if( firstRun == 1)
					{
						//print finished
						process = shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
						if(process != 0)
						{
							if( !isPFinished(finished, process, processCount) )
							{	//System.out.println("Finished bt_"+currentBurstT+" t_"+t+" P_"+process);	//DEBUG
								currentBurstT = burstTimeForP(burstSortedT, process, processCount);
								printFinished(bw, prevSelectT+currentBurstT, process);
								insertFinishedData(finished, process, t+currentBurstT, processCount);
								updateBTimeToZero(burstSortedT, process, processCount);
								t = prevSelectT+currentBurstT;  //t++ is why -1
								//System.out.println("Finished bt_"+currentBurstT+" t_"+t+" P_"+process);	//DEBUG
							}
						}

						bubbleSortFinal(burstSortedT);
						bubbleSortFinal(arivedSortedT);

						//print selected
						process = shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
						if(process != 0 && !isPFinished(finished, process, processCount) )
						{

							bubbleSort(burstSortedT);
							bubbleSort(arivedSortedT);

							currentBurstT = burstTimeForP(burstSortedT, process, processCount);
							printSelected(bw, t, process, currentBurstT);	//DEBUG
							//System.out.println("Select bt_"+currentBurstT+" t_"+t+" P_"+process);	//DEBUG
							//System.out.println("Burst:"+Arrays.deepToString(burstSortedT));			//DEBUG
							t = prevSelectT+currentBurstT;	//account for t++ (-1), on firstR we dont want this
							prevSelectP = process;
							prevSelectT = t;
							//System.out.println("Select bt_"+currentBurstT+" t_"+t+" P_"+process);	//DEBUG
						}

					}
					if( firstRun == 0 )
					{

						//print finished
						process = shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
						if(process != 0)
						{
							if( !isPFinished(finished, process, processCount) )
							{	//System.out.println("Finished bt_"+currentBurstT+" t_"+t+" P_"+process);	//DEBUG
								currentBurstT = burstTimeForP(burstSortedT, process, processCount);
								printFinished(bw, prevSelectT+currentBurstT - 1, process);
								insertFinishedData(finished, process, t+currentBurstT, processCount);
								updateBTimeToZero(burstSortedT, process, processCount);
								t = prevSelectT+currentBurstT;  //t++ is why -1
								//System.out.println("Finished bt_"+currentBurstT+" t_"+t+" P_"+process);	//DEBUG
							}
						}

						bubbleSortFinal(burstSortedT);
						bubbleSortFinal(arivedSortedT);

						//print selected
						process = shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
						if(process != 0 && !isPFinished(finished, process, processCount) )
						{

							bubbleSort(burstSortedT);
							bubbleSort(arivedSortedT);

							currentBurstT = burstTimeForP(burstSortedT, process, processCount);
							printSelected(bw, t - 1, process, currentBurstT);	//DEBUG
							//System.out.println("Select bt_"+currentBurstT+" t_"+t+" P_"+process);	//DEBUG
							//System.out.println("Burst:"+Arrays.deepToString(burstSortedT));			//DEBUG
							//t = prevSelectT+currentBurstT;	//account for t++ (-1), on firstR we dont want this
							prevSelectP = process;
							prevSelectT = t;
							//System.out.println("Select bt_"+currentBurstT+" t_"+t+" P_"+process);	//DEBUG	
						}			
					}

					//Check to see if we still need to be in the while loop
					//process = shortestB_PFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
					//currentBurstT = burstTimeForP(burstSortedT, process, processCount);
					greatestBurst = greatestP_BFromArrivedPs(arrivedProcesses, burstSortedT, processCount);
					//System.out.println("End gb_"+greatestBurst+" t_"+t+" P_"+process);	//DEBUG
					t++;
					firstRun = 0;
					lastKnownT = t;

				}//END When last process running didn't finish while

				t = finished[0].length;
				t = finished[1][t-1] - 2;	//there is a descrepency of 2 somewhere

				while(t < runFor )
				{
					bw.write("Time "+t+": IDLE\n");
					t++;
				}

				bw.write("Finished at time "+runFor+"\n\n");

				bubbleSortP(firstSelected);
				bubbleSortP(arivedSortedT);
				bubbleSortP(finished);

				//finished[1][p] - arivedSortedT[1][p] - arivalBurst[p+1][1];
				timeThisPArrived(arivedSortedT,process,processCount);
				//System.out.println("Finished:"+Arrays.deepToString(finished));	//DEBUG
				//System.out.println("Arrived:"+Arrays.deepToString(arivedSortedT));	//DEBUG
				//System.out.println("arivalBurst:"+Arrays.deepToString(arivalBurst));	//DEBUG


				for(int i = 0; i < arivedSortedT[0].length; i++)
				{
					int p = i;
					//System.out.println("P_"+(p+1)+" A_"+arivedSortedT[1][P]+" f_"+finished[1][p]+" A_"+arivedSortedT[1][p]);  // DEBUG
					//System.out.println("P_"+(p+1)+" f_"+finished[1][p]+" A_"+arivedSortedT[1][P]+" A_"+arivedSortedT[1][p]);  // DEBUG	
					//int wait = (timeThisPFinished(finished,p,processCount)-2) - timeThisPArrived(arivedSortedT,p,processCount) - arivalBurst[p+1][1];
					int wait = (finished[1][p]-2) - arivedSortedT[1][p]- arivalBurst[p+1][1];
					int turnaround = wait + arivalBurst[p+1][1];
					printWaitTurnaroundT(bw, p+1, wait, turnaround);
				}	

			}//END previous selected didn't finish

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

	public static boolean weFinishedPrevSelectedP(Integer[][] finished, int processCount, int prevSelectP)
	{
		//System.out.println("preSelected_"+prevSelectP);	//DEBUG

		for(int i = 0; i < processCount; i++)
		{
			if(finished[0][i] == prevSelectP)
			{
				//System.out.println("We finished prev selected process"); DEBUG
				return true;
			}
		}
		//System.out.println("NOPE");	DEBUG
		return false;	
	}

	//If (allArrivedPsFinished) or there's no other arrived Ps select current arrived P
	public static boolean nextIsSelect(ArrayList<Integer> arrivedProcesses, Integer[][] finished, int processCount, int process )
	{
		//Check that all arived Ps finished, if no arrived Ps it shouldn't go in for loop
		for(int i = 0; i < arrivedProcesses.size(); i++ )
		{
			if(isPFinished(finished, arrivedProcesses.get(i), processCount) == false  && (arrivedProcesses.get(i) != process) )
				return false;
		}
		return true;
	}

	public static void debug_pSelected(int t, int tempP, int currentBurstT, Integer[][] arivedSortedT, Integer[][] burstSortedT,Integer[][] firstSelected, Integer[][] finished, int prevSelectP,int prevSelectT,int processCount)
	{
		//debug_pSelected( t, tempP, currentBurstT, arivedSortedT, burstSortedT,firstSelected, finished,prevSelectP,prevSelectT,processCount);
		System.out.println(" t_"+t+" tempP_"+tempP+" currentBurstT_"+currentBurstT+" prevSelectP_"+prevSelectP+" prevSelectT_"+prevSelectT+" processCount_"+processCount);
		System.out.println("arivedSortedT:"+Arrays.deepToString(arivedSortedT));
		System.out.println("burstSortedT:"+Arrays.deepToString(burstSortedT));
		System.out.println("firstSelected:"+Arrays.deepToString(firstSelected));
		System.out.println("finished:"+Arrays.deepToString(finished));
	}

	public static int pSelected(BufferedWriter bw, int t, int tempP, int currentBurstT, Integer[] finishedPreviousSelected, Integer[][] arivedSortedT, Integer[][] burstSortedT,Integer[][] firstSelected, Integer[][] finished, int prevSelectP,int prevSelectT,int processCount)
	{
		//System.out.println("pSelected_before");	//DEBUG
		//debug_pSelected( t, tempP, currentBurstT, arivedSortedT, burstSortedT,firstSelected, finished,prevSelectP,prevSelectT,processCount);	//DEBUG

		printSelected(bw, t, tempP, currentBurstT);
		//keep track when processes are selected
		insertFirstSelectedData(firstSelected, tempP, t, processCount);
		//update info //Update burst for previous selected P
		updateBTime(burstSortedT, prevSelectP, processCount, t, prevSelectT);
		prevSelectP = tempP;
		prevSelectT = t;

		//System.out.println("pSelected_middle");	//DEBUG
		//debug_pSelected( t, tempP, currentBurstT, arivedSortedT, burstSortedT,firstSelected, finished,prevSelectP,prevSelectT,processCount);	//DEBUG

		//DEBUG watch out for negatives		//tempP could == process here
		currentBurstT = burstTimeForP(burstSortedT, tempP, processCount);
		finishedPreviousSelected[0] = 0;

		//Check if we can finish running this process
		if( weCanFinishThis(arivedSortedT, processCount, tempP, t, currentBurstT) )
		{
			printFinished(bw, t+currentBurstT, tempP);
			insertFinishedData(finished, tempP, t+currentBurstT, processCount);
			//updateBTime(burstSortedT, prevSelectP, processCount, t+currentBurstT, prevSelectT);
			updateBTimeToZero(burstSortedT, prevSelectP, processCount);
			//System.out.println("pSelected_after_in_if");	//DEBUG
			//debug_pSelected( t, tempP, currentBurstT, arivedSortedT, burstSortedT,firstSelected, finished,prevSelectP,prevSelectT,processCount);	//DEBUG

			finishedPreviousSelected[0] = 1;
			return t+currentBurstT; //Update time 
		}

		//System.out.println("pSelected_after_Noif");	//DEBUG
		//debug_pSelected( t, tempP, currentBurstT, arivedSortedT, burstSortedT,firstSelected, finished,prevSelectP,prevSelectT,processCount);	//DEBUG
		return t;
	}

	public static void insertFirstSelectedData(Integer[][] myArray, int process, int t, int processCount)
	{ //firstSelected
		for(int i = 0; i < processCount; i++)
		{
			if(myArray[0][i] == 0)
			{
				myArray[0][i] = process;
				myArray[1][i] =	t;
				break;
			}
		}
	}

	// [x][y] x = 0 (finished p's), x = 1 (finished p times),  NULL = 0 
	public static void insertFinishedData(Integer[][] finished, int process, int t, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if(finished[0][i] == 0)
			{
				finished[0][i] = process;
				finished[1][i] =	t;
				break;
			}
		}
	}

	public static boolean isPFinished(Integer[][] finished, int process, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if(finished[0][i] == process)
				return true;
		}
		return false;
	}

	//I'm certain this is ugly code. I hate this method but I just want this to work	DEBUG
	public static void initSelectedData(Integer[][] myArray, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
				myArray[0][i] = 0;
				myArray[1][i] =	0;
		}
	}
	//Again, I'm certain this is ugly code. I hate this method but I just want this to work	DEBUG
	public static void initFinishedData(Integer[][] myArray, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
				myArray[0][i] = 0;
				myArray[1][i] =	0;
		}
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

	//arivedSortedT = myArray: I believe this checks if finished goes after selected
	public static boolean weCanFinishThis( Integer[][] myArray, int processCount, int process, int t, int currentBurstT)
	{
		//Once selected, check if you can finish by 
		//seeing if there is an arrival before your burst deminishes
		int remainingBurst = currentBurstT;

		//Should also be if nothing is selected
		//If nothing arrives between now and the end of your burst we can finish
		for(int i = 0; i < processCount; i++)
		{
			if( (myArray[1][i] > t) && (myArray[1][i] < (remainingBurst + t)) )
			{
				//System.out.println("a_"+myArray[1][i]+" r_"+remainingBurst+" t_"+t); //DEBUG
				return false;
			}
		}
		return true;
	}

									//burstSortedT,           prevSelectP, processCount, t+currentBurstT, prevSelectT
	//Updating burst time for previous selected Process
	public static void updateBTime( Integer[][] myArray, int  process, int  processCount, int t, int prevSelectT)
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

	//Process with shortest burst from arrived burst  //myArray = burstSortedT
	public static int shortestB_PFromArrivedPs(ArrayList<Integer> arrivedProcesses, Integer[][] myArray, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if(arrivedProcesses.contains(myArray[0][i]) ) 
				if( myArray[1][i] > 0 )
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

	public static int timeThisPArrived(Integer[][] arivedSortedT, int process, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if( arivedSortedT[0][i] == process )
				return arivedSortedT[1][i];
		}
		return 0;
	}

	public static int timeThisPFinished(Integer[][] finished, int process, int processCount)
	{
		for(int i = 0; i < processCount; i++)
		{
			if( finished[0][i] == process )
				return finished[1][i];
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
	//myArray should be finishedSortedT
	public static void printFinishedAt(BufferedWriter bw, Integer[][] myArray)
	{ //finishedSortedT
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

	public static void printHeaderRR(int processCount, BufferedWriter bw)
	{
		try {
			bw.write(processCount+" processes\n");
			bw.write("Using Round-Robin\n");
			bw.write("Quantum 2\n\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printHeaderSJF(int processCount, BufferedWriter bw)
	{
		try {
			bw.write(processCount+" processes\n");
			bw.write("Using Shortest Job First (Pre)\n\n");
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
            	runfcfs( runFor, processCount );
            }
            if(use.equals("sjf"))
            {
            	System.out.println("use sjf!!!");	//DEBUG
            	runsjf( runFor, processCount );
            }
            if(use.equals("rr"))
            {
            	System.out.println("user rr");
            	runrr( runFor, processCount, quantum );
            }

        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }

	}

	//Sort according to time
	private static void bubbleSort(Integer[][] intArray) 
	{

    int n = intArray.length;
	    int temp = 0;
	    int tempP = 0;
	   
	    for(int i=1; i < n; i++){
	            for(int j=0; j < (n-i); j++){

	                    if(intArray[1][j] > intArray[1][j+1]){
	                            //swap the elements!
	                            temp = intArray[1][j+1];
	                            intArray[1][j+1] = intArray[1][j];
	                            intArray[1][j] = temp;

	                            //now swap the p values
	                            tempP = intArray[0][j+1];
	                            intArray[0][j+1] = intArray[0][j];
	                            intArray[0][j] = tempP;
	                    }
	                   
	            }
	    }
	}

	//Sort according to time
	private static void bubbleSortFinal(Integer[][] intArray) 
	{

    int n = intArray[0].length;
	    int temp = 0;
	    int tempP = 0;
	   
	    for(int i=1; i < n; i++){
	            for(int j=0; j < (n-i); j++){
	            		//System.out.println("Burst:"+Arrays.deepToString(intArray));	//DEBUG
	                    if(intArray[1][j] > intArray[1][j+1]){
	                            //swap the elements!
	                            temp = intArray[1][j+1];
	                            intArray[1][j+1] = intArray[1][j];
	                            intArray[1][j] = temp;

	                            //now swap the p values
	                            tempP = intArray[0][j+1];
	                            intArray[0][j+1] = intArray[0][j];
	                            intArray[0][j] = tempP;
	                    }
	                   
	            }
	    }
	    //intArray[1][n-1]=72;
	    //System.out.println("Burst:"+Arrays.deepToString(intArray)+" n_"+n);	//DEBUG

	}

	//Sort according to process
	private static void bubbleSortP(Integer[][] intArray) 
	{

    int n = intArray[0].length;
	    int temp = 0;
	    int tempP = 0;
	   
	    for(int i=0; i < n; i++){
	            for(int j=1; j < (n-i); j++){
	                   
	                    if(intArray[0][j-1] > intArray[0][j]){
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



				
