package nl.lexfelix.simplecalculator.calculationserver.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor_ = @JsonCreator)
public class CalculationDto {

    private Long id;

    @NotNull
    @Pattern(regexp = Calculation.CALCULATION_REGEX, message = "Must be a valid calculation")
    private final String calculationString;

    private Double result;

}
