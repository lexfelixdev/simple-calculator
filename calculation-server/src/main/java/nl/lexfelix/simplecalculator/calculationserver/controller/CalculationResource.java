package nl.lexfelix.simplecalculator.calculationserver.controller;

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
public class CalculationResource {

    private final CalculationService calculationService;

    @Autowired
    public CalculationResource(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping
    public ResponseEntity<Void> addCalculation(@Valid @RequestBody CalculationDto calculationDto){
        final var calculate = calculationService.calculate(new Calculation(calculationDto.getCalculationString()));
        return ResponseEntity.created(URI.create("/calculations/" + calculate.getId())).build();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalculationDto> getCalculation(@PathVariable Long id){
        final var calculation = calculationService.getCalculationById(id);
        final var calculationDto = new CalculationDto(calculation.getId(), calculation.getCalculation(), calculation.getResult());
        return ResponseEntity.ok(calculationDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalculationDto>> getCalculations(){
        final var calculationList = calculationService.getAllCalculations();
        final List<CalculationDto> calculationDtoList = new ArrayList<>();
        for (Calculation calculation : calculationList){
            calculationDtoList.add(new CalculationDto(calculation.getId(), calculation.getCalculation(), calculation.getResult()));
        }
        return ResponseEntity.ok(calculationDtoList);
    }
}
