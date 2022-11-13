import logic.Logic;
import model.AminoAcid;
import model.Folding;
import model.HPModel;
import model.Protein;

import java.util.Map;

public class Main {
  public static void test(){
    Protein protein = new Protein(Examples.SEQ7);
    String foldingDirection = "NWSWNNE";
    Folding folding = new Folding(foldingDirection);
    HPModel hpModel = new HPModel(protein, folding);
    Logic.fold(hpModel);
    System.out.println(hpModel);
  }

  public static void main(String[] args) {
//    Protein protein = new Protein(Examples.SEQ20);
//    Folding folding = new Folding("NNNNNNNNNNNNNNNNNNN");
//    HPModel hpModel = new HPModel(protein, folding);
//    Logic.fold(hpModel);
//    System.out.println(hpModel);
    test();
  }
}
