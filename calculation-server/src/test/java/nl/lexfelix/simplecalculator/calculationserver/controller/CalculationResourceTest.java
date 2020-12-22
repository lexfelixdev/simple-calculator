package nl.lexfelix.simplecalculator.calculationserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import nl.lexfelix.simplecalculator.calculationserver.CalculationServerApplication;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import nl.lexfelix.simplecalculator.calculationserver.controller.dto.CalculationDto;
import nl.lexfelix.simplecalculator.calculationserver.repository.CalculationRepository;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.OperatorType;
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
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.transaction.Transactional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CalculationServerApplication.class)
@AutoConfigureMockMvc
@Transactional
@Testcontainers
class CalculationResourceTest {

    @Container
    private final static MySQLContainer MYSQL_CONTAINER = new MySQLContainer(DockerImageName.parse("mysql:8"));

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
                new CalculationDto(1,3, OperatorType.ADD),
                new CalculationDto(10,-20,OperatorType.SUBTRACT),
                new CalculationDto(-1,4, OperatorType.SUBTRACT),
                new CalculationDto(1,4, OperatorType.MULTIPLY),
                new CalculationDto(1,4, OperatorType.DIVIDE)
        );
    }
    private static Stream<CalculationDto> provideIncorrectCalculationDtos(){
        return Stream.of(
                new CalculationDto(null,null,null),
                new CalculationDto(1,null,null),
                new CalculationDto(null,2,null),
                new CalculationDto(null,null,OperatorType.ADD)
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