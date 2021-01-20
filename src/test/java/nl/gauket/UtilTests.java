package nl.gauket;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

public class UtilTests {

    @Test
    void random() {
        var list = List.of(
                new Fruit("Apple", 0.5f),
                new Fruit("Mango", 1.5f),
                new Fruit("Kiwi", 3.0f),
                new Fruit("Pear", 5.0f)
        );

        var totalFitness = 0.0f;
        for (var f : list) {
            // fitness score divided by sum of all scores
            totalFitness += f.fitness;
        }

        for (var f : list) {
            // fitness score divided by sum of all scores
            f.normalized = f.fitness / totalFitness;
        }

        for (int i = 0; i < 10_000; i++) {
            var random = randomSelect(list);
            random.score++;
        }

        System.out.println("");
        for (var f : list) {
            System.out.println(f.name + ": \tfitness: " + f.fitness + " | score: " + f.score );
        }
        System.out.println("");
    }

    private Fruit randomSelect(List<Fruit> fruitList) {
        var index = 0;
        var r = new Random().nextFloat();

        while (r > 0) {
            r = r - fruitList.get(index).normalized;
            index++;
        }

        return fruitList.get(--index);
    }

    class Fruit {
        String name;
        float fitness;
        float normalized;
        int score;

        public Fruit(String name, float fitness) {
            this.name = name;
            this.fitness = fitness;
        }
    }
}
