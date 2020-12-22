package nl.lexfelix.simplecalculator.calculationserver.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.OperatorType;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationDto {

    @NotNull
    private Integer a;
    @NotNull
    private Integer b;
    @NotNull
    private OperatorType operator;

    private Double result;

    public CalculationDto(@NotNull Integer a, @NotNull Integer b, @NotNull OperatorType operator) {
        this.a = a;
        this.b = b;
        this.operator = operator;
    }
}
