package nl.lexfelix.simplecalculator.calculationserver.calc;

import org.springframework.stereotype.Component;

@Component
public class SimpleCalculator {

    public double add(int a, int b) {
        return Math.addExact(a, b);
    }

    public double subtract(int a, int b) {
        return Math.subtractExact(a, b);
    }

    public double multiply(int a, int b) {
        return Math.multiplyExact(a, b);
    }

    /**
     * Integers are given but because a double is returned
     * The IEEE 754 arithemtic specification is used (https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.17.2)
     * @param numerator Numerator
     * @param denominator Denominator
     * @return Value of the division according to IEEE 754
     */
    public double divide(int numerator, int denominator) {
        return (double) numerator / (double)denominator ;
    }
}
