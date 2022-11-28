package model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Protein {
  private List<AminoAcid> proteinChain;

  public Protein(String sequence) {
    proteinChain = new ArrayList<>();
    for (char item : sequence.toCharArray()) {
      proteinChain.add(new AminoAcid(item));
    }
  }

  public Protein(Protein copy){
    proteinChain = new ArrayList<>();
    for (AminoAcid aminoAcid : copy.getProteinChain()) {
      proteinChain.add(new AminoAcid(aminoAcid));
    }
  }


}
