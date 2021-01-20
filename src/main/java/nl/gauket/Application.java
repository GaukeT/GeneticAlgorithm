package nl.gauket;

public class Application {

    private static final String target       = "talk is cheap, show me the code.";
    // Size of population
    private static final int    popMax       = 300;
    // Mutation percentage 1% - 100%
    private static final int    mutationRate = 1;

    private static Population population;

    public static void main(String[] args) {
        setup();
        loop();
    }

    private static void setup() {
        // Create a population with the target, mutationRate and max population size.
        population = new Population(target, mutationRate, popMax);
    }

    private static void loop() {
        while (true) {
            // Calculate fitness
            population.calcFitness();
            if (population.isFinished()) break;

            // Perform selection
            population.naturalSelection();
            // Create next generation
            population.generate();
        }
    }
}



