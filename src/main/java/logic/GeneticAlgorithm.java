package logic;

import model.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneticAlgorithm {
  private static final int POPULATION_SIZE = 100;
  private static final int MAX_GENERATIONS = 100;

  private static List<Individual> initPopulation(String protein){
    return null;
  }

  private static void evaluatePopulationFitness(List<Individual> population) {
    for (Individual individual : population) {
      Logic.fold(individual.getHpModel());
      Logic.evaluateFitness(individual);
    }
  }

  private static List<Individual> evolveNextGeneration(List<Individual> currentGenerationPopulation) {
    List<Individual> newPopulation = new ArrayList<>(currentGenerationPopulation.size());
    evaluatePopulationFitness(currentGenerationPopulation);
    for (int i = 0; i < currentGenerationPopulation.size(); i++) {
      makeOffspring(newPopulation, currentGenerationPopulation);
    }
    //do mutation here
    return currentGenerationPopulation;
  }

  private static void makeOffspring(List<Individual> newPopulation, List<Individual> oldPopulation){
    Individual firstParent = rouletteWheelSelection(oldPopulation);
    Individual secondParent = rouletteWheelSelection(oldPopulation);
    newPopulation.addAll(crossover(firstParent, secondParent));
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

  private static List<Individual> crossover(Individual firstParent, Individual secondParent){
    //no crossover for now
//    List<Individual> children = new ArrayList<>();
//    children.add(firstParent);
//    children.add(secondParent);
//    return children;
    //return a random parent
    return Math.random() < 0.5 ? List.of(firstParent) : List.of(secondParent);
  }
}