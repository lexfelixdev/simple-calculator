package nl.lexfelix.simplecalculator.calculationserver.service;

import lombok.RequiredArgsConstructor;
import nl.lexfelix.simplecalculator.calculationserver.calc.SimpleCalculator;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import nl.lexfelix.simplecalculator.calculationserver.repository.CalculationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final SimpleCalculator simpleCalculator;

    private final CalculationRepository calculationRepository;

    public List<Calculation> calculate(List<Calculation> calculationList){
        return calculationList.stream().map(this::calculate).collect(Collectors.toList());
    }

    public Calculation calculate(Calculation calculation) {
        final double result;
        switch(calculation.getOperator()){
            case ADD:
                result = simpleCalculator.add(calculation.getA(), calculation.getB());
                break;
            case SUBTRACT:
                result = simpleCalculator.subtract(calculation.getA(), calculation.getB());
                break;
            case MULTIPLY:
                result = simpleCalculator.multiply(calculation.getA(), calculation.getB());
                break;
            case DIVIDE:
                result = simpleCalculator.divide(calculation.getA(), calculation.getB());
                break;
            default:
                throw new IllegalArgumentException("Unknown Operator");
        }
        calculation.setResult(result);
        calculationRepository.save(calculation);
        return calculation;
    }

    public Calculation getCalculationById(Long id) {
        return calculationRepository.findById(id).orElseThrow();
    }

    public List<Calculation> getAllCalculations() {
        return calculationRepository.findAll();
    }
}
