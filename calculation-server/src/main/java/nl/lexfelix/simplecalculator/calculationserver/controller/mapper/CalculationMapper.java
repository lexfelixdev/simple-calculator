package nl.lexfelix.simplecalculator.calculationserver.controller.mapper;

import nl.lexfelix.simplecalculator.calculationserver.controller.dto.CalculationDto;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CalculationMapper {

    List<CalculationDto> toCalculationDto(List<Calculation> calculation);
    CalculationDto toCalculationDto(Calculation calculation);


    List<Calculation> toCalculation(List<CalculationDto> calculation);

    @Mapping(target ="id", ignore = true)
    Calculation toCalculation(CalculationDto calculation);
}
