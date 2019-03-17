import java.util.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;

// Main file, which everything runs from.
public class TSP {
	
// In this method, use variables "fileno" and "gens" to select the file used and how many generations are run, respectively.
public static void main(String[] args) {
	
	int nrow = 0;
	int ncol = 0;
	
	// Valid numbers to choose from are: 48, 51, 52, 70, 76, 100, 105, 442
	int fileno = 100;
	int gens = 15;//iterations
			
	double [] [] arrayTSP = new double [nrow] [ncol];
	ArrayList<Integer> arrayOP = new ArrayList<Integer>();
	arrayTSP = ReadArrayFile("C:\\temp\\CS2004 TSP Data\\TSP_" + fileno + ".txt"," ");
	arrayOP = ReadIntegerFile("C:\\temp\\CS2004 TSP Data//TSP_" + fileno + "_OPT.txt");
	 

	
	//System.out.println(arrayOP);
	//PrintArray(arrayTSP);
			
	ArrayList<Integer> a = RandomPerm(arrayTSP);		
	double fulldistanceop = OP_Quality(arrayTSP,arrayOP);
				
	for (int i =0; i < gens ; i++) {
		RandomMutationHillClimbing(arrayTSP, a , 1000);
	}
		
		
	for(int l = 0; l < gens; l++) {
		SimulatedAnnealing(arrayTSP,a,100,1000,1);
	} 
}

//Shared random object
static private Random rand;
	
// Create a uniformly distanceributed random integer between aa and bb inclusive
static public int UI(int aa, int bb) {
	int a = Math.min(aa, bb);
	int b = Math.max(aa, bb);
	if (rand == null) {
		rand = new Random();
		rand.setSeed(System.nanoTime());
	}
	int d = b - a + 1;
	int x = rand.nextInt(d) + a;
	return (x);
}

// Create a uniformly distanceributed random double between a and b inclusive
static public double UR(double a, double b) {
	if (rand == null) {
		rand = new Random();
		rand.setSeed(System.nanoTime());
	}
	return ((b - a) * rand.nextDouble() + a);
}

// Starting point: methodcontaining the creation of an array of integers, containing a permutation of {1,...,n}.
private static ArrayList<Integer> RandomPerm(double[][] array) {
	
	ArrayList<Integer> p = new ArrayList<Integer>();
	
	for(int i = 0; i < array.length ; i++) {
		p.add(i);
	}
		
	ArrayList<Integer> a = new ArrayList<Integer>();
			
	while(p.size()>0) {
		int i = UI(0,p.size()-1);
		a.add(p.get(i));
		p.remove(i);
	}
	
	return a;
}
	
// Method containing an array representation of the Fitness Function.
private static double FitnessFunction(double[][] array,ArrayList<Integer> a) {
	
	double fulldistance = 0;
			
	for(int i = 0; i < a.size()-1; i++) {
		int j = a.get(i);
		int k = a.get(i+1);
		double distance = array[j][k];
		fulldistance = fulldistance+distance;
	}
	
	return fulldistance;
}

// Method containing an array representation of the Fitness Function.
private static double OP_Quality(double[][] array, ArrayList<Integer> arrayOP) {
	
	double fulldistanceop = 0;
			
	for(int i = 0; i < arrayOP.size()-1; i++) {
		int j = arrayOP.get(i);
		int k = arrayOP.get(i+1);
		double distance = array[j][k];
		fulldistanceop = fulldistanceop+distance;
	}	
	
		System.out.println("\nOptimal Quality: "+fulldistanceop+"\n");
	
		return fulldistanceop;
}
		
// Implementation of a "Small Change" method, which ensures that any changes to the array stay valid and be a permutation.	
private static ArrayList<Integer> SmallChange(ArrayList<Integer> array) {
	
	int i = 0,j=0;
	while(i==j) {
		i = UI(0, array.size()-1);
		j = UI(0, array.size()-1);
	}

	int i2 = (array.get(i));
	int j2 = (array.get(j));	
		
	array.set(i, j2);
	array.set(j, i2);

	return array;
}

// Method containing the implementation of the first algorithm, Random Mutation Hill Climbing.

@SuppressWarnings({ "rawtypes", "unchecked", "null" })
private static double RandomMutationHillClimbing(double[][] arrayTSP, ArrayList a, int iter) {
	
	if (arrayTSP == null || iter < 1)
	{
		return (Double) null;
	}
	double lowdistance = FitnessFunction(arrayTSP,a);
	double newdistance = 0;
	System.out.println("Initial distance: "+lowdistance);
	
	for(int i = 0; i < iter; i++) {
		SmallChange(a);
		if ((newdistance = FitnessFunction(arrayTSP,a))<lowdistance) {
			lowdistance = newdistance;
		}			
	}
	
		
	System.out.println("RMHC distance: "+lowdistance+"\n \n");
	return lowdistance;
}

