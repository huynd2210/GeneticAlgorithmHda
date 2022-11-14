import logic.Logic;
import model.Folding;
import model.HPModel;
import model.Protein;

public class Main {

  public static void test(String sequence, String foldingDirection){
    Protein protein = new Protein(sequence);
    Folding folding = new Folding(foldingDirection);
    HPModel hpModel = new HPModel(protein, folding);
    Logic.fold(hpModel);
    System.out.println(hpModel);
    System.out.println(Logic.getFitness(hpModel));
  }

  public static void testOverlapping(){
    test(Examples.SEQ7, "NWSSWNE");
  }

  public static void testNoOverlapping() {
    test(Examples.SEQ7, "NWSWNNE");
  }

  public static void main(String[] args) {
//    Protein protein = new Protein(Examples.SEQ20);
//    Folding folding = new Folding("NNNNNNNNNNNNNNNNNNN");
//    HPModel hpModel = new HPModel(protein, folding);
//    Logic.fold(hpModel);
//    System.out.println(hpModel);
    testNoOverlapping();
    testOverlapping();
  }
}
