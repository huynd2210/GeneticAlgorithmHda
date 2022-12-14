package util;

import logic.GraphicLogic;
import logic.Logic;
import model.AminoAcid;
import model.HPModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphicsUtil {
  private int cellSize = 50;
  private int canvasHeight = 500;
  private int canvasWidth = 800;
  private BufferedImage image;
  private Graphics2D g2;
  private final int labelOffsetI = 4;
  private final int labelOffsetJ = -1;


  public GraphicsUtil(){
    this.init();
  }

  public void drawAtCoord(int i, int j, String label){
//    this.drawSampleGrid(i, j);
    int y = i * cellSize;
    int x = j * cellSize;

    g2.setColor(Color.YELLOW);
    g2.fillRect(x, y, cellSize, cellSize);
    g2.setColor(Color.BLACK);
//    g2.drawString(label, x + (cellSize / 2) + labelOffsetJ, y + (cellSize / 2) + labelOffsetI);
//    drawCellConnection(i, j - 1, i, j);
//    save();
  }

  private void drawLabel(int i, int j, String label){
    int y = i * cellSize;
    int x = j * cellSize;
    g2.setColor(Color.BLACK);
    g2.drawString(label, x + (cellSize / 2) + labelOffsetJ, y + (cellSize / 2) + labelOffsetI);
  }

  public void drawCellConnection(int fromI, int fromJ, int toI, int toJ){
    final int offSetI = 25;
    final int offSetJ = 25;
    int fromY = (fromI * cellSize) + offSetI;
    int fromX = (fromJ * cellSize) + offSetJ;
    int toY = (toI * cellSize) + offSetI;
    int toX = (toJ * cellSize) + offSetJ;
    g2.setColor(Color.RED);
    g2.drawLine(fromX, fromY, toX, toY);
  }

  public void drawSampleGrid(int maxI, int maxJ){
    for (int i = 0; i <= maxI; i++) {
      for (int j = 0; j <= maxJ; j++) {
        g2.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
      }
    }
//    save();
  }

  private void init() {
    image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
    g2 = image.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
  }

  //TODO
  private void drawModel(HPModel foldedModel){
    int maxI = GraphicLogic.findUpOffset(foldedModel) + GraphicLogic.findDownOffset(foldedModel);
    int maxJ = GraphicLogic.findLeftOffset(foldedModel) + GraphicLogic.findRightOffset(foldedModel);

//    int cellSize = Math.min(canvasHeight / maxI, canvasWidth / maxJ);
    HPModel graphic = GraphicLogic.normalize(foldedModel);
    GraphicLogic.sort(graphic);

    Set<Integer> positionSet = new HashSet<>();

    Integer[] previousPosition = null;
    //map contains label at position for drawing label after hpmodel is drawn
    Map<String, String> positionLabelMap = new HashMap<>();

    for (AminoAcid aminoAcid : foldedModel.getProtein().getProteinChain()) {
      Integer[] currentPosition = aminoAcid.getPosition();
      Integer positionHash = Logic.hashPosition(currentPosition);

      if (!positionSet.contains(positionHash)){
        positionSet.add(positionHash);
        drawAtCoord(currentPosition[0], currentPosition[1], Integer.toString(aminoAcid.getIndex()));

        if (previousPosition != null){
          drawCellConnection(previousPosition[0], previousPosition[1], currentPosition[0], currentPosition[1]);
        }
        previousPosition = currentPosition;
      }else{
        //resolve drawing overlaps
//        g2.drawString(Integer.toString(aminoAcid.getIndex()), currentPosition[1] + (cellSize / 2) + labelOffsetJ + , currentPosition[0] + (cellSize / 2) + labelOffsetI);
        positionLabelMap.put(currentPosition.toString(), positionLabelMap.get(currentPosition) + ", " + aminoAcid.getIndex());
      }
    }

  }

  private void drawBackground(Color color) {
    //recommended color
//    Color tmp = new Color(90, 174, 169);
    g2.setColor(color);
    g2.fillRect(0, 0, canvasWidth, canvasHeight);
  }

  private void drawHydrophillicAcid(int x, int y) {
    g2.setColor(Color.WHITE);
    g2.fillRect(x, y, cellSize, cellSize);
  }

  private void drawHydrophobicAcid(int x, int y) {
    g2.setColor(Color.BLACK);
    g2.fillRect(x, y, cellSize, cellSize);
  }

  private void save(){
    String folder = "C:\\Woodchop\\GeneticAlgorithmHda";
    String filename = "image.png";
    if (!new File(folder).exists()) new File(folder).mkdirs();

    try {
      ImageIO.write(image, "png", new File(folder + File.separator + filename));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public void tmp() {
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

    String folder = "C:\\Woodchop\\GeneticAlgorithmHda";
    String filename = "image.png";
    if (!new File(folder).exists()) new File(folder).mkdirs();

    try {
      ImageIO.write(image, "png", new File(folder + File.separator + filename));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

}
