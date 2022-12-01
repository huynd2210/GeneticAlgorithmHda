package logic;

import model.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
  private static final int POPULATION_SIZE = 100;
  private static final int MAX_GENERATIONS = 100;
  private static final Random r = new Random();

  public static Individual runGeneticAlgorithm(String targetProtein){
    //generate and evaluate initial population
    List<Individual> currentPopulation = initPopulation(targetProtein);
    evaluatePopulationFitness(currentPopulation);
    Individual bestIndividual = findBestFitnessOfPopulation(currentPopulation);

    List<String[]> logs = new ArrayList<>();
    logs.add(new String[]{"Generation", "Average fitness", "Best current fitness", "Best fitness overall", "H/H Bonds", "Overlapping Amino Acids", "Best Folding", "Population"});
    for (int i = 0; i < MAX_GENERATIONS; i++) {
      String[] dataLine = new String[8];
      dataLine[0] = String.valueOf(i);
      dataLine[1] = String.valueOf(findAverageFitnessOfPopulation(currentPopulation));
      Individual currentMostFit = findBestFitnessOfPopulation(currentPopulation);
      dataLine[2] = String.valueOf(currentMostFit.getFitness());

      if (currentMostFit.getFitness() > bestIndividual.getFitness()) {
        bestIndividual = currentMostFit;
      }
      dataLine[3] = String.valueOf(bestIndividual.getFitness());
      dataLine[4] = Integer.toString(bestIndividual.getIndividualInformation().getNumberOfHHBonds());
      dataLine[5] = Integer.toString(bestIndividual.getIndividualInformation().getOverlappingAminoAcids().size());
      dataLine[6] = bestIndividual.getHpModel().getFolding().getFoldingDirection();
      dataLine[7] = currentPopulation.stream().collect(StringBuilder::new, (sb, individual) -> sb.append(individual.toString()).append(";"), StringBuilder::append).toString();
      logs.add(dataLine);

      currentPopulation = evolveNextGeneration(currentPopulation);
    }
//    return currentPopulation;
    Logger.write("C:\\Woodchop\\GeneticAlgorithmHda\\data.csv", logs);
    return bestIndividual;
  }


  private static double findAverageFitnessOfPopulation(List<Individual> population){
    double averageFitness = 0;
    for (Individual individual : population) {
      averageFitness += individual.getFitness();
    }
      return averageFitness / population.size();
  }

  private static Individual findBestFitnessOfPopulation(List<Individual> population){
    Individual bestIndividual = population.get(0);
    for (Individual individual : population) {
      if (individual.getFitness() > bestIndividual.getFitness()) {
        bestIndividual = individual;
      }
    }
    return bestIndividual;
  }

  private static void evaluatePopulationFitness(List<Individual> population) {
    for (Individual individual : population) {
      Logic.fold(individual.getHpModel());
      Logic.evaluateFitness(individual);
    }
  }

  private static List<Individual> evolveNextGeneration(List<Individual> currentGenerationPopulation) {
    List<Individual> newPopulation = new ArrayList<>();
    evaluatePopulationFitness(currentGenerationPopulation);
    for (int i = 0; i < currentGenerationPopulation.size(); i++) {
      makeOffspring(newPopulation, currentGenerationPopulation);
    }
    //do mutation here

    //sum of currentGenerationPopulation
//    double currentGenSum = currentGenerationPopulation.stream().mapToDouble(Individual::getFitness).sum();
//    double newGenSum = newPopulation.stream().mapToDouble(Individual::getFitness).sum();

    return newPopulation;
  }

  private static void makeOffspring(List<Individual> newPopulation, List<Individual> oldPopulation){
    Individual firstParent = rouletteWheelSelection(oldPopulation);
//    Individual secondParent = rouletteWheelSelection(oldPopulation);
//    newPopulation.addAll(crossover(firstParent, secondParent));
    newPopulation.add(firstParent);
  }

  private static Individual rouletteWheelSelection(List<Individual> population) {
    double totalFitness = 0;
    for (Individual individual : population) {
      totalFitness += individual.getFitness();
    }

    //generate a random number in proportional to the total fitness
    //e.g population fitness is [4,2,3,1], total fitness is 10
    //the individual with a higher fitness is more likely to reduce the
    //random number down to <= 0, and therefore more likely to be selected
    double random = Math.random() * totalFitness;
    //shuffle the population
    Collections.shuffle(population);
    for (Individual individual : population) {
      random -= individual.getFitness();
      if (random <= 0) {
        return individual;
      }
    }
    return null;
  }

//  private static List<Individual> crossover(Individual firstParent, Individual secondParent){
    //no crossover for now
//    List<Individual> children = new ArrayList<>();
//    children.add(firstParent);
//    children.add(secondParent);
//    return children;
    //return a random parent
//    return Math.random() < 0.5 ? List.of(firstParent) : List.of(secondParent);
//  }
  private static String randomizeFolding(String protein){
    StringBuilder sb = new StringBuilder();
    final String direction = "NSEW";
    for (int i = 0; i < protein.length() - 1; i++) {
      int randomInt = r.nextInt(4);
      sb.append(direction.charAt((randomInt)));
    }
    return sb.toString();
  }

  private static List<Individual> initPopulation(String protein){
    List<Individual> population = new ArrayList<>();
    for (int i = 0; i < POPULATION_SIZE; i++) {
      String foldingDirection = randomizeFolding(protein);
      population.add(new Individual(protein, foldingDirection));
    }
    return population;
  }

}