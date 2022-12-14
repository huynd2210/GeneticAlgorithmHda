package logic;

import model.AminoAcid;
import model.HPModel;

import java.util.Comparator;

public class GraphicLogic {
  public static int findLeftOffset(HPModel foldedModel){
//    int currentMaxLeftOffset = 0;
//    for (AminoAcid aminoAcid : foldedModel.getProtein().getProteinChain()) {
//      if (aminoAcid.getPosition()[0] < currentMaxLeftOffset) {
//        currentMaxLeftOffset = aminoAcid.getPosition()[0];
//      }
//    }

    return Math.abs(foldedModel.getProtein().getProteinChain()
            .stream().min(Comparator.comparing(i -> i.getPosition()[0])).get().getPosition()[0]);
  }

  public static int findUpOffset(HPModel foldedModel){
    return Math.abs(foldedModel.getProtein().getProteinChain()
            .stream().min(Comparator.comparing(i -> i.getPosition()[1])).get().getPosition()[1]);
  }

  public static int findDownOffset(HPModel foldedModel){
    return Math.abs(foldedModel.getProtein().getProteinChain()
            .stream().max(Comparator.comparing(i -> i.getPosition()[1])).get().getPosition()[1]);
  }

  public static int findRightOffset(HPModel foldedModel){
    return Math.abs(foldedModel.getProtein().getProteinChain()
            .stream().max(Comparator.comparing(i -> i.getPosition()[0])).get().getPosition()[0]);
  }

  public static HPModel normalize(HPModel foldedModel){
    Integer leftOffset = findLeftOffset(foldedModel);
    Integer upOffset = findUpOffset(foldedModel);
    return normalize(foldedModel, leftOffset, upOffset);
  }

  private static HPModel normalize(HPModel foldedModel, Integer leftOffset, Integer upOffset) {
    HPModel normalizedModel = new HPModel(foldedModel);
    for (AminoAcid aminoAcid : normalizedModel.getProtein().getProteinChain()) {
      aminoAcid.setPosition(new Integer[]{aminoAcid.getPosition()[0] + leftOffset, aminoAcid.getPosition()[1] + upOffset});
    }
    return foldedModel;
  }

  public static void sort(HPModel normalizedModel){
    normalizedModel.getProtein().getProteinChain().sort(Comparator.comparing(AminoAcid::getIndex));
  }

}
