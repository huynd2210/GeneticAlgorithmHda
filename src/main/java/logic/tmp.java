package logic;

public class tmp {
//  package logic;
//
//  public class GeneticAlgorithm {
//    private static final int POPULATION_SIZE = 100;
//    private static final int MAX_GENERATIONS = 1000;
//    private static final double MUTATION_RATE = 0.01;
//    private static final double CROSSOVER_RATE = 0.9;
//    private static final int TOURNAMENT_SELECTION_SIZE = 5;
//    private static final boolean ELITISM = true;
//
//    public static Population evolvePopulation(Population population) {
//      Population newPopulation = new Population(population.size(), false);
//
//      // Keep our best individual if elitism is enabled
//      int elitismOffset = 0;
//      if (ELITISM) {
//        newPopulation.saveIndividual(0, population.getFittest());
//        elitismOffset = 1;
//      }
//
//      // Crossover population
//      // Loop over the new population's size and create individuals from
//      // Current population
//      for (int i = elitismOffset; i < newPopulation.size(); i++) {
//        // Select parents
//        Individual indiv1 = tournamentSelection(population);
//        Individual indiv2 = tournamentSelection(population);
//        // Crossover parents
//        Individual newIndiv = crossover(indiv1, indiv2);
//        // Add new individual to population
//        newPopulation.saveIndividual(i, newIndiv);
//      }
//
//      // Mutate population
//      for (int i = elitismOffset; i < newPopulation.size(); i++) {
//        mutate(newPopulation.getIndividual(i));
//      }
//
//      return newPopulation;
//    }
//
//    // Crossover individuals
//    private static Individual crossover(Individual indiv1, Individual indiv2) {
//      Individual newSol = new Individual();
//      // Loop through genes
//      for (int i = 0; i < indiv1.size(); i++) {
//        // Crossover
//        if (Math.random() <= CROSSOVER_RATE) {
//          newSol.setGene(i, indiv1.getGene(i));
//        } else {
//          newSol.setGene(i, indiv2.getGene(i));
//        }
//      }
//      return newSol;
//    }
//
//    // Mutate an individual
//    private static void mutate(Individual indiv) {
//      // Loop through genes
//      for (int i = 0; i < indiv.size(); i++) {
//        if (Math.random() <= MUTATION_RATE) {
//          // Create random gene
//          byte gene = (byte) Math.round(Math.random());
//          indiv.setGene(i, gene);
//        }
//      }
//    }
//  }
}
