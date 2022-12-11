package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Folding {
  private String foldingDirection;

  public Folding(String foldingDirection) {
    if (this.isValid(foldingDirection)) {
      this.foldingDirection = foldingDirection;
    } else {
      throw new IllegalArgumentException("Invalid folding direction");
    }
  }

  public Folding(Folding copy){
    this.foldingDirection = copy.getFoldingDirection();
  }

  private boolean isValid(String foldingDirection) {
    return foldingDirection.matches("[NSEW]+");
  }
}
