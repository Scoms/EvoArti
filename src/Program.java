import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class Program {

	static int nbIndividus = 100000;
	static int nbCoeurs = 10000;
	static int nbGenerations = 100;
	static int generationCylcle = 100;
	
	public static void main(String[] args) throws RowsExceededException, BiffException, WriteException, IOException
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Nombre de générations : (max 150) ");
		int generation = Integer.parseInt(scanner.nextLine());
		
		//Reduce type
		Enums.Reduce reduceType = determineEnum(scanner);
		
		//Best Proportion 
		int bestProportion = 0;
		if(reduceType == Enums.Reduce.Elitiste)
		{
			bestProportion = Math.max(0,Math.min(determineBestProportion(scanner), 100));
		}
		
		//Doublons 
		boolean allowDoublons = determineDoublons(scanner);
		
		System.out.println(String.format("Reduce : %s, %s doublons",
				reduceType.toString(),
				allowDoublons ? "avec" : "sans"
				));
		
		long start =  System.currentTimeMillis();
		lancerSimulation(reduceType, bestProportion, allowDoublons, generation);
		long end = System.currentTimeMillis();
		
		long ecart = end - start;
		float second = ecart / 1000;
		
		System.out.println(String.format("Temps d'éxécution %f s",second));
	}
	
	
	private static boolean determineDoublons(Scanner scanner) {
		System.out.println("Autoriser les doublons ? (o/n)");
	
		return scanner.next().equals("o") ? true : false;
	}


	private static int determineBestProportion(Scanner scanner) {
		System.out.println("Best proportion");
		return Integer.parseInt(scanner.nextLine());
	}


	public static Enums.Reduce determineEnum(Scanner scanner)
	{
		Enums.Reduce res;
		String enumQuestion = String.format("Type de reduce \n");
		HashMap<Integer,Enums.Reduce> map = new HashMap<Integer,Enums.Reduce>();
		
		int i = 1;
		for (Enums.Reduce red : Enums.Reduce.values()) {
			map.put(i, red);
			enumQuestion += " - " + i + " : " + red.toString() + "\n";
			i++;
		}
		
		System.out.println(enumQuestion);
		String typeReduce = scanner.nextLine();
		int reduceKey = Integer.parseInt(typeReduce);
		
		return map.get(reduceKey);
	}
	
	public static void lancerSimulation(Enums.Reduce reduceType, int bestProportion, boolean allowDoublons, int generations) throws IOException, RowsExceededException, BiffException, WriteException
	{
		String ds = "D" + allowDoublons;
		String directory = String.format("Files/%s_%s_%s", reduceType.toString(),bestProportion, ds);
		new File(directory).mkdirs();
		
		//fichier global
		String fileName = directory + "/global.xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
		WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		
		//fichier final
		String fileNameFinal = directory + "/final.xls";
		WritableWorkbook workbookFinal = Workbook.createWorkbook(new File(fileNameFinal));
		
		System.out.println("Evolution artificielle");
		
		List<Crepe> enfants = new ArrayList<Crepe>();		
		nbIndividus = 1000;
		nbCoeurs = nbIndividus / 10;
		nbGenerations = generations;
		generationCylcle = nbGenerations / 10;
		generationCylcle = 20;
		List<Crepe> population = PopulationInitiale(nbIndividus);
		List<Coeur> coeurs = InitCoeurs(nbCoeurs);
		
		int indParCoeurs = nbIndividus / nbCoeurs;
		System.out.println("Individus par coeurs : " + indParCoeurs);
		int i = 0;
		for (; i < nbGenerations; i++) {
			
			System.out.println(i + "/" + nbGenerations);
			
			for (int j = 0; j < nbCoeurs; j++) {
				int offset = j * indParCoeurs;
				
				for (int k = 0; k < indParCoeurs; k = k + 2) {
					//System.out.println("Création d'enfant " + (k + offset));
					Crepe parentA = population.get(offset + k);
					Crepe parentB = population.get(offset + k + 1);
					
					enfants.add(coeurs.get(j).renvoieMeilleur(parentA, parentB, i == generationCylcle));
				}
			}
			
			
			population = reduce(enfants, population, reduceType, bestProportion, allowDoublons);
			Collections.shuffle(population);
			ExcelHandler.Write(sheet, population, i);
			Collections.shuffle(population);
			//population = shake(population);
			enfants = new ArrayList<Crepe>();
		}
		
		population.sort(new CrepeComparator());
		Crepe meilleur = population.get(0);
		
		String intro = String.format("La meilleur crepe est : ");
		System.out.println(intro);
		meilleur.read();
		
		ExcelHandler.finalise(workbook);
		
		ExcelHandler.writeFinal(workbookFinal, population);
		ExcelHandler.finalise(workbookFinal);
		System.out.println("Repetoire des résultats : " + directory);
	}
	
	public static List<Crepe> reduce(List<Crepe> enfants, List<Crepe> population, Enums.Reduce param, int bestProportion, boolean allowDoublons)
	{
		//System.out.println("Reduce :" + enfants.size() + " " + population.size());
		List<Crepe> res = new ArrayList<Crepe>();
		
		List<Crepe> popTotal = new ArrayList<Crepe>();
		popTotal.addAll(enfants);
		popTotal.addAll(population);
		
		switch (param) {
			case Elitiste:
				res = Reducer.Elitiste(popTotal, population.size(), bestProportion, allowDoublons);
				break;
			case Aleatoire:
				res = Reducer.Elitiste(popTotal, population.size(), 0, allowDoublons);
				break;
			default:
				break;
		}
		
		return res;
	}
	
	public static List<Crepe> shake(List<Crepe> pop)
	{
		Collections.shuffle(pop);
		return pop;
	}
	
	public static List<Crepe> PopulationInitiale(int nb)
	{
		List<Crepe> res = new ArrayList<Crepe>();
		for (int i = 0; i < nb; i++) {
			res.add(Crepe.Random());
		}
		
		return res;
	}
	
	public static List<Coeur> InitCoeurs(int nb)
	{
		List<Coeur> res = new ArrayList<Coeur>();
		
		for (int i = 0; i < nb; i++) {
			res.add(new Coeur(i));
		}
		
		return res;
	}
}
