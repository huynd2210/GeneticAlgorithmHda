package model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Folding {
  private final String foldingDirection;

  public Folding(String foldingDirection) {
    if (this.isValid(foldingDirection)) {
      this.foldingDirection = foldingDirection;
    } else {
      throw new IllegalArgumentException("Invalid folding direction");
    }
  }

  private boolean isValid(String foldingDirection) {
    return foldingDirection.matches("[NSEW]+");
  }
}
