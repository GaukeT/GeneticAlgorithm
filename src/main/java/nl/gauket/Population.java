package nl.gauket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Population {
    private final OPTION partnerPickerOption;
    private int generations = 0;
    private boolean finished = false;
    private final String target;
    private final int mutationRate;
    private final int popMax;
    private DNA[] population;
    private float averageFitness = 0.0f;

    public Population(String target, int mutationRate, int popMax, OPTION partnerPickerOption) {
        this.popMax = popMax;
        this.population = new DNA[popMax];
        this.target = target;
        this.mutationRate = mutationRate;
        this.partnerPickerOption = partnerPickerOption;

        for (int i = 0; i < popMax; i++) {
            this.population[i] = new DNA(target.length());
        }
    }

    public void calcFitness() {
        for (DNA dna : this.population) {
            dna.calcFitness(this.target);
        }

        // Evaluate if target is already found
        this.evaluate();
    }

    public void naturalSelection() {
        // Merged with generateNextGen() method.
    }

    public void generateNextGen() {
        var totalFitness = 0f;
        var maxFitness = 0f;

        for (DNA dna : this.population) {
            totalFitness += dna.getFitness() - 0.01;
            maxFitness = Math.max(dna.getFitness(), maxFitness);
        }

        if (this.partnerPickerOption.value == 2) {
            for (DNA dna : this.population) {
                dna.normalizeFitness(totalFitness);
            }
        }

        this.averageFitness = totalFitness / this.popMax;

        var newPopulation = new DNA[this.popMax];
        for (int i = 0; i < this.popMax; i++) {

            DNA partnerA, partnerB;
            // Option 1: pick new partner based on 1 random index and accept or reject
            if (this.partnerPickerOption.value == 1) {
                partnerA = acceptReject(maxFitness);
                partnerB = acceptReject(maxFitness);
            } else {
                // Option 2: pick new partners based on 1 random index
                partnerA = randomSelect();
                partnerB = randomSelect();
            }

            // Performs crossover of partnerA and partnerB
            var child = partnerA.crossover(partnerB);
            // Mutate child based on mutationRate
            child.mutate(this.mutationRate);

            newPopulation[i] = child;
        }

        population = newPopulation;
        this.generations++;
    }

    private DNA randomSelect() {
        var index = 0;
        var r = new Random().nextFloat();

        while (r > 0) {
            r = r - this.population[index].getNormalizedFitness();
            index++;
        }

        return this.population[--index];
    }

    private DNA acceptReject(float maxFitness) {
        var failsafe = 0;
        while (true) {
            var index = new Random().nextInt(this.popMax);
            var parent = this.population[index];

            // More likely to accept parents with higher fitness
            var r = new Random().nextFloat() * maxFitness;
            if (r < parent.getFitness()) {
                return parent;
            }

            failsafe++;
            // Accept it anyway :)
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

        System.out.println("best phrase: " + best.getPhrase() + " (" + round((best.getFitness() - 0.01f) * 100) + "%) | generation: " + this.generations);

        // If target found or failsafe for amount of generations.
        if (best.getPhrase().equals(target) || (this.generations >= 1_000 && best.getFitness() >= 0.80f)) {
            this.finished = true;
        }
    }

    public boolean isFinished() {
        if (this.finished) {
            System.out.printf("total generations: %d%n", this.generations);
            System.out.println("average fitness: " + round(this.averageFitness * 100) + "%");
            System.out.printf("total population: %d%n", this.popMax);
            System.out.println("mutation rate: " + (this.mutationRate) + "%");
        }

        return this.finished;
    }

    private float round(float f) {
        return BigDecimal.valueOf(f).setScale(2,  RoundingMode.HALF_UP).floatValue();
    }
}
