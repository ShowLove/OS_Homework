import java.util.Arrays;
import java.io.*;
import java.util.*;
import java.util.Arrays;

public class processes
{
	public static int[][] arivalBurst;

	public static void main( String args[] )
	{
		processes();
	}

/*DEBUG
            System.out.println("p_"+processCount+" r_"+runFor+" u_"+use);
            System.out.println(" q_"+quantum+" proCount_"+processCount);
            System.out.println(" arrival_"+arrival+" burst_"+burst);
            System.out.println(Arrays.deepToString(arivalBurst));
*/

	public static void runfcfs(int runFor, int processCount)
	{
		int timeData = processCount*3;
		int arrival = 0;	int burst = 0;
		int selected = 0;
		int finished = 0;
		int prevSelected = 0;
		int prevFinished = 0;

		//Map<String, List<String>> pData = new Hashtable<String, List<String>>();
		//Map<Integer, List<Integer>> pData = new Hashtable<Integer, List<Integer>>();

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
			/* DEBUG
			System.out.println("Time "+timeList[p][0]+": P"+p+" arrived");
			System.out.println("Time "+timeList[p][1]+": P"+p+" selected (burst "+burst+")" );
			System.out.println("Time "+timeList[p][2]+": P"+p+" finished");
			*/
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

/*
		Arrays.sort( arivedSortedT );
		Arrays.sort( selectedSortedT );
		Arrays.sort( finishedSortedT );
		Arrays.sort( sortedTimes );

		System.out.println("1D Array->");
		System.out.println( Arrays.toString( sortedTimes ) );
		System.out.println( Arrays.toString( arivedSortedT ) );
		System.out.println( Arrays.toString( selectedSortedT ) );
		System.out.println( Arrays.toString( finishedSortedT ) );
*/
	}

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
	            		//get process number as int//*numProcessStr = values[i+1];*///*numChar = numProcessStr.charAt(1);//
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
            System.out.println("p_"+processCount+" r_"+runFor+" u_"+use);
            System.out.println(" q_"+quantum+" proCount_"+processCount);
            System.out.println(" arrival_"+arrival+" burst_"+burst);
            System.out.println(Arrays.deepToString(arivalBurst));
            input.close();

            ///////////////////////////
            //process information here
            ///////////////////////////

            //if fcfs
            if(use.equals("fcfs"))
            {
            	System.out.println("use fcfs!!!");
            	runfcfs(runFor, processCount);
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