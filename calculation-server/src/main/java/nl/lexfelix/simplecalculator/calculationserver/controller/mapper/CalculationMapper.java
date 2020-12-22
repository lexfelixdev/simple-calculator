package nl.lexfelix.simplecalculator.calculationserver.controller.mapper;

import nl.lexfelix.simplecalculator.calculationserver.controller.dto.CalculationDto;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalculationMapper {

    CalculationDto toCalculationDto(Calculation calculation);

    @Mapping(target ="id", ignore = true)
    Calculation toCalculation(CalculationDto calculation);
}
