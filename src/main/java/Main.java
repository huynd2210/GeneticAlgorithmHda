import logic.GeneticAlgorithm;
import logic.Logic;
import model.Folding;
import model.HPModel;
import model.Individual;
import model.Protein;
import graphics.GraphicsUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Main {
  public static void runGA(String proteinSequence){
    Individual individuals = GeneticAlgorithm.runGeneticAlgorithm(proteinSequence);
    GraphicsUtil graphicsUtil = new GraphicsUtil();
    graphicsUtil.drawModel(individuals.getHpModel());
    graphicsUtil.save();
    System.out.println(individuals);
    System.out.println("HH Bonds: " + individuals.getIndividualInformation().getNumberOfHHBonds());
    System.out.println("Number of overlapping amino acids: " + individuals.getIndividualInformation().getOverlappingAminoAcids().size());
    System.out.println("Overlapping Amino Acids: " + individuals.getIndividualInformation().getOverlappingAminoAcids());
  }

  public static void testGA(){
    Individual individuals = GeneticAlgorithm.runGeneticAlgorithm(Examples.SEQ50);
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



  public static void runGraphicExample() {

    int height = 500;
    int width = 800;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = image.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g2.setColor(Color.YELLOW);
    g2.fillRect(0, 0, width, height);

    int cellSize = 50;

    g2.setColor(new Color(0, 200, 0));
    g2.fillRect(50, 50, cellSize, cellSize);

    g2.setColor(new Color(255, 0, 0));
    g2.fillRect(250, 50, cellSize, cellSize);

    g2.setColor(Color.BLACK);
    g2.drawLine(50 + cellSize, 50 + cellSize / 2, 250, 50 + cellSize / 2);

    g2.setColor(new Color(255, 255, 255));
    String label = "GA";
    Font font = new Font("Serif", Font.PLAIN, 40);
    g2.setFont(font);
    FontMetrics metrics = g2.getFontMetrics();
    int ascent = metrics.getAscent();
    int labelWidth = metrics.stringWidth(label);

    g2.drawString(label, 50 + cellSize / 2 - labelWidth / 2, 50 + cellSize / 2 + ascent / 2);

    String folder = ".";
    String filename = "sample graphic.png";
    if (new File(folder).exists() == false) new File(folder).mkdirs();

    try {
      ImageIO.write(image, "png", new File(folder + File.separator + filename));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    }


  }

  public static void main(String[] args) {
//    testNoOverlapping();
//    testOverlapping();
//    testHashing();
//    runGraphicExample();

//    testSampleOverlapping();
    runGA(Examples.SEQ24);




  }
}
