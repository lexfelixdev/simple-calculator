package nl.lexfelix.simplecalculator.calculationserver.calc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class SimpleCalculatorTest {

    @InjectMocks
    private SimpleCalculator simpleCalculator;

    private static Stream<SimpleTestCalculation> provideTestCalculationsForAddTest(){
        return Stream.of(
                new SimpleTestCalculation(1,3, 4),
                new SimpleTestCalculation(3,1,4),
                new SimpleTestCalculation(1,0, 1),
                new SimpleTestCalculation(-7,-2, -9),
                new SimpleTestCalculation(-7,2,-5),
                new SimpleTestCalculation(7, -2, 5)
        );
    }
    private static Stream<SimpleTestCalculation> provideTestCalculationsForSubtractTest(){
        return Stream.of(
                new SimpleTestCalculation(1,3, -2),
                new SimpleTestCalculation(3,1,2),
                new SimpleTestCalculation(1,0, 1),
                new SimpleTestCalculation(0,1, -1),
                new SimpleTestCalculation(-7,-2, -5),
                new SimpleTestCalculation(-7,2,-9),
                new SimpleTestCalculation(7, -2, 9)
        );
    }
    private static Stream<SimpleTestCalculation> provideTestCalculationsForMultiplyTest(){
        return Stream.of(
                new SimpleTestCalculation(1,3, 3),
                new SimpleTestCalculation(2,3,6),
                new SimpleTestCalculation(2,0,0),
                new SimpleTestCalculation(0,2,0),
                new SimpleTestCalculation(-7,-2, 14),
                new SimpleTestCalculation(-7,2,-14),
                new SimpleTestCalculation(7, -2, -14)
        );
    }
    private static Stream<SimpleTestCalculation> provideTestCalculationsForDivideTest(){
        return Stream.of(
                new SimpleTestCalculation(1,3, 0.3333333333333333),
                new SimpleTestCalculation(2,3,0.6666666666666666),
                new SimpleTestCalculation(0,2,0),
                new SimpleTestCalculation(-7,-2, 3.5),
                new SimpleTestCalculation(-7,2,-3.5),
                new SimpleTestCalculation(7, -2, -3.5),
                new SimpleTestCalculation(1, 0, Double.POSITIVE_INFINITY),
                new SimpleTestCalculation(-1, 0, Double.NEGATIVE_INFINITY)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCalculationsForAddTest")
    void addingShouldCalculateCorrectlyGiven(SimpleTestCalculation simpleTestCalculation){
        final var result = simpleCalculator.add(simpleTestCalculation.getA(), simpleTestCalculation.getB());
        assertThat(result).isEqualTo(simpleTestCalculation.getExpectedResult());
    }

    @ParameterizedTest
    @MethodSource("provideTestCalculationsForSubtractTest")
    void substractingShouldCalculateCorrectlyGiven(SimpleTestCalculation simpleTestCalculation){
        final var result = simpleCalculator.subtract(simpleTestCalculation.getA(), simpleTestCalculation.getB());
        assertThat(result).isEqualTo(simpleTestCalculation.getExpectedResult());
    }

    @ParameterizedTest
    @MethodSource("provideTestCalculationsForMultiplyTest")
    void multiplyingShouldCalculateCorrectlyGiven(SimpleTestCalculation simpleTestCalculation){
        final var result = simpleCalculator.multiply(simpleTestCalculation.getA(), simpleTestCalculation.getB());
        assertThat(result).isEqualTo(simpleTestCalculation.getExpectedResult());
    }

    @ParameterizedTest
    @MethodSource("provideTestCalculationsForDivideTest")
    void dividingShouldCalculateCorrectlyGiven(SimpleTestCalculation simpleTestCalculation){
        final var result = simpleCalculator.divide(simpleTestCalculation.getA(), simpleTestCalculation.getB());
        assertThat(result).isEqualTo(simpleTestCalculation.getExpectedResult());
    }

    @Test
    void addingShouldThrowArithemticExceptionWhenIntegerOverflows(){
        assertThatThrownBy(() -> {
            simpleCalculator.add(Integer.MAX_VALUE, 1);
        }).isInstanceOf(ArithmeticException.class);
    }
    @Test
    void addingShouldThrowArithemticExceptionWhenIntegerUnderflows(){
        assertThatThrownBy(() -> {
            simpleCalculator.add(Integer.MIN_VALUE, -1);
        }).isInstanceOf(ArithmeticException.class);
    }

    @Test
    void subtractingShouldThrowArithemticExceptionWhenIntegerOverflows(){
        assertThatThrownBy(() -> {
            simpleCalculator.subtract(Integer.MIN_VALUE, 1);
        }).isInstanceOf(ArithmeticException.class);
    }
    @Test
    void substractingShouldThrowArithemticExceptionWhenIntegerUnderflows(){
        assertThatThrownBy(() -> {
            simpleCalculator.subtract(Integer.MAX_VALUE, -1);
        }).isInstanceOf(ArithmeticException.class);
    }

    @Test
    void multiplyingShouldThrowArithemticExceptionWhenIntegerOverflows(){
        assertThatThrownBy(() -> {
            simpleCalculator.multiply(Integer.MAX_VALUE, 2);
        }).isInstanceOf(ArithmeticException.class);
    }
    @Test
    void multiplyingShouldThrowArithemticExceptionWhenIntegerUnderflows(){
        assertThatThrownBy(() -> {
            simpleCalculator.multiply(Integer.MIN_VALUE, 2);
        }).isInstanceOf(ArithmeticException.class);
    }

}