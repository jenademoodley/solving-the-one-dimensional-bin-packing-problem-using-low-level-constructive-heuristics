package binpacking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import bpp2.BPP2;

public class Packing {
	
	public static void displayBins(ArrayList<ArrayList<Integer>> bins){
		for(int i=0; i<bins.size(); i++){
			System.out.print("Bin " + (i+1) +":\t\t");
			for(int j=0; j<bins.get(i).size(); j++){
				System.out.print("\t" + bins.get(i).get(j));
			}
			System.out.println();
		}
	}

	public static double fitness(ArrayList<ArrayList<Integer>> bins, int cap) {
		double fitness = 0;
		double sum = 0;

		for (int i = 0; i < bins.size(); i++) {
			double fullness = 0;
			for(int j=0; j<bins.get(i).size(); j++){
			fullness += bins.get(i).get(j);
		}

			sum += Math.pow((fullness / cap), 2);
		}

		fitness = 1 - (sum / bins.size());
		double fit = Math.round(fitness * 10000000);
		fit = fit / 10;
		fitness = Math.round(fit);
		fitness = fitness / 1000000;

		return fitness;
	} 
	
	public static int arraySum(ArrayList<Integer> array){
		int sum = 0;
		for(int i=0; i<array.size(); i++)
			sum += array.get(i);
		
		return sum;
	}

	public static void firstFit(String filename) {
		BPP2 bin = new BPP2(filename);
		ArrayList<ArrayList<Integer>> bins = new ArrayList<>();

		bin.readFile();
		int[] items = bin.getItemWeights();

		int cap = bin.getBinCap();

		ArrayList<Integer> firstBin = new ArrayList<>();
		boolean addNewBin = true;
		bins.add(firstBin);

		for (int i = 0; i < items.length; i++) {
			for (int j = 0; j < bins.size(); j++) {
				int outcome = arraySum(bins.get(j)) + items[i];
				if (outcome <= cap) {
					bins.get(j).add(items[i]);
					addNewBin = false;
					break;
				}
			}

			if (addNewBin){
				ArrayList<Integer> newBin = new ArrayList<>();
				newBin.add(items[i]);
				bins.add(newBin);
			}
			addNewBin = true;
		}

		double fit = fitness(bins, cap);
		displayBins(bins);
		System.out.println("\nNumber of Bins:\t\t" + bins.size());
		System.out.println("Fitness:\t\t" + fit);
	}

	public static void bestFit(String filename) {
		BPP2 bin = new BPP2(filename);
		ArrayList<ArrayList<Integer>> bins = new ArrayList<>();


		bin.readFile();
		int[] items = bin.getItemWeights();

		int cap = bin.getBinCap();

		ArrayList<Integer> firstBin = new ArrayList<>();
		boolean addNewBin = true;
		bins.add(firstBin);


		int best = cap;
		int counter = -1;

		for (int i = 0; i < items.length; i++) {
			for (int j = 0; j < bins.size(); j++) {
				int outcome = arraySum(bins.get(j)) + items[i];
				int space = cap - outcome;
				if (space >= 0 && space < best) {
					best = space;
					counter = j;
					addNewBin = false;
				}
			}

			if (addNewBin){
				ArrayList<Integer> newBin = new ArrayList<>();
				newBin.add(items[i]);
				bins.add(newBin);
			}

			else 
				bins.get(counter).add(items[i]);

			addNewBin = true;
			counter = -1;
			best = cap;

		}
		double fit = fitness(bins, cap);
		displayBins(bins);
		System.out.println("\nNumber of Bins:\t\t" + bins.size());
		System.out.println("Fitness:\t\t" + fit);
	}

