package logic;

import model.AminoAcid;
import model.Folding;
import model.HPModel;
import model.Individual;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;


public class Logic {
  private static final boolean isLogging = true;
  private Logic() {
  }

  public static void evaluateFitness(Individual individual){
    double fitness = 1;
    int numberOfHHBonds = 0;

    List<AminoAcid> hydrophobicAminoAcids = filterForHydrophobicAcids(individual.getHpModel());
//    List<AminoAcid> overlappingAminoAcids = filterForOverlappingAminoAcids(hydrophobicAminoAcids);
    List<List<AminoAcid>> overlappingAminoAcids = filterForOverlappingAminoAcids(individual.getHpModel().getProtein().getProteinChain());
//    List<AminoAcid> overlappingAminoAcids = filterAllAminoAcidsForOverlappingAminoAcids(individual.getHpModel().get);

    for (int i = 0; i < hydrophobicAminoAcids.size() - 1; i++) {
      for (int j = i + 1; j < hydrophobicAminoAcids.size(); j++) {
        if (isValidForEnergyCount(hydrophobicAminoAcids.get(i), hydrophobicAminoAcids.get(j))) {
          numberOfHHBonds += 1;
        }
      }
    }

    individual.getIndividualInformation().setOverlappingAminoAcids(overlappingAminoAcids);
    individual.getIndividualInformation().setNumberOfHHBonds(numberOfHHBonds);

    fitness = fitness + ((double) numberOfHHBonds / (overlappingAminoAcids.size() + 1));
    individual.setFitness(fitness);
  }

  public static double getFitnessOld(HPModel foldedModel) {
    double fitness = 1;
    int numberOfHHBonds = 0;

    List<AminoAcid> hydrophobicAminoAcids = (filterForHydrophobicAcids(foldedModel));
//    List<AminoAcid> overlappingAminoAcids = filterForOverlappingAminoAcids(hydrophobicAminoAcids);
    List<List<AminoAcid>> overlappingAminoAcids = filterForOverlappingAminoAcids(foldedModel.getProtein().getProteinChain());

    for (int i = 0; i < hydrophobicAminoAcids.size() - 1; i++) {
      for (int j = i + 1; j < hydrophobicAminoAcids.size(); j++) {
        if (isValidForEnergyCount(hydrophobicAminoAcids.get(i), hydrophobicAminoAcids.get(j))) {
          numberOfHHBonds += 1;
        }
      }
    }

    if (isLogging) {
      System.out.println("Number of overlapping amino acids: " + overlappingAminoAcids.size());
      System.out.println("Overlapping amino acids: " + overlappingAminoAcids);
      System.out.println("Number of HH bonds: " + numberOfHHBonds);
    }

    fitness = fitness + ((double) numberOfHHBonds / (overlappingAminoAcids.size() + 1));

    return fitness;
  }

  public static void fold(HPModel hpModel) {
    Folding folding = hpModel.getFolding();
    //set first index of the amino acid chain
    hpModel.getProtein().getProteinChain().get(0).setIndex(0);
    for (int i = 0; i < folding.getFoldingDirection().length(); i++) {
      char direction = folding.getFoldingDirection().charAt(i);
      AminoAcid currentAminoAcid = hpModel.getProtein().getProteinChain().get(i + 1);
      AminoAcid previousAminoAcid = hpModel.getProtein().getProteinChain().get(i);
      //set position and index of the amino acid
      currentAminoAcid.setPosition(move(direction, previousAminoAcid.getPosition()));
      currentAminoAcid.setIndex(i + 1);
    }
  }

  private static boolean isValidForEnergyCount(AminoAcid first, AminoAcid second) {
    boolean isAdjacent = isAdjacent(first, second);
    boolean isNeighbor = Math.abs(first.getIndex() - second.getIndex()) == 1;

    boolean valid = isAdjacent && !isNeighbor;
    if (valid && isLogging) {
      System.out.println("Valid pair for fitness score: " + first + " and " + second);
    }

    return valid;
  }

  private static int manhattanDistance(Integer[] first, Integer[] second) {
    return Math.abs(first[0] - second[0]) + Math.abs(first[1] - second[1]);
  }

  private static boolean isAdjacent(AminoAcid first, AminoAcid second) {
    return manhattanDistance(first.getPosition(), second.getPosition()) == 1;
  }

  private static List<AminoAcid> filterForHydrophobicAcids(HPModel foldedModel) {
    List<AminoAcid> hydrophobicAminoAcids = new ArrayList<>();

    for (int i = 0; i < foldedModel.getProtein().getProteinChain().size(); i++) {
      if (foldedModel.getProtein().getProteinChain().get(i).isHydrophob()) {
        hydrophobicAminoAcids.add(foldedModel.getProtein().getProteinChain().get(i));
      }
    }

    return hydrophobicAminoAcids;
  }

  private static int countOverlappings(List<List<AminoAcid>> overlappingAminoAcids){
    //count the number of element in a cartesian product of the inner list
    CombinatoricsUtils.binomialCoefficient(5,2);
    return 0;
  }

  private static List<List<AminoAcid>> filterForOverlappingAminoAcids(List<AminoAcid> allAminoAcids) {
    List<List<AminoAcid>> overlappingAminoAcids = new ArrayList<>();

    Map<Integer, List<AminoAcid>> positionsHashes = new HashMap<>();
    for (AminoAcid aminoAcid : allAminoAcids) {
      int positionHash = hashPosition(aminoAcid.getPosition());
      positionsHashes.computeIfAbsent(positionHash, k -> new ArrayList<>()).add(aminoAcid);

//      if (!positionsHashes.containsKey(positionHash)) {
//        List<AminoAcid> tmp = new ArrayList<>();
//        tmp.add(aminoAcid);
//        positionsHashes.put(positionHash, tmp);
//      }
    }

    for (List<AminoAcid> aminoAcids : positionsHashes.values()) {
      if (aminoAcids.size() > 1){
        for (AminoAcid aminoAcid : aminoAcids) {
          aminoAcid.setOverlapping(true);
        }
        overlappingAminoAcids.add(aminoAcids);
      }
    }

    return overlappingAminoAcids;
  }

  //check for overlappings
//  private static List<AminoAcid> filterForOverlappingAminoAcids(List<AminoAcid> allAminoAcids) {
//    List<AminoAcid> overlappingAminoAcids = new ArrayList<>();
//    Set<Integer> positionsHashes = new HashSet<>();
//    for (AminoAcid hydrophobicAminoAcid : allAminoAcids) {
//      int positionHash = hashPosition(hydrophobicAminoAcid.getPosition());
//      if (!positionsHashes.contains(positionHash)) {
//        positionsHashes.add(positionHash);
//      } else {
//        overlappingAminoAcids.add(hydrophobicAminoAcid);
//        hydrophobicAminoAcid.setOverlapping(true);
//      }
//    }
//    return overlappingAminoAcids;
//  }

  public static Integer hashPosition(Integer[] position) {
    //choosing large prime for less collisions
    final int prime = 773;
    return (position[0] * prime) + position[1];
  }

  private static Integer[] move(char direction, Integer[] position) {
    Integer[] tmp = new Integer[]{position[0], position[1]};
    switch (direction) {
      case 'N':
        tmp[0] -= 1;
        break;
      case 'S':
        tmp[0] += 1;
        break;
      case 'E':
        tmp[1] += 1;
        break;
      case 'W':
        tmp[1] -= 1;
        break;
      default:
        throw new IllegalArgumentException("Invalid direction");
    }
    return tmp;
  }
}
