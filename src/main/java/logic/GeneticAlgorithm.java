package logic;

import model.Individual;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 100;
    private static boolean isRouletteSelection = true;
    private static boolean useDynamicMutationRate = true;
    private static final Random r = new Random();
    private static double MUTATION_RATE = 0.005;
    private static double FINAL_MUTATION_RATE = 0.005;
    private static final int TOURNAMENT_SIZE = 3;

    private GeneticAlgorithm() {

    }

    public static Individual runGeneticAlgorithmWithConfig(Map<String, String> config) throws Exception {
        isRouletteSelection = Boolean.parseBoolean(config.get("isRouletteSelection"));
        useDynamicMutationRate = Boolean.parseBoolean(config.get("useDynamicMutationRate"));
        if (useDynamicMutationRate){
            FINAL_MUTATION_RATE = MUTATION_RATE;
        }
        return runGeneticAlgorithm(config.get("target"));
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
            if (correctnessCheckOverlapping != bestIndividual.getIndividualInformation().getOverlappingAminoAcids().size()) {
                System.out.println("ERROR: Overlapping amino acids are not correct!, " + correctnessCheckOverlapping + " != " + bestIndividual.getIndividualInformation().getOverlappingAminoAcids().size());
            }

            dataLine[5] = Integer.toString(bestIndividual.getIndividualInformation().getOverlappingAminoAcids().size());
            dataLine[6] = currentMostFit.getHpModel().getFolding().getFoldingDirection();
            dataLine[7] = bestIndividual.getHpModel().getFolding().getFoldingDirection();
            dataLine[8] = currentPopulation.stream().collect(StringBuilder::new, (sb, individual) -> sb.append(individual.toString()).append(";"), StringBuilder::append).toString();
            double mutationRate = getMutationRate(i, MAX_GENERATIONS, MUTATION_RATE, FINAL_MUTATION_RATE);
            dataLine[9] = String.valueOf(mutationRate);
//            dataLine[10] = String.valueOf(calculatePopulationDiversity(currentPopulation));
            logs.add(dataLine);

            currentPopulation = evolveNextGeneration(currentPopulation, MUTATION_RATE);
        }
//    return currentPopulation;
        Logger.write("data.csv", logs);
        return bestIndividual;
    }

    private static double findAverageFitnessOfPopulation(List<Individual> population) {
        return population.stream().mapToDouble(Individual::getFitness).average().getAsDouble();
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
//            noMutate(individual, mutationRate);
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
//            nextGeneration.addAll(copyBestCrossover(firstParent, secondParent));
//            nextGeneration.addAll(selectiveCrossover(firstParent, secondParent));
        }
        return nextGeneration;
    }

    private static List<Individual> selectForReproduction(List<Individual> currentGenerationPopulation) throws Exception {
        List<Individual> selectedForReproduction = new ArrayList<>();
        for (int i = 0; i < currentGenerationPopulation.size(); i++) {
            if (isRouletteSelection){
                selectedForReproduction.add(rouletteWheelSelectionAlt(currentGenerationPopulation));
            }else{
                selectedForReproduction.add(tournamentSelection(currentGenerationPopulation));
            }
        }
        return selectedForReproduction;
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

    private static Individual tournamentSelection(List<Individual> population) {
        int tournamentSize = TOURNAMENT_SIZE;
        if (tournamentSize > population.size()){
            tournamentSize = population.size();
        }

        List<Individual> tournamentPopulation = new ArrayList<>(tournamentSize);

        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = (int) (Math.random() * population.size());
            tournamentPopulation.add(population.get(randomIndex));
        }

        return tournamentPopulation.stream().max(Comparator.comparingDouble(Individual::getFitness)).get();
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

        Logic.fold(firstChild.getHpModel());
        Logic.fold(secondChild.getHpModel());
        Logic.evaluateFitness(firstChild);
        Logic.evaluateFitness(secondChild);

        children.add(firstChild);
        children.add(secondChild);
        return children;
    }

    private static List<Individual> selectiveCrossover(Individual firstParent, Individual secondParent){
        Map<Integer, Individual[]> children = evaluateAllOffspring(firstParent, secondParent);
        List<Individual> sortedChildren = children.values().stream().flatMap(Arrays::stream).sorted(Comparator.comparing(Individual::getFitness)).collect(Collectors.toList());
        Collections.reverse(sortedChildren);
        return sortedChildren.subList(1,3);
    }

    private static Map<Integer, Individual[]> evaluateAllOffspring(Individual firstParent, Individual secondParent) {
        Map<Integer, Individual[]> crossoverPointFitnessValueMap = new HashMap<>();
        int crossoverPoint = 0;
        for (int i = 0; i < firstParent.getHpModel().getFolding().getFoldingDirection().length(); i++) {
            crossoverPoint = i;

            Individual firstChild = new Individual(firstParent,
                    firstParent.getHpModel().getFolding().getFoldingDirection().substring(0, crossoverPoint)
                            + secondParent.getHpModel().getFolding().getFoldingDirection().substring(crossoverPoint));
            Individual secondChild = new Individual(secondParent,
                    secondParent.getHpModel().getFolding().getFoldingDirection().substring(0, crossoverPoint)
                            + firstParent.getHpModel().getFolding().getFoldingDirection().substring(crossoverPoint));
            Logic.fold(firstChild.getHpModel());
            Logic.fold(secondChild.getHpModel());
            Logic.evaluateFitness(firstChild);
            Logic.evaluateFitness(secondChild);
            Individual[] children = new Individual[2];
            children[0] = firstChild;
            children[1] = secondChild;
            crossoverPointFitnessValueMap.put(crossoverPoint, children);
        }
        return crossoverPointFitnessValueMap;
    }

    private static List<Individual> copyBestCrossover(Individual firstParent, Individual secondParent) {
        List<Individual> children = new ArrayList<>();
        if (firstParent.getFitness() > secondParent.getFitness()) {
            children.add(new Individual(firstParent));
            children.add(new Individual(firstParent));
        } else {
            children.add(new Individual(secondParent));
            children.add(new Individual(secondParent));
        }
        return children;
    }

    private static void noMutate(Individual individual, double mutationRate) {
        return;
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

    //The choice of putting params here despite having global variables is intentional
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
