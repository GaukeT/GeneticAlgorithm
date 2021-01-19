package nl.gauket;

import java.util.Random;

public class Application {

    private static final String target       = "to be or not to be.";
    private static final int    popMax       = 200;
    private static final float  mutationRate = 0.01f;

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
            // Perform selection
            population.naturalSelection();
            // Create next generation
            population.generate();

            population.evaluate();
            if (population.isFinished()) break;
        }
    }
}