	public static void nextFit(String filename) {
		BPP2 bin = new BPP2(filename);
		ArrayList<ArrayList<Integer>> bins = new ArrayList<>();

		bin.readFile();
		int[] items = bin.getItemWeights();

		int cap = bin.getBinCap();

		ArrayList<Integer> firstBin = new ArrayList<>();
		bins.add(firstBin);

		int counter = 0;

		for (int i = 0; i < items.length; i++) {
			int outcome = arraySum(bins.get(counter)) + items[i];
			if (outcome <= cap)
				bins.get(counter).add(items[i]);

			else {
				ArrayList<Integer> newBin = new ArrayList<>();
				newBin.add(items[i]);
				bins.add(newBin);
				counter++;
			}
		}

		double fit = fitness(bins, cap);
		displayBins(bins);
		System.out.println("\nNumber of Bins:\t\t" + bins.size());
		System.out.println("Fitness:\t\t" + fit);
	}

	public static void worstFit(String filename) {

		BPP2 bin = new BPP2(filename);
		ArrayList<ArrayList<Integer>> bins = new ArrayList<>();

		bin.readFile();
		int[] items = bin.getItemWeights();

		int cap = bin.getBinCap();

		ArrayList<Integer> firstBin = new ArrayList<>();
		boolean addNewBin = true;
		bins.add(firstBin);

		int best = 0;
		int counter = -1;

		for (int i = 0; i < items.length; i++) {
			for (int j = 0; j < bins.size(); j++) {
				int outcome = arraySum(bins.get(j)) + items[i];
				int space = cap - outcome;
				if (space <= cap && space >= best) {
					best = space;
					counter = j;
					addNewBin = false;
				}
			}

			if (addNewBin){
				ArrayList<Integer> newBin = new ArrayList<>();
				newBin.add(items[i]);
				bins.add(newBin);
			}

			else
				bins.get(counter).add(items[i]);

			addNewBin = true;
			counter = -1;
			best = 0;

		}

		double fit = fitness(bins, cap);
		displayBins(bins);
		System.out.println("\nNumber of Bins:\t\t" + bins.size());
		System.out.println("Fitness:\t\t" + fit);
	}

	public static void main(String[] args) {
		String[] filenames = { "EasyA.txt", "EasyB.txt", "MediumA.txt",
				"MediumB.txt", "HardA.txt", "HardB.txt" };

		Scanner sc = new Scanner(System.in);

		System.out.println("One-Dimensional Bin-Packing Problem");
		System.out.println("Choose a Problem Instance (Number Only)");

		for (int i = 0; i < filenames.length; i++)
			System.out.println((i + 1) + " - " + filenames[i]);

		int num = sc.nextInt();

		while (num < 0 || num > filenames.length) {
			System.out.println();
			System.out.println("Option Not Available");
			System.out.println("Choose a Problem Instance (Number Only)");

			for (int i = 0; i < filenames.length; i++)
				System.out.println((i + 1) + " - " + filenames[i]);
			System.out.println();

			num = sc.nextInt();
		}

		System.out.println();
		System.out.println("Enter Heuristic (Number Only)");
		System.out.println("1 - First Fit");
		System.out.println("2 - Next Fit");
		System.out.println("3 - Best Fit");
		System.out.println("4 - Worst Fit");
		int choice = sc.nextInt();

		while (choice < 1 || choice > 4) {
			System.out.println();
			System.out.println("Option Not Available");
			System.out.println("Enter Heuristic (Number Only)");
			System.out.println("1 - First Fit");
			System.out.println("2 - Next Fit");
			System.out.println("3 - Best Fit");
			System.out.println("4 - Worst Fit");
			choice = sc.nextInt();
		}

		System.out.println("\nProblem Instance:\t" + filenames[num - 1]);

		switch (choice) {
		case 1:
			System.out.println("Heuristic:\t\tFirst Fit\n");
			firstFit("src/resources/" + filenames[num - 1]);
			break;

		case 2:
			System.out.println("Heuristic:\t\tNext Fit\n");
			nextFit("src/resources/" + filenames[num - 1]);
			break;

		case 3:
			System.out.println("Heuristic:\t\tBest Fit\n");
			bestFit("src/resources/" + filenames[num - 1]);
			break;

		case 4:
			System.out.println("Heuristic:\t\tWorst Fit\n");
			worstFit("src/resources/" + filenames[num - 1]);
			break;

		}

		System.out.println("\nPush Enter to Continue");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.close();

	}
}
