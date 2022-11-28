package logic;

import model.Individual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Logger {


  public static void write(String path, List<String[]> dataLines){
    File csvOutputFile = new File(path);

    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
      dataLines.stream()
              .map(Logger::convertToCSV)
              .forEach(pw::println);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static String convertToCSV(String[] data) {
    return String.join(",", data);
  }



}
