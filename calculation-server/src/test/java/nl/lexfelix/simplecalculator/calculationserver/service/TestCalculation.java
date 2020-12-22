package nl.lexfelix.simplecalculator.calculationserver.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;

@Data
@AllArgsConstructor
public class TestCalculation {
    private Calculation calculation;
    private double result;
}
