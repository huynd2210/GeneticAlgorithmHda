package model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Individual {
  private HPModel hpModel;
  private Double fitness;
  private IndividualInformation individualInformation;

  public Individual(HPModel hpModel) {
    this.hpModel = hpModel;
    this.fitness = 0.0;
    this.individualInformation = new IndividualInformation();
  }

  public Individual(Individual copy) {
    this.hpModel = new HPModel(copy.getHpModel());
    this.fitness = copy.getFitness();
    this.individualInformation = new IndividualInformation(copy.getIndividualInformation());
  }
}
