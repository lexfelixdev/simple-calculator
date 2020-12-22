package nl.lexfelix.simplecalculator.calculationserver.service;

import nl.lexfelix.simplecalculator.calculationserver.calc.SimpleCalculator;
import nl.lexfelix.simplecalculator.calculationserver.repository.CalculationRepository;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.OperatorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    public static final long VALID_ID = 1L;
    @Mock
    private SimpleCalculator simpleCalculator;
    @Mock
    private CalculationRepository calculationRepository;

    @Mock
    private Calculation calculation;

    @InjectMocks
    private CalculationService calculationService;

    @Captor
    private ArgumentCaptor<Calculation> calculationArgumentCaptor;

    @BeforeEach
    public void setupSimpleCalculator(){
        lenient().when(simpleCalculator.add(anyInt(), anyInt())).thenAnswer(invocation -> {
            final int a = invocation.getArgument(0);
            final int b = invocation.getArgument(1);
            return (double) Math.addExact(a, b);
        });
        lenient().when(simpleCalculator.subtract(anyInt(), anyInt())).thenAnswer(invocation -> {
            final int a = invocation.getArgument(0);
            final int b = invocation.getArgument(1);
            return (double) Math.subtractExact(a, b);
        });
        lenient().when(simpleCalculator.multiply(anyInt(), anyInt())).thenAnswer(invocation -> {
            final int a = invocation.getArgument(0);
            final int b = invocation.getArgument(1);
            return (double) Math.multiplyExact(a, b);
        });
        lenient().when(simpleCalculator.divide(anyInt(), anyInt())).thenAnswer(invocation -> {
            final int numerator = invocation.getArgument(0);
            final int denominator = invocation.getArgument(1);
            return (double) numerator / (double) denominator ;
        });
    }

    private static Stream<TestCalculation> provideCorrectCalculationDtos(){
        return Stream.of(
                new TestCalculation(new Calculation(1, 2, OperatorType.ADD), 3),
                new TestCalculation(new Calculation(1, -2, OperatorType.ADD), -1),
                new TestCalculation(new Calculation(1, 4, OperatorType.SUBTRACT), -3),
                new TestCalculation(new Calculation(-1, 4, OperatorType.SUBTRACT), -5),
                new TestCalculation(new Calculation(1, 4, OperatorType.MULTIPLY), 4),
                new TestCalculation(new Calculation(1, 4, OperatorType.DIVIDE), 0.25),
                new TestCalculation(new Calculation(4, 0, OperatorType.DIVIDE), Double.POSITIVE_INFINITY),
                new TestCalculation(new Calculation(-4, 0, OperatorType.DIVIDE), Double.NEGATIVE_INFINITY)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCorrectCalculationDtos")
    void calculateShouldReturnCorrectResult(TestCalculation testCalculation){
        when(calculationRepository.save(any())).thenAnswer(invocationOnMock -> {
            final Calculation calculationArgument = invocationOnMock.getArgument(0);
            calculationArgument.setId(1L);
            return calculationArgument;
        });

        final var calculate = calculationService.calculate(testCalculation.getCalculation());

        verify(calculationRepository).save(calculationArgumentCaptor.capture());

        assertThat(calculate.getResult()).isEqualTo(testCalculation.getResult());
        assertThat(calculationArgumentCaptor.getValue()).isEqualTo(calculate);
    }

    @Test
    void calculateWithListShouldReturnCorrectResult(){
        final var testCalculationStream = provideCorrectCalculationDtos();
        final var testCalculationList = provideCorrectCalculationDtos().collect(Collectors.toList());
        final var calculationList = testCalculationStream.map(TestCalculation::getCalculation).collect(Collectors.toList());

        final var calculatedList = calculationService.calculate(calculationList);

        verify(calculationRepository, times(testCalculationList.size())).save(calculationArgumentCaptor.capture());

        assertThat(calculatedList).hasSameSizeAs(testCalculationList);

        for (int i = 0; i < calculatedList.size(); i++) {
            assertThat(calculatedList.get(i).getResult()).isEqualTo(testCalculationList.get(i).getResult());
        }
    }

    @Test
    void getCalculationShouldReturnCalculation(){
        when(calculationRepository.findById(VALID_ID)).thenReturn(Optional.of(calculation));

        final var calculationById = calculationService.getCalculationById(VALID_ID);

        verify(calculationRepository).findById(VALID_ID);

        assertThat(calculationById).isEqualTo(calculation);
    }

    @Test
    void getCalculationShouldThrowNoSuchElementExceptionWhenNotFound(){
        when(calculationRepository.findById(VALID_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->{
            calculationService.getCalculationById(VALID_ID);
        }).isInstanceOf(NoSuchElementException.class);
    }

}