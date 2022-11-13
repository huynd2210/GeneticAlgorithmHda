package logic;

import lombok.Getter;
import model.AminoAcid;

@Getter
public class Pair {
  private Integer index;
  private AminoAcid aminoAcid;

  public Pair(Integer index, AminoAcid aminoAcid) {
    this.index = index;
    this.aminoAcid = aminoAcid;
  }

}
