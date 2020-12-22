package nl.lexfelix.simplecalculator.calculationserver.calc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SimpleTestCalculation {
    private int a;
    private int b;
    private double expectedResult;
}
