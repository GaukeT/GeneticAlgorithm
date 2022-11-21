package nl.gauket;

import org.junit.jupiter.api.Test;

import java.util.Vector;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class MethodSwitchTests {

    @Test
    void p() {
        BiPredicate<Integer, Integer> sum = (x, y) -> x <= y;
        BiPredicate<Integer, Integer> add = (x, y) -> x >= y;

        Vector<Integer> v = new Vector<>();

        if (mapP(sum, 2, 2)) System.out.println("true");
        if (mapP(add, 5, 3)) System.out.println("false");
    }

    static <T> boolean mapP(BiPredicate<T, T> p, T x, T y) {
        return p.test(x, y);
    }

    @Test
    void f() {
        BiFunction<Integer, Integer, Integer> sum = (x, y) -> y + x;
        BiFunction<Integer, Integer, Integer> mult = (x, y) -> y * x;

        var result = mapF(sum, 2, 6);
        var result1 = mapF(mult, 5, 4);

        System.out.println(result);
        System.out.println(result1);
    }

    static <T> T mapF(BiFunction<T, T, T> f, T a, T b) {
        return f.apply(a, b);
    }
}
