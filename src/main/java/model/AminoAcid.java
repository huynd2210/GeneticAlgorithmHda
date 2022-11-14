package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class AminoAcid {
  // 0 = hydrophil, "white"
  // 1 = hydrophob, "black"
  private boolean isHydrophob;
  //i,j position, with the first amino acid being at position (0,0)
  private Integer[] position;

  private int index;

  private boolean isOverlapping;

  public AminoAcid(boolean isHydrophob) {
    this.isHydrophob = isHydrophob;
    this.position = new Integer[]{0, 0};
    this.isOverlapping = false;
  }

  public AminoAcid(char type) {
    this.isHydrophob = type == '1';
    this.position = new Integer[]{0, 0};
    this.isOverlapping = false;
  }

  @Override
  public String toString() {
    String type = isHydrophob ? "hydrophob" : "hydrophil";
    return "AminoAcid{" +
            "index=" + index + ", " +
            type +
            ", position=" + Arrays.toString(position) +
            '}';
  }
}