	// Method containing the implementation of the fourth algorithm, Simulated Annealing.
	@SuppressWarnings({ "rawtypes", "unchecked", "null" })
	private static double SimulatedAnnealing(double[][] arrayTSP, ArrayList a, double starTemp,int iter, double coolRate) {
		
		if (arrayTSP == null || iter < 1)
		{
			return (Double) null;
		}
		double fitness = FitnessFunction(arrayTSP, a);
		double p;
		
		for(double i = 0 ; i<iter -1 ; i=i+coolRate) {
			ArrayList<Integer> newA = SmallChange(a);
			double newFitness = FitnessFunction(arrayTSP, newA);
			if (newFitness>fitness) {
				p = Math.exp((-(FitnessFunction(arrayTSP,a)/FitnessFunction(arrayTSP,newA)))/starTemp);
				if(p < UR(0,1)) {
				}
				else {
					a = newA;
				}
			}
			else {
				a = newA;
			}
		}
		
		System.out.print("SA distance: "+FitnessFunction(arrayTSP,a)+"\n \n");

		return FitnessFunction(arrayTSP,a);
	}

	//Print a 2D double array to the console Window
	static public void PrintArray(double x[][])
	{
		for(int i=0;i<x.length;++i)
		{
			for(int j=0;j<x[i].length;++j)
			{
				System.out.print(x[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	//This method reads in a text file and parses all of the numbers in it
	//This method is for reading in a square 2D numeric array from a text file
	//This code is not very good and can be improved!
	//But it should work!!!
	//'sep' is the separator between columns
	static public double[][] ReadArrayFile(String filename,String sep)
	{
		double res[][] = null;
		try
		{
			BufferedReader input = null;
			input = new BufferedReader(new FileReader(filename));
			String line = null;
			int ncol = 0;
			int nrow = 0;
			
			while ((line = input.readLine()) != null) 
			{
				++nrow;
				String[] columns = line.split(sep);
				ncol = Math.max(ncol,columns.length);
			}
			res = new double[nrow][ncol];
			input = new BufferedReader(new FileReader(filename));
			int i=0,j=0;
			while ((line = input.readLine()) != null) 
			{
				
				String[] columns = line.split(sep);
				for(j=0;j<columns.length;++j)
				{
					res[i][j] = Double.parseDouble(columns[j]);
				}
				++i;
			}
		}
		catch(Exception E)
		{
			System.out.println("+++ReadArrayFile: "+E.getMessage());
		}
	    return(res);
	}

	
	//This method reads in a text file and parses all of the numbers in it
	//This code is not very good and can be improved!
	//But it should work!!!
	//It takes in as input a string filename and returns an array list of Integers
	static public ArrayList<Integer> ReadIntegerFile(String filename)
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		Reader r;
		try
		{
			r = new BufferedReader(new FileReader(filename));
			StreamTokenizer stok = new StreamTokenizer(r);
			stok.parseNumbers();
			stok.nextToken();
			while (stok.ttype != StreamTokenizer.TT_EOF) 
			{
				if (stok.ttype == StreamTokenizer.TT_NUMBER)
				{
					res.add((int)(stok.nval));
				}
				stok.nextToken();
			}
		}
		catch(Exception E)
		{
			System.out.println("+++ReadIntegerFile: "+E.getMessage());
		}
	    return(res);
	}
}