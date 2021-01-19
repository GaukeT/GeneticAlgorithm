package nl.gauket;

import java.util.Random;

public class Population {
    private int generations = 0;
    private boolean finished = false;
    private String target;
    private float mutationRate;
    private int perfectScore = 1;
    private DNA[] population;
    private String best = "";

    public Population(String target, float mutationRate, int popMax) {
        this.population = new DNA[popMax];
        this.target = target;
        this.mutationRate = mutationRate;

        for (int i = 0; i < popMax; i++) {
            this.population[i] = new DNA(target.length());
        }
    }

    public void calcFitness() {
        for (int i = 0; i < this.population.length; i++) {
            this.population[i].calcFitness(this.target);
        }
    }

    public void naturalSelection() {
        // merged with generate method.
    }

    public void generate() {
        var maxFitness = 0f;
        for (int i = 0; i < this.population.length; i++) {
            maxFitness = Math.max(this.population[i].getFitness(), maxFitness);
        }

        var newPopulation = new DNA[this.population.length];
        for (int i = 0; i < this.population.length; i++) {
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
            var index = new Random().nextInt(this.population.length);
            var parent = this.population[index];

            var r = new Random().nextFloat() * maxFitness;

            if (r < parent.getFitness()) {
                return parent;
            }

            failsafe++;
            // accept it anyway :)
            if (failsafe >= 1000) return parent;
        }
    }

    public void evaluate() {
        var worldRecord = 0.0f;
        var index = 0;

        for (int i = 0; i < this.population.length; i++) {
            if (this.population[i].getFitness() > worldRecord) {
                index = i;
                worldRecord = this.population[i].getFitness();
            }
        }

        this.best = this.population[index].getPhrase();
        if (worldRecord >= perfectScore) this.finished = true;

        System.out.printf("Best phrase: %s%n", this.best);

        // failsafe.
        if (this.generations > 2000) this.finished = true;
    }

    public boolean isFinished() {
        if (this.finished) {
            System.out.printf("total generations: %d%n", this.generations);
            System.out.printf("average fitness: %d%n", 0);
            System.out.printf("total population: %d%n", this.population.length);
            System.out.println("mutation rate: " + (this.mutationRate * 100) + "%");
        }

        return this.finished;
    }
}
