package logic;

import model.AminoAcid;
import model.Folding;
import model.HPModel;

import java.util.ArrayList;
import java.util.List;


public class Logic {
  private Logic() {
  }

  public static double getFitness(HPModel foldedModel){
    double fitness = 1;
    List<AminoAcid> hydrophobicAminoAcids = new ArrayList<>();

    for (int i = 0; i < foldedModel.getProtein().getProteinChain().size(); i++) {
      if (foldedModel.getProtein().getProteinChain().get(i).isHydrophob()){
        hydrophobicAminoAcids.add(foldedModel.getProtein().getProteinChain().get(i));
      }
    }

    for (int i = 0; i < hydrophobicAminoAcids.size() - 1; i++) {
      for (int j = i + 1; j < hydrophobicAminoAcids.size(); j++) {
        if (isValidForEnergyCount(hydrophobicAminoAcids.get(i), hydrophobicAminoAcids.get(j))) {
          fitness += 1;
        }
      }
    }
    return fitness;
  }

  public static void fold(HPModel hpModel){
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

  private static boolean isValidForEnergyCount(AminoAcid first, AminoAcid second){
    boolean isAdjacent = isAdjacent(first, second);
    boolean isNeighbor = Math.abs(first.getIndex() - second.getIndex()) == 1;

    boolean valid = isAdjacent && !isNeighbor;
    if (valid){
      System.out.println("Valid pair for fitness score: " + first + " and " + second);
    }

    return valid;
  }

  private static int manhattanDistance(Integer[] first, Integer[] second){
    return Math.abs(first[0] - second[0]) + Math.abs(first[1] - second[1]);
  }

  private static boolean isAdjacent(AminoAcid first, AminoAcid second){
    return manhattanDistance(first.getPosition(), second.getPosition()) == 1;
  }

  private static Integer[] move(char direction, Integer[] position){
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
