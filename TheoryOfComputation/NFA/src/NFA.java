import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import java.io.*;

/** Samuel C. Tyler
 * Theory of Computation Fall 2010 - Homework 1
 * 
_NFA Simulator_
For this problem, you will write a program called NFA.cpp or NFA.java that simulates NFA. Your program
will read an NFA description in the input format given below. It will accept (print out [accept]) if and only
if a given input string is in the language of its given NFA. Otherwise, your program will print out [reject]
and exit.

Your NFA program will take two command-line arguments. The ﬁrst argument will give the name of an
input ﬁle containing a description of a nondeterministic ﬁnite automaton. The second argument will give
the name of a ﬁle containing an input string.

The following example is a sample NFA description for your program. Each line starting with trans
describes a transition of the automaton. The ﬁrst value after trans gives the state (number) the transition
is from, the next value gives the input symbol for the transition, and the third value gives the destination
state for the transition. Thus, the ﬁfth line below says that there is a transition from state 1 to state 2 on
input symbol b. States are non-negative integers, and the input symbols will always be in the range a . . . z.
Also, since the automaton is nondeterministic, there may be more than one transition from the same state
on the same input symbol. For example, lines one and four below say that, if the automaton is in state zero,
it can go to state zero and state one when it sees input symbol a. There may not be any transitions deﬁned
for some state, symbol pairs. The automaton below, for example, doesn’t have any transitions deﬁned for
what it should do when sees the symbol a while in state two.

After the sequence of transitions, the automaton description will contain a (possibly empty) list of starting
states. Each starting state is given as a line containing the word start followed by a non-negative integer.
If an automaton has more than one start state, there will be more than one start line.
Finally, lines that start with the word final indicate ﬁnal (accepting) states for the automaton. The
number after the keyword final gives the state that is an accepting state. For automata that have more
than one accepting state, there may be more than one final line.
trans 0 a b
trans 0 b 0
trans 0 c 0
trans 0 a 1
trans 1 b 2
trans 2 c 3
trans 0 b 4
trans 4 b 5
trans 5 b 3
trans 3 a 3
trans 3 b 3
start 0
final 3

The input string for the NFA will be read from the ﬁle named by the second command-line argument.
The string will be given on a line by itself (i.e., the contents of the string followed by a newline), and the
string will consist of only characters from a . . . z.
You do not need to turn in a printout of your source code. Just submit an electronic copy before class
on the due date.

 */

/**
 * @author sam
 *
 */
/**
 * @author sam
 *
 */
public class NFA {
	//the machine. list of all states
	ArrayList<State> theStates = new ArrayList<State>();

	//currently "active" states
	ArrayList<State> startStates = new ArrayList<State>();
	
	private class State{
		private int idx;
		private boolean isAccept=false;
		private boolean isStart=false;
		
		//the following are values in the form <String,ArrayList<Integer>>
		private HashMap toStates=new HashMap();
		private HashMap fromStates=new HashMap();
		
		public State(int i){this.idx=i;}
		/**
		 * @param from the index of the node we are coming from
		 * @param to the index of the node we are going to
		 * @param thechar the String (char) we read in
		 */
		public State(int i,int to, String thechar){}
		/**
		 * @param to the index of the node we are going to
		 * @param thechar the String (char) we read in
		 * @param accept are we an accept state?
		 * @param start are we a start state?
		 */
		public State(int i,int to, String thechar, boolean accept, boolean start){
			this.idx=i;this.isStart=start;this.isAccept=accept;
			this.toStates.put(thechar,to);
		}
		/**
		 * @param b Turn on/off start flag
		 */
		public void setStart(boolean b){
			this.isStart=b;
			//since we are a start state, we have no from
			this.fromStates.clear();
		}
		/**
		 * @param b turn on/off accept flag
		 */
		public void setAccept(boolean b){this.isAccept=b;}
		public boolean getAccept(){return this.isAccept;}
		/**
		 * @param i the index in the state list. also - the index of the node
		 */
		public void setIndex(int i){this.idx=i;}
		public int getIndex(){return this.idx;}
		/**
		 * @return
		 */
		public HashMap getFromStates(){return this.fromStates;}
		/**
		 * @return
		 */
		public HashMap getToStates(){return this.toStates;}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString(){
			return ""+this.idx+(this.isAccept?"f":"")+(this.isStart?"s":"");//+": "+this.fromStates+" , "+this.toStates+"";
		}
		/**
		 * @param to
		 * @param thechar
		 */
		public void addTo(int to,String thechar){
			//if key already exists, add node to it, otherwise create a new array and add the array to the map
			if(this.toStates.get(thechar)!=null){
				((ArrayList<Integer>)this.toStates.get(thechar)).add(to);
			}else{
				ArrayList<Integer> newA=new ArrayList<Integer>();
				newA.add(to);
				this.toStates.put(thechar, newA);
			}
		}
		/**
		 * @param from
		 * @param thechar
		 */
		public void addFrom(int from,String thechar){
			//if key already exists, add node to it, otherwise create a new array and add the array to the map
			if(this.fromStates.get(thechar)!=null){
				((ArrayList<Integer>)this.fromStates.get(thechar)).add(from);
			}else{
				ArrayList<Integer> newA=new ArrayList<Integer>();
				newA.add(from);
				this.fromStates.put(thechar, newA);
			}
		}
		/**
		 * @param thechar
		 * @return
		 */
		public ArrayList<Integer> goesTo(String thechar){
			return (ArrayList<Integer>)toStates.get(thechar);
		}
	}
	
