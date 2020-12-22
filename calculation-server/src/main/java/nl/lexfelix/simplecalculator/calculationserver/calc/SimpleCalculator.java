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
     * Although Double has support for Infinity Values the database can't store them so it throws ArithmeticException
     * @param numerator Numerator
     * @param denominator Denominator
     * @return Value of the division
     */
    public double divide(int numerator, int denominator) {
        final var result = (double) numerator / (double) denominator;
        if(!Double.isFinite(result)){
            throw new ArithmeticException("Division by Zero is not supported");
        }
        return result;
    }
}
