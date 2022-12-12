package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IndividualInformation {
  private List<AminoAcid> overlappingAminoAcids;
  private Integer numberOfHHBonds;

  public IndividualInformation(
          List<AminoAcid> overlappingAminoAcids,
          Integer numberOfHHBonds) {
    this.overlappingAminoAcids = overlappingAminoAcids;
    this.numberOfHHBonds = numberOfHHBonds;
  }

  public IndividualInformation(){
    this.overlappingAminoAcids = new ArrayList<>();
    this.numberOfHHBonds = 0;
  }

  public IndividualInformation(IndividualInformation copy) {
    this.overlappingAminoAcids = new ArrayList<>();
    for (AminoAcid overlappingAminoAcid : copy.getOverlappingAminoAcids()) {
      this.overlappingAminoAcids.add(new AminoAcid(overlappingAminoAcid));
    }
    this.numberOfHHBonds = copy.getNumberOfHHBonds();
  }
}
