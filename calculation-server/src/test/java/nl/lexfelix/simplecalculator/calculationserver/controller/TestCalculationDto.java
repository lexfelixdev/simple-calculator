package nl.lexfelix.simplecalculator.calculationserver.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.lexfelix.simplecalculator.calculationserver.controller.dto.CalculationDto;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;

@Data
@AllArgsConstructor
public class TestCalculationDto {
    private CalculationDto calculation;
    private double result;
}
