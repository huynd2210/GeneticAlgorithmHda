package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HPModel {
  private final Protein protein;
  private final Folding folding;

  public HPModel(Protein protein, Folding folding) {
    this.protein = protein;
    this.folding = folding;
  }

  @Override
  public String toString() {
    return "HPModel{" +
            "protein=" + protein +
            ", folding=" + folding +
            '}';
  }
}
