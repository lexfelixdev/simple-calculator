package nl.lexfelix.simplecalculator.calculationserver.controller;

import lombok.RequiredArgsConstructor;
import nl.lexfelix.simplecalculator.calculationserver.controller.mapper.CalculationMapper;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import nl.lexfelix.simplecalculator.calculationserver.controller.dto.CalculationDto;
import nl.lexfelix.simplecalculator.calculationserver.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calculations")
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders = {"Location"})
@RequiredArgsConstructor
public class CalculationResource {

    private final CalculationService calculationService;
    private final CalculationMapper calculationMapper;

    @PostMapping
    public ResponseEntity<List<CalculationDto>> addCalculations(@Valid @RequestBody List<CalculationDto> calculationDto){
        List<Calculation> calculation = calculationMapper.toCalculation(calculationDto);
        calculation = calculationService.calculate(calculation);
        return ResponseEntity.ok(calculationMapper.toCalculationDto(calculation));
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalculationDto> getCalculation(@PathVariable Long id){
        final var calculation = calculationService.getCalculationById(id);
        CalculationDto calculationDto = calculationMapper.toCalculationDto(calculation);
        return ResponseEntity.ok(calculationDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalculationDto>> getCalculations(){
        final var calculationList = calculationService.getAllCalculations();
        final List<CalculationDto> calculationDtoList = new ArrayList<>();
        for (Calculation calculation : calculationList){
            calculationDtoList.add(calculationMapper.toCalculationDto(calculation));
        }
        return ResponseEntity.ok(calculationDtoList);
    }
}