	/**
	 * 
	 */
	public NFA(){}
	/**
	 * @param f
	 */
	public NFA(File f){readMachineDescription(f);}
	/**
	 * @param fn
	 */
	public NFA(String fn){readMachineDescription(new File(fn));}

	/**
	 * @param filename
	 */
	public void readMachineDescription(String filename){readMachineDescription(new File(filename));}
	/**
	 * @param theFile
	 */
	public void readMachineDescription(File theFile){
		try {

			// read input file containing machine description
			FileInputStream inStream = new FileInputStream(theFile);
			BufferedReader theReader = new BufferedReader(new InputStreamReader(inStream));

			//read each line and determine what values we have, either {"trans","start","final"}
			while (theReader.ready()) {
				String theLine = theReader.readLine();
				
				//NOTE: I like regex, so i am gonna use it here. There is is probably a nicer way, but I like this approach.
				//The following bit of code assembles pattern matchers for the input file
				Pattern ptrans = Pattern.compile("trans\\s*(\\d)\\s*(\\S*)\\s*(\\d)"),
						pstart = Pattern.compile("start\\s*(\\d)"),
						pfinal = Pattern.compile("final\\s*(\\d)"),
						pcomment = Pattern.compile("$\\s*\\#");
				Matcher tmatch=ptrans.matcher(theLine),
						smatch=pstart.matcher(theLine),
						cmatch=pcomment.matcher(theLine),
						fmatch=pfinal.matcher(theLine);

				//first, we check for the most used occurrence, being "trans"
				if (cmatch.find()){
					//ignore comments, line that begin with a #
				}else if (tmatch.find()){
					//save values for readability
					int from = new Integer(tmatch.group(1));
					String thechar = tmatch.group(2);
					int to = new Integer(tmatch.group(3));
					
					//resize machine to hold the number of states in from and to
					ensureMachineSize(from);
					ensureMachineSize(to);
					theStates.get(from).addTo(to, thechar);
					theStates.get(to).addFrom(from, thechar);
					
					//System.out.println("Trans Line: at "+tmatch.group(1)+", on "+tmatch.group(2)+" go to "+tmatch.group(3));

				//next, we check for "start"
				}else if (smatch.find()){
					//System.out.println("Start:"+smatch.group(1));
					theStates.get(new Integer(smatch.group(1))).setStart(true);
					startStates.add(theStates.get(new Integer(smatch.group(1))));
				//lastly, we check for "final"
				}else if (fmatch.find()){
					//System.out.println("Final:"+fmatch.group(1));
					theStates.get(new Integer(fmatch.group(1))).setAccept(true);
				} 
			}

			// dispose all the resources after using them.
			inStream.close();
			theReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/** ensures the machine has at least <i>i</i> nodes
	 * @param i
	 */
	private void ensureMachineSize(int i){
		if (theStates.size()<=i)
			for (int j=theStates.size();j<=i;j++)
				theStates.add(new State(j));
	}

	
	/**
	 * @param theInput
	 */
	public void processInput(String theFile){processInput(new File(theFile));}
	/**
	 * @param theInput
	 */
	public void processInput(File theFile){
		ArrayList<String> theInputString=new ArrayList<String>();
		ArrayList<String> theTestResults=new ArrayList<String>();

		try {

			// read input file containing machine description
			FileInputStream inStream = new FileInputStream(theFile);
			BufferedReader theReader = new BufferedReader(new InputStreamReader(inStream));
			while (theReader.ready()) {
				String curLine=theReader.readLine().trim();//read the line, trimming whitespace at beginning and end
				if(curLine.isEmpty() || curLine.startsWith("#")); //skip blank lines and comments
				else{
										
					if(curLine.endsWith("#f")){
						theTestResults.add("f");
						curLine=curLine.split("\\#")[0];
					}else if(curLine.endsWith("#a")){
						theTestResults.add("a");
						curLine=curLine.split("\\#")[0];
					}else
						theTestResults.add("");
					//add the input string to the lsit of input strings
					theInputString.add(curLine.trim());
				}
			}
			// close the file readers
			inStream.close();
			theReader.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int curIdx=0;
		for(String s:theInputString){
			if(canHandleString(s)){
				System.out.println("[accept]");
				boolean b = checkVaildity(s,true,theTestResults.get(curIdx));
			}else{
				System.out.println("[reject]");
			}
			curIdx++;
		}
		
	}
	private boolean checkVaildity(String theTestString,boolean actualResult, String intendedResult){
		boolean isValid=false;
		String errString="";
		if(intendedResult==null){
			errString+="No result data for "+theTestString+". Actual result is "+actualResult+".";
			isValid=true;
		}else{
			boolean intendedBool=false;
			if(intendedResult.equals("f"))
				intendedBool=false;
			if(intendedResult.equals("a"))
				intendedBool=true;

			if(actualResult==intendedBool){
				errString+="Test matches for "+theTestString+".";
				isValid=true;
			}else{
				errString+="Test fails with "+theTestString+" ("+actualResult+" != "+intendedBool+") Something is wrong with your logic. Check the machine and input and try again.";
				isValid=false;				
			}
		}
		System.err.println(errString);
		return isValid;
	}
	public boolean canHandleString(String s){
		ArrayList<State> curStates=new ArrayList<State>(startStates);
		boolean ret=false;
		//System.err.println("Begin Processing "+s);//+" with:\n"+curStates);

			for(char c :s.toCharArray()){
				//copy into new array so we aren't messing with original
				ArrayList<Integer> addStateIdxs = new ArrayList<Integer>();
				ArrayList<Integer> removeStateIdxs = new ArrayList<Integer>();
				
				//Step 1: check all curStates for char
				for(int i=0;i<curStates.size();i++){
					//Step 1.1: if curStates[i] can move to a new state,
					ArrayList<Integer> goesTo=curStates.get(i).goesTo(""+c);
					if(goesTo!=null && goesTo.size()>0){
						//1.1.1: add to removeStateIdxs
						removeStateIdxs.add(i);
						//1.1.2: append to newStateIdxs
						addStateIdxs.addAll(curStates.get(i).goesTo(""+c));
					}else{
						//1.1.3: add to removeStateIdxs if we can't go anywhere
						removeStateIdxs.add(i);
					}
				}
				//Step 2: remove currentStates[removeStateIdxs]
				for(int i:removeStateIdxs){
					curStates.remove(i);
				}
				//Step 3: append addStateIdxs to curState
				for(int i:addStateIdxs){
					curStates.add(theStates.get(i));
				}

				//System.err.println("at "+c+": "+curStates);
			}
			
			for(State S:curStates){
				if(S.getAccept()){
					ret=true;
					//System.out.println("ACCEPTS!");
				}
			}
//			System.out.println((ret?"ACCEPTS!":"REJECTS!"));
		
		return ret;
	}
	
	/**BONUS! make a dot file for use with the graphviz package.
	 * @return
	 */
	public String buildDotString(){
		String ret="";
		for (State s : theStates){
			for (Object withChar : s.getToStates().keySet()){
				for (Integer toNode : (ArrayList<Integer>)s.getToStates().get(withChar)){
					ret+=s.getIndex()+" -> "+toNode+"[label=\""+withChar+"\"]\n";
				}
			}
		}
		return ret;
	}
	/**
	 * @param theFile
	 */
	public void saveDotFile(String theFile){
	}
	/**
	 * @param inFile
	 * @param outFile
	 */
	public void saveDotGraph(String inFile,String outFile){
		
		ProcessBuilder p = new ProcessBuilder("dot","-o"+outFile,inFile);
		try {
			p.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param theFile
	 */
	public void showDotFile(String theFile){
		ProcessBuilder p = new ProcessBuilder("dot",theFile);
		try {
			p.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * @param theData
	 */
	public void showDotGraph(String theData){}
	/**
	 * @param theFile
	 */
	public void showDotGraph(File theFile){}
	public void printMachine(){
		for(State s:theStates){
			System.out.println(s);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Make sure we are operating with 2 files
		if(args.length==1){
			System.out.println("Please specify file containing input string.");
			System.exit(1);
		}else if (args.length == 0 ){
			System.out.println("Usage: java NFA <Description File> <Input File>.");
			System.exit(1);
		}
		String NFADescriptionFile=args[0];
		String NFAInputFileName=args[1];
		NFA theMachine=new NFA(NFADescriptionFile);
		//theMachine.printMachine();
		theMachine.processInput(NFAInputFileName);
		System.out.print(theMachine.buildDotString());
	}

}
