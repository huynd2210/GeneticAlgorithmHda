package util;

import graphics.GraphicsUtil;
import logic.GeneticAlgorithm;
import model.Individual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class Console {
    private Console(){

    }

    private static final Map<String, String> listOfTargetProteins = Map.of(
            "SEQ7", Examples.SEQ7,
            "SEQ12", Examples.SEQ12,
            "SEQ20", Examples.SEQ20,
            "SEQ24", Examples.SEQ24,
            "SEQ25", Examples.SEQ25,
            "SEQ36", Examples.SEQ36,
            "SEQ48", Examples.SEQ48,
            "SEQ50", Examples.SEQ50
    );

    public static void runMainConsole() throws Exception {
        Map<String, String> config = config();
        runGA(config);
    }
    private static void runGA(Map<String, String> config) throws Exception {
        printConfig(config);
        Individual individuals = GeneticAlgorithm.runGeneticAlgorithmWithConfig(config);
        GraphicsUtil graphicsUtil = new GraphicsUtil();
        graphicsUtil.drawModel(individuals.getHpModel());
        graphicsUtil.save();
        System.out.println(individuals);
        System.out.println("HH Bonds: " + individuals.getIndividualInformation().getNumberOfHHBonds());
        System.out.println("Number of overlapping amino acids: " + individuals.getIndividualInformation().getOverlappingAminoAcids().size());
        System.out.println("Overlapping Amino Acids: " + individuals.getIndividualInformation().getOverlappingAminoAcids());
    }
    private static Map<String, String> config(){
        Scanner scanner = new Scanner(System.in);
        String targetProteinToOptimize = "";

        System.out.println("Select protein to optimize");
        System.out.println(listOfTargetProteins.keySet());
        String targetProtein = scanner.nextLine();
        targetProteinToOptimize = listOfTargetProteins.get(targetProtein);
        System.out.println("Use default setting?(y/n)");
        boolean isDefaultSetting = scanner.nextLine().equalsIgnoreCase("y");
        if (isDefaultSetting){
            return getDefaultConfig(targetProteinToOptimize);
        }

        System.out.println("Use roulette selection?(y/n)");
        boolean isRouletteSelection = scanner.nextLine().equalsIgnoreCase("y");
        System.out.println("Use dynamic mutation rate?(y/n)");
        boolean useDynamicMutationRate = scanner.nextLine().equalsIgnoreCase("y");

        Map<String, String> config = new HashMap<>();
        config.put("target", targetProteinToOptimize);
        config.put("isRouletteSelection", String.valueOf(isRouletteSelection));
        config.put("useDynamicMutationRate", String.valueOf(useDynamicMutationRate));
        return config;
    }

    private static Map<String, String> getDefaultConfig(String proteinTarget){
        Map<String, String> defaultConfig = new HashMap<>();
        defaultConfig.put("target", proteinTarget);
        defaultConfig.put("isRouletteSelection", "true");
        defaultConfig.put("useDynamicMutationRate", "true");
        return defaultConfig;
    }

    private static void printConfig(Map<String, String> config){
        System.out.println("Optimizing: SEQ" + config.get("target").length());
        if (config.get("isRouletteSelection").equalsIgnoreCase("true")){
            System.out.println("Using roulette selection");
        }else{
            System.out.println("Using tournament selection");
        }

        if (config.get("useDynamicMutationRate").equalsIgnoreCase("true")){
            System.out.println("Using dynamic mutation rate");
        }else{
            System.out.println("Using static mutation rate");
        }
    }


}
