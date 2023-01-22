import logic.Logic;
import model.AminoAcid;
import model.Folding;
import model.HPModel;
import model.Protein;
import org.junit.jupiter.api.Test;
import util.Examples;

import java.util.List;

public class LogicTest {
    @Test
    public static void testFilterOverlapping(){
        HPModel hpModel = new HPModel(new Protein(Examples.SEQ24), new Folding("ESSWNSWWNENWNWSWSSESWWNN"));
        Logic.fold(hpModel);
        List<List<AminoAcid>> lists = Logic.filterForOverlappingAminoAcids(hpModel.getProtein().getProteinChain());
        System.out.println(lists);
    }
}
