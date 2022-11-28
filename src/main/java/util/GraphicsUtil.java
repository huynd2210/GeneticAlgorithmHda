package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphicsUtil {
  private int cellSize = 80;
  private int canvasHeight = 500;
  private int canvasWidth = 800;
  private BufferedImage image;
  private Graphics2D g2;


  private void init() {
    image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
    g2 = image.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
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
