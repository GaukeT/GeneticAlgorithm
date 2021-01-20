package nl.gauket;

import java.util.Random;

public class DNA {
    private final char[] genes;
    private float fitness;

    public DNA(int length) {
        this.genes = new char[length];

        for (int i = 0; i < length; i++) {
            this.genes[i] = newChar();
        }
    }

    public void calcFitness(String target) {
        var score = 0f;
        for (int i = 0; i < this.genes.length; i++) {
            if (this.genes[i] == target.charAt(i)) score++;
        }

        this.fitness = score / target.length();
        this.fitness = (float) Math.pow(this.fitness, 2) + 0.01f;
    }

    public DNA crossover(DNA partner) {
        var child = new DNA(this.genes.length);

        var midPoint = new Random().nextInt(this.genes.length);

        for (int i = 0; i < this.genes.length; i++) {
            if (i > midPoint) child.genes[i] = this.genes[i];
            else child.genes[i] = partner.genes[i];
        }

        return child;
    }

    public void mutate(int mutationRate) {
        for (int i = 0; i < this.genes.length; i++) {
            if (new Random().nextInt(100) < mutationRate) {
                this.genes[i] = newChar();
            }
        }
    }

    private char newChar() {
        var rd = new Random();
        var alphabet = "abcdefghijklmnopqrstuvwxyz., ";
        return alphabet.charAt(rd.nextInt(alphabet.length()));
    }

    public String getPhrase() {
        return new String(this.genes);
    }

    public float getFitness() {
        return fitness;
    }
}
