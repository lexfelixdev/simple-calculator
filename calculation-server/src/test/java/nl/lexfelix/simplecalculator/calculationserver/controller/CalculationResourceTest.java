package nl.lexfelix.simplecalculator.calculationserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import nl.lexfelix.simplecalculator.calculationserver.CalculationServerApplication;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import nl.lexfelix.simplecalculator.calculationserver.controller.dto.CalculationDto;
import nl.lexfelix.simplecalculator.calculationserver.repository.CalculationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CalculationServerApplication.class)
@AutoConfigureMockMvc
@Transactional
class CalculationResourceTest {

    public static final long VALID_ID = 1L;
    public static final String ADD_CALCULATION_PATH = "/calculations";
    private static final String GET_CALCULATION_PATH = "/calculations";


    @Captor
    private ArgumentCaptor<Calculation> calculationArgumentCaptor;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CalculationRepository calculationRepository;


    private static Stream<CalculationDto> provideCorrectCalculationDtos(){
        return Stream.of(
                new CalculationDto("1+2"),
                new CalculationDto("1+-2"),
                new CalculationDto("1-4"),
                new CalculationDto("-1-4"),
                new CalculationDto("1*4"),
                new CalculationDto("1*4/2"),
                new CalculationDto("1/4"),
                new CalculationDto("1+2+3"),
                new CalculationDto("11+11")
        );
    }
    private static Stream<CalculationDto> provideIncorrectCalculationDtos(){
        return Stream.of(
                new CalculationDto("1++2"),
                new CalculationDto("01-4"),
                new CalculationDto("1*04"),
                new CalculationDto("1+2+3+4+5+6+7+9+00"),
                new CalculationDto(""),
                new CalculationDto("klajnsdf"),
                new CalculationDto("1+2+dfdf")

        );
    }


    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideCorrectCalculationDtos")
    void addingCalculationShouldCallCalculationServiceWithValuesFromCalculationDto(CalculationDto calculationDto){
        final var calculationDtoJsonString= objectMapper.writeValueAsString(calculationDto);
        final var mvcResult = mockMvc.perform(
                post(ADD_CALCULATION_PATH)
                        .content(calculationDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andReturn();

        assertThat(mvcResult.getResponse().getHeader("Location")).matches("/calculations/\\d*");
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideIncorrectCalculationDtos")
    void addingCalculationShouldThrowExceptionWhenTheGivenCalculationStringIsInvalid(CalculationDto calculationDto){
        final var calculationDtoJsonString= objectMapper.writeValueAsString(calculationDto);
        mockMvc.perform(
                post(ADD_CALCULATION_PATH)
                        .content(calculationDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getCalculationShouldReturn404WhenNotFound(){
        mockMvc.perform(
                get(GET_CALCULATION_PATH + "/0")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

}