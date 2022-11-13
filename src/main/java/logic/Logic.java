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
    double fitness = 0;

    List<Pair> indexAminoAcids = new ArrayList<>();
    for (int i = 0; i < foldedModel.getProtein().getProteinChain().size(); i++) {
      if (foldedModel.getProtein().getProteinChain().get(i).isHydrophob()){
        indexAminoAcids.add(new Pair(i, foldedModel.getProtein().getProteinChain().get(i)));
      }
    }

    for (int i = 0; i < indexAminoAcids.size() - 1; i++) {
      for (int j = i + 1; j < indexAminoAcids.size(); j++){
        if (isValidForEnergyCount(indexAminoAcids.get(i), indexAminoAcids.get(j))){
          fitness += 1;
        }
      }
    }
    
    return fitness;
  }

  public static void fold(HPModel hpModel){
    Folding folding = hpModel.getFolding();

    for (int i = 0; i < folding.getFoldingDirection().length(); i++) {
      char direction = folding.getFoldingDirection().charAt(i);
      AminoAcid currentAminoAcid = hpModel.getProtein().getProteinChain().get(i + 1);
      AminoAcid previousAminoAcid = hpModel.getProtein().getProteinChain().get(i);
      currentAminoAcid.setPosition(move(direction, previousAminoAcid.getPosition()));
    }
  }

  private static boolean isValidForEnergyCount(Pair first, Pair second){
    AminoAcid firstAcid = first.getAminoAcid();
    Integer firstIndex = first.getIndex();
    AminoAcid secondAcid = second.getAminoAcid();
    Integer secondIndex = second.getIndex();

    boolean isAdjacent = isAdjacent(firstAcid, secondAcid);
    boolean isNeighbor = Math.abs(firstIndex - secondIndex) == 1;
    return isAdjacent && !isNeighbor;
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
