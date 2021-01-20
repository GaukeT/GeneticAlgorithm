package nl.gauket;

import java.util.Random;

public class Population {
    private int generations = 0;
    private boolean finished = false;
    private final String target;
    private final int mutationRate;
    private final int popMax;
    private DNA[] population;
    private float averageFitness = 0.0f;

    public Population(String target, int mutationRate, int popMax) {
        this.popMax = popMax;
        this.population = new DNA[popMax];
        this.target = target;
        this.mutationRate = mutationRate;

        for (int i = 0; i < popMax; i++) {
            this.population[i] = new DNA(target.length());
        }
    }

    public void calcFitness() {
        for (DNA dna : this.population) {
            dna.calcFitness(this.target);
        }

        // evaluate if all best is found
        this.evaluate();
    }

    public void naturalSelection() {
        // merged with generate method.
    }

    public void generate() {
        var totalFitness = 0f;
        var maxFitness = 0f;
        for (DNA dna : this.population) {
            totalFitness += dna.getFitness() - 0.01;
            maxFitness = Math.max(dna.getFitness(), maxFitness);
        }

        this.averageFitness = totalFitness / (this.popMax * 2);

        var newPopulation = new DNA[this.popMax];
        for (int i = 0; i < this.popMax; i++) {
            var partnerA = acceptReject(maxFitness);
            var partnerB = acceptReject(maxFitness);

            // perform crossover of partnerA and partnerB
            var child = partnerA.crossover(partnerB);
            // mutateChild based on mutationRate
            child.mutate(this.mutationRate);

            newPopulation[i] = child;
        }

        population = newPopulation;
        this.generations++;
    }

    private DNA acceptReject(float maxFitness) {
        var failsafe = 0;
        while (true) {
            var index = new Random().nextInt(this.popMax);
            var parent = this.population[index];

            // more likely to accept parents with higher fitness
            var r = new Random().nextFloat() * maxFitness;
            if (r < parent.getFitness()) {
                return parent;
            }

            failsafe++;
            // accept it anyway :)
            if (failsafe >= (this.popMax * 10)) return parent;
        }
    }

    public void evaluate() {
        var maxFitness = 0f;
        var best = new DNA(0);

        for (DNA dna : this.population) {
            if (dna.getFitness() > maxFitness) {
                maxFitness = dna.getFitness();
                best = dna;
            }
        }

        System.out.println("Best phrase: " + best.getPhrase() + " (" + ((best.getFitness()) / 2 * 100) + ")");

        // if target found or failsafe for amount of generations.
        if (best.getPhrase().equals(target) || this.generations >= 1000) {
            this.finished = true;
        }
    }

    public boolean isFinished() {
        if (this.finished) {
            System.out.printf("total generations: %d%n", this.generations);
            System.out.println("average fitness: " + (this.averageFitness * 100) + "%");
            System.out.printf("total population: %d%n", this.popMax);
            System.out.println("mutation rate: " + (this.mutationRate) + "%");
        }

        return this.finished;
    }
}
