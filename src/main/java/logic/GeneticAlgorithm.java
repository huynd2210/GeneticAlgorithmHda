package logic;

import model.Individual;

import java.util.*;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 1000;
    private static final int MAX_GENERATIONS = 100;
    private static final Random r = new Random();
    private static double MUTATION_RATE = 0.01;
    private static double FINAL_MUTATION_RATE = 0.001;

    private GeneticAlgorithm() {

    }

    public static Individual runGeneticAlgorithm(String targetProtein) throws Exception {
        //generate and evaluate initial population
        List<Individual> currentPopulation = initPopulation(targetProtein);
        evaluatePopulationFitness(currentPopulation);
        Individual bestIndividual = findBestFitnessOfPopulation(currentPopulation);

        List<String[]> logs = new ArrayList<>();
        logs.add(new String[]
                {
                        "Generation", "Average fitness", "Best current generation fitness",
                        "Best fitness overall", "H/H Bonds", "Overlapping Amino Acids",
                        "Best current generation folding", "Best Folding", "Population", "Mutation Rate"}
        );
        for (int i = 0; i < MAX_GENERATIONS; i++) {
            evaluatePopulationFitness(currentPopulation);
            String[] dataLine = new String[10];
            dataLine[0] = String.valueOf(i);
            dataLine[1] = String.valueOf(findAverageFitnessOfPopulation(currentPopulation));
            Individual currentMostFit = findBestFitnessOfPopulation(currentPopulation);
            dataLine[2] = String.valueOf(currentMostFit.getFitness());

            if (currentMostFit.getFitness() > bestIndividual.getFitness()) {
                bestIndividual = new Individual(currentMostFit);
//        bestIndividual = currentMostFit;
            }
            dataLine[3] = String.valueOf(bestIndividual.getFitness());
            dataLine[4] = Integer.toString(bestIndividual.getIndividualInformation().getNumberOfHHBonds());

            Individual fitnessCorrectness = new Individual(bestIndividual);
            Logic.evaluateFitness(fitnessCorrectness);
            if (!Objects.equals(fitnessCorrectness.getFitness(), bestIndividual.getFitness())) {
                System.out.println("Fitness is not correct, fitness: " + bestIndividual.getFitness() + " correct fitness: " + fitnessCorrectness.getFitness());
            }


            int correctnessCheckOverlapping = Logic.filterForOverlappingAminoAcids(bestIndividual.getHpModel().getProtein().getProteinChain()).size();
            if (correctnessCheckOverlapping != bestIndividual.getIndividualInformation().getOverlappingAminoAcids().size()){
                System.out.println("ERROR: Overlapping amino acids are not correct!, " + correctnessCheckOverlapping + " != " + bestIndividual.getIndividualInformation().getOverlappingAminoAcids().size());
            }

            dataLine[5] = Integer.toString(bestIndividual.getIndividualInformation().getOverlappingAminoAcids().size());
            dataLine[6] = currentMostFit.getHpModel().getFolding().getFoldingDirection();
            dataLine[7] = bestIndividual.getHpModel().getFolding().getFoldingDirection();
            dataLine[8] = currentPopulation.stream().collect(StringBuilder::new, (sb, individual) -> sb.append(individual.toString()).append(";"), StringBuilder::append).toString();
            double mutationRate = getMutationRate(i, MAX_GENERATIONS, MUTATION_RATE, FINAL_MUTATION_RATE);
            dataLine[9] = String.valueOf(mutationRate);
            logs.add(dataLine);

            currentPopulation = evolveNextGeneration(currentPopulation, i);
        }
//    return currentPopulation;
        Logger.write("data.csv", logs);
        return bestIndividual;
    }

    private static double findAverageFitnessOfPopulation(List<Individual> population) {
        double averageFitness = 0;
        for (Individual individual : population) {
            averageFitness += individual.getFitness();
        }
        return averageFitness / population.size();
    }

    private static Individual findBestFitnessOfPopulation(List<Individual> population) {
        Individual bestIndividual = population.get(0);
        for (Individual individual : population) {
            if (individual.getFitness() > bestIndividual.getFitness()) {
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }

    private static List<Individual> evolveNextGeneration(List<Individual> currentGenerationPopulation, double mutationRate) throws Exception {
        List<Individual> newPopulation = makeOffspring(currentGenerationPopulation);
        for (Individual individual : newPopulation) {
            mutate(individual, mutationRate);
        }
        return newPopulation;
    }

    private static void evaluatePopulationFitness(List<Individual> population) {
        for (Individual individual : population) {
            Logic.fold(individual.getHpModel());
            Logic.evaluateFitness(individual);
        }
    }

    private static List<Individual> makeOffspring(List<Individual> currentGenerationPopulation) throws Exception {
        List<Individual> nextGeneration = new ArrayList<>();
        List<Individual> selectedForReproduction = selectForReproduction(currentGenerationPopulation);
        if (selectedForReproduction.size() % 2 != 0) {
            //if odd number of individuals, add last individual without crossover
            nextGeneration.add(selectedForReproduction.get(selectedForReproduction.size() - 1));
            selectedForReproduction.remove(selectedForReproduction.size() - 1);
        }

        for (int i = 0; i < currentGenerationPopulation.size(); i += 2) {
            Individual firstParent = selectedForReproduction.get(i);
            Individual secondParent = selectedForReproduction.get(i + 1);
            nextGeneration.addAll(crossover(firstParent, secondParent));
        }
        return nextGeneration;
    }

    private static List<Individual> selectForReproduction(List<Individual> currentGenerationPopulation) throws Exception {
        List<Individual> selectedForReproduction = new ArrayList<>();
        for (int i = 0; i < currentGenerationPopulation.size(); i++) {
            selectedForReproduction.add(rouletteWheelSelectionAlt(currentGenerationPopulation));
        }
        return selectedForReproduction;
    }

    public static Individual select(Individual[] population, double[] fitnesses) {
        double totalFitness = 0;
        for (double f : fitnesses) {
            totalFitness += f;
        }
        double[] normalizedFitnesses = new double[fitnesses.length];
        for (int i = 0; i < fitnesses.length; i++) {
            normalizedFitnesses[i] = fitnesses[i] / totalFitness;
        }
        double[] accumulatedFitnesses = new double[normalizedFitnesses.length];
        accumulatedFitnesses[0] = normalizedFitnesses[0];
        for (int i = 1; i < normalizedFitnesses.length; i++) {
            accumulatedFitnesses[i] = accumulatedFitnesses[i - 1] + normalizedFitnesses[i];
        }
        Random r = new Random();
        double rnd = r.nextDouble();
        for (int i = 0; i < accumulatedFitnesses.length; i++) {
            if (rnd < accumulatedFitnesses[i]) {
                return population[i];
            }
        }
        return null;
    }


    private static Individual rouletteWheelSelectionAlt(List<Individual> population) throws Exception {
        double sumFitness = 0;
        for (Individual individual : population) {
            sumFitness += individual.getFitness();
        }
        double[] normalizedFitness = new double[population.size()];
        for (int i = 0; i < population.size(); i++) {
            normalizedFitness[i] = population.get(i).getFitness() / sumFitness;
        }
        double[] accumulatedFitness = new double[population.size()];
        accumulatedFitness[0] = normalizedFitness[0];
        for (int i = 1; i < normalizedFitness.length; i++) {
            accumulatedFitness[i] = accumulatedFitness[i - 1] + normalizedFitness[i];
        }
        Random r = new Random();
        double rnd = r.nextDouble();
        for (int i = 0; i < accumulatedFitness.length; i++) {
            if (rnd < accumulatedFitness[i]) {
                return population.get(i);
            }
        }
        throw new Exception("Roulette wheel selection returns null");
//        return null;
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

    //1-point crossover
    private static List<Individual> crossover(Individual firstParent, Individual secondParent) {
        List<Individual> children = new ArrayList<>();
        int crossoverPoint = r.nextInt(firstParent.getHpModel().getFolding().getFoldingDirection().length());
        Individual firstChild = new Individual(firstParent,
                firstParent.getHpModel().getFolding().getFoldingDirection().substring(0, crossoverPoint)
                        + secondParent.getHpModel().getFolding().getFoldingDirection().substring(crossoverPoint));
        Individual secondChild = new Individual(secondParent,
                secondParent.getHpModel().getFolding().getFoldingDirection().substring(0, crossoverPoint)
                        + firstParent.getHpModel().getFolding().getFoldingDirection().substring(crossoverPoint));
        children.add(firstChild);
        children.add(secondChild);
        return children;
    }

    private static void mutate(Individual individual, double mutationRate) {
        final String direction = "NSEW";
        StringBuilder sb = new StringBuilder(individual.getHpModel().getFolding().getFoldingDirection());
        for (int i = 0; i < sb.length(); i++) {
            if (Math.random() <= mutationRate) {
                char randomChar = direction.charAt((r.nextInt(4)));
                sb.setCharAt(i, randomChar);
            }
        }
        individual.getHpModel().getFolding().setFoldingDirection(sb.toString());
    }

    private static double getMutationRate(int currentGeneration, int maxGeneration, double initialRate, double finalRate) {
        return initialRate + (finalRate - initialRate) * currentGeneration / maxGeneration;
    }

    private static List<Individual> initPopulation(String protein) {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            String foldingDirection = randomizeFolding(protein);
            population.add(new Individual(protein, foldingDirection));
        }
        return population;
    }

    private static String randomizeFolding(String protein) {
        StringBuilder sb = new StringBuilder();
        final String direction = "NSEW";
        for (int i = 0; i < protein.length() - 1; i++) {
            int randomInt = r.nextInt(4);
            sb.append(direction.charAt((randomInt)));
        }
        return sb.toString();
    }
}
