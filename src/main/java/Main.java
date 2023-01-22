import logic.GeneticAlgorithm;
import logic.Logic;
import model.*;
import graphics.GraphicsUtil;
import util.Console;
import util.Examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
  public static void runGA(String proteinSequence) throws Exception {
    Individual individuals = GeneticAlgorithm.runGeneticAlgorithm(proteinSequence);
    GraphicsUtil graphicsUtil = new GraphicsUtil();
    graphicsUtil.drawModel(individuals.getHpModel());
    graphicsUtil.save();
    System.out.println(individuals);
    System.out.println("HH Bonds: " + individuals.getIndividualInformation().getNumberOfHHBonds());
    System.out.println("Number of overlapping amino acids: " + individuals.getIndividualInformation().getOverlappingAminoAcids().size());
    System.out.println("Overlapping Amino Acids: " + individuals.getIndividualInformation().getOverlappingAminoAcids());
  }

  public static void testGA() throws Exception {
    Individual individuals = GeneticAlgorithm.runGeneticAlgorithm(Examples.SEQ24);
    GraphicsUtil graphicsUtil = new GraphicsUtil();
    graphicsUtil.drawModel(individuals.getHpModel());
    graphicsUtil.save();
    System.out.println(individuals);
  }

  public static void test(String sequence, String foldingDirection) {
    Protein protein = new Protein(sequence);
    Folding folding = new Folding(foldingDirection);
    HPModel hpModel = new HPModel(protein, folding);
    Logic.fold(hpModel);

    GraphicsUtil graphics = new GraphicsUtil();
    graphics.drawModel(hpModel);
    graphics.save();

    Individual individuals = new Individual(hpModel);

    System.out.println(hpModel);
    System.out.println(Logic.getFitnessOld(hpModel));
  }

  public static void testHashing() {
    int maxI = 770;
    int maxJ = 770;
    Map<Integer, Integer[]> map = new HashMap<>();
    for (int i = 0; i < maxI; i++) {
      for (int j = 0; j < maxJ; j++) {
        int hash = Logic.hashPosition(new Integer[]{i, j});
        if (map.containsKey(hash)){
          System.out.println("Hash collision: " + hash + " " + i + " " + j + " " + map.get(hash)[0] + " " + map.get(hash)[1]);
        }else{
          map.put(hash, new Integer[]{i, j});
        }
      }
    }
  }

  public static void testOverlapping() {
    test(Examples.SEQ24, "SWSNNEEWEEESWNSEENWSNNN");
  }

  public static void testSampleOverlapping(){
    test(Examples.SEQ7, "NWSWESN");
  }

  public static void testNoOverlapping() {
    test(Examples.SEQ7, "NWSWNNE");
  }

  public static void main(String[] args) throws Exception {
    Console.runMainConsole();
  }

  private static void testIndividualCopyCorrectness() throws Exception {
    Individual individual = GeneticAlgorithm.runGeneticAlgorithm(Examples.SEQ24);
    individual.getIndividualInformation().setOverlappingAminoAcids(List.of(individual.getHpModel().getProtein().getProteinChain()));
    System.out.println(individual);
    Individual tmp = new Individual(individual);
    System.out.println(tmp);
    System.out.println("asdjsd");
  }

  public static void testFilterOverlapping(){
    String foldingDirection = "EENWSES";
    System.out.println(foldingDirection.length());
    HPModel hpModel = new HPModel(new Protein(Examples.SEQ7), new Folding(foldingDirection));
    Logic.fold(hpModel);
    List<List<AminoAcid>> lists = Logic.filterForOverlappingAminoAcids(hpModel.getProtein().getProteinChain());
    System.out.println(lists);
  }
}
