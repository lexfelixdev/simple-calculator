package nl.lexfelix.simplecalculator.calculationserver.service;

import lombok.RequiredArgsConstructor;
import nl.lexfelix.simplecalculator.calculationserver.calc.SimpleCalculator;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import nl.lexfelix.simplecalculator.calculationserver.repository.CalculationRepository;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final SimpleCalculator simpleCalculator;

    private final CalculationRepository calculationRepository;


    public Calculation calculate(@Valid Calculation calculation) {
        final var calcString = calculation.getCalculation();

        final var calcTokenList = parseCalculationStringIntoTokenList(calcString);
        final var result = calculate(calcTokenList);
        calculation.setResult(result);

        return calculationRepository.save(calculation);
    }

    /**
     * Parses the multiplication and division sub calculations out the the whole calculation
     * and calculates that first to comply with calculation priority rules.
     * After this the addition and subtractions are calculated using the results of the previous calculations
     *
     * @param calcTokenList List of tokens (numbers and operators) which make up the calculation
     * @return Result of the calculation
     */
    private double calculate(List<String> calcTokenList) {
        Deque<String> calculation = new ArrayDeque<>();
        for (int i = 0; i < calcTokenList.size(); i++) {
            var token = calcTokenList.get(i);
            if (isHighPriorityOperator(token)) {
//                Get result of full high priority calculation
                Deque<String> subCalculation = new ArrayDeque<>();
                subCalculation.addFirst(calculation.removeFirst());

                while (token != null && isHighPriorityOperator(token)) {
                    subCalculation.addFirst(token);
                    subCalculation.addFirst(calcTokenList.get(++i));
                    if (i < calcTokenList.size() - 1) {
                        token = calcTokenList.get(++i);
                    } else {
                        token = null;
                    }
                }
                calculation.addFirst(String.valueOf(calculate(subCalculation)));
            } else {
                calculation.addFirst(token);
            }
        }
        return calculate(calculation);
    }

    /**
     * Makes the calculation
     *
     * @param calculation Deque should only contain operators which have the same priority e.g. - or *
     * @return Result of the calculation
     */
    private double calculate(Deque<String> calculation) {
        while (calculation.size() > 1) {
            double b = Double.parseDouble(calculation.removeFirst());
            String operator = calculation.removeFirst();
            double a = Double.parseDouble(calculation.removeFirst());
            final var result = calculate((int) a, (int) b, operator);
            calculation.addFirst(String.valueOf(result));
        }
        return Double.parseDouble(calculation.removeFirst());
    }

    private double calculate(Integer a, Integer b, String operator) {
        switch (operator) {
            case "+":
                return simpleCalculator.add(a, b);
            case "-":
                return simpleCalculator.subtract(a, b);
            case "*":
                return simpleCalculator.multiply(a, b);
            case "/":
                return simpleCalculator.divide(a, b);
            default:
                throw new IllegalArgumentException("Operator not know");
        }
    }

    private boolean isHighPriorityOperator(String token) {
        return token.equals("*") || token.equals("/");
    }

    /**
     * Uses a regex to capture tokens (full numbers, operators) from the give string
     * For now only works with calculations with max 3 numbers and 2 operators
     * The regex group matches only the first 2 and the last 1 because of greedy quantifiers
     *
     * @param calcString calculation string must be a valid string according to the regex
     * @return List of String tokens that can be used in a calculation
     */
    private List<String> parseCalculationStringIntoTokenList(String calcString) {
        List<String> calcTokenList = new LinkedList<>();

        String regexString = Calculation.CALCULATION_REGEX;

        final var regex = Pattern.compile(regexString);
        final var matcher = regex.matcher(calcString);
        while (matcher.find()) {
            final var groupCount = matcher.groupCount();
            for (int i = 1; i <= groupCount; i++) {
                final var regexGroup = matcher.group(i);
                if (regexGroup != null) {
                    calcTokenList.add(regexGroup);
                }
            }
        }
        return calcTokenList;
    }

    public Calculation getCalculationById(Long id) {
        return calculationRepository.findById(id).orElseThrow();
    }

    public List<Calculation> getAllCalculations() {
        return calculationRepository.findAll();
    }
}
