package logic;

import graphics.GraphicsModel;
import graphics.GraphicsNode;
import model.AminoAcid;
import model.HPModel;
import util.Vector2D;

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
            .stream().min(Comparator.comparing(i -> i.getPosition()[1])).get().getPosition()[1]);
  }

  public static int findUpOffset(HPModel foldedModel){
    return Math.abs(foldedModel.getProtein().getProteinChain()
            .stream().min(Comparator.comparing(i -> i.getPosition()[0])).get().getPosition()[0]);
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
      aminoAcid.setPosition(new Integer[]{aminoAcid.getPosition()[0] + upOffset, aminoAcid.getPosition()[1] + leftOffset});
    }
    return normalizedModel;
  }

  public static void sort(HPModel normalizedModel){
    normalizedModel.getProtein().getProteinChain().sort(Comparator.comparing(AminoAcid::getIndex));
  }

  public static GraphicsModel getGraphicsModel(HPModel normalizedModel) {
    GraphicsModel graphicsModel = new GraphicsModel();

    GraphicsNode previousNode = null;

    for (AminoAcid aminoAcid : normalizedModel.getProtein().getProteinChain()) {
      GraphicsNode node = graphicsModel.FindNode(aminoAcid.getPosition());
      if (node != null) {
        node.getAminoAcids().add(aminoAcid);
      } else {
        node = new GraphicsNode();
        node.setPosition(aminoAcid.getPosition());
        node.getAminoAcids().add(aminoAcid);
        graphicsModel.AddNode(node);
      }

      // Handle connections
      if (previousNode != null) {
        node.getConnections().add(new Vector2D(previousNode.getPosition()[0], previousNode.getPosition()[1]));
      }
      previousNode = node;
    }
    return graphicsModel;
  }
}
