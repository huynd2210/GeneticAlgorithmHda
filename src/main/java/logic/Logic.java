package logic;

import model.AminoAcid;
import model.Folding;
import model.HPModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Logic {
  private Logic() {
  }

  public static double getFitness(HPModel foldedModel) {
    double fitness = 1;
    int numberOfHHBonds = 0;
    List<AminoAcid> hydrophobicAminoAcids = (filterForHydrophobicAcids(foldedModel));
    List<AminoAcid> overlappingAminoAcids = filterForOverlappingAminoAcids(hydrophobicAminoAcids);
    System.out.println("Number of overlapping amino acids: " + overlappingAminoAcids.size());
    System.out.println("Overlapping amino acids: " + overlappingAminoAcids);
    for (int i = 0; i < hydrophobicAminoAcids.size() - 1; i++) {
      for (int j = i + 1; j < hydrophobicAminoAcids.size(); j++) {
        if (isValidForEnergyCount(hydrophobicAminoAcids.get(i), hydrophobicAminoAcids.get(j))) {
          numberOfHHBonds += 1;
        }
      }
    }
    System.out.println("Number of HH bonds: " + numberOfHHBonds);

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
    if (valid) {
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

  private static List<AminoAcid> filterForOverlappingAminoAcids(List<AminoAcid> hydrophobicAminoAcids) {
    List<AminoAcid> overlappingAminoAcids = new ArrayList<>();
    Set<Integer> positionsHashes = new HashSet<>();
    for (AminoAcid hydrophobicAminoAcid : hydrophobicAminoAcids) {
      int positionHash = hashPosition(hydrophobicAminoAcid.getPosition());
      if (!positionsHashes.contains(positionHash)) {
        positionsHashes.add(positionHash);
      } else {
        overlappingAminoAcids.add(hydrophobicAminoAcid);
        hydrophobicAminoAcid.setOverlapping(true);
      }
    }
    return overlappingAminoAcids;
  }

  private static Integer hashPosition(Integer[] position) {
    int prime = 31;
    return position[0] * prime + position[1];
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
