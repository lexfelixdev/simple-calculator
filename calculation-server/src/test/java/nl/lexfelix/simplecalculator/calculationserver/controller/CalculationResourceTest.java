package nl.lexfelix.simplecalculator.calculationserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import nl.lexfelix.simplecalculator.calculationserver.CalculationServerApplication;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import nl.lexfelix.simplecalculator.calculationserver.controller.dto.CalculationDto;
import nl.lexfelix.simplecalculator.calculationserver.repository.CalculationRepository;
import nl.lexfelix.simplecalculator.calculationserver.repository.entity.OperatorType;
import nl.lexfelix.simplecalculator.calculationserver.service.TestCalculation;
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
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CalculationServerApplication.class)
@AutoConfigureMockMvc
@Transactional
@Testcontainers
class CalculationResourceTest {

    @Container
    private final static MySQLContainer MYSQL_CONTAINER = new MySQLContainer(DockerImageName.parse("mysql:8"));

    public static final String ADD_CALCULATION_PATH = "/calculations";
    private static final String GET_CALCULATION_PATH = "/calculations";


    @Captor
    private ArgumentCaptor<Calculation> calculationArgumentCaptor;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void addingCalculationShouldReturnCorrectResults(){
        final var calculationDtoList = Arrays.asList(
                new CalculationDto(4,1, OperatorType.DIVIDE),
                new CalculationDto(1,3, OperatorType.ADD),
                new CalculationDto(10,-20,OperatorType.SUBTRACT));
        final var expectedCalculationDtoList = Arrays.asList(
                new CalculationDto(4,1, OperatorType.DIVIDE, 4.0),
                new CalculationDto(1,3, OperatorType.ADD, 4.0),
                new CalculationDto(10,-20,OperatorType.SUBTRACT, 30.0));

        final var calculationDtoJsonString= objectMapper.writeValueAsString(calculationDtoList);
        mockMvc.perform(
                post(ADD_CALCULATION_PATH)
                        .content(calculationDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedCalculationDtoList)));
    }

    @SneakyThrows
    @Test
    void addingCalculationShouldReturnBadRequestWhenListContainsOneInvalidCalculation(){
        final var calculationDtoList = Arrays.asList(
                new CalculationDto(4,0, OperatorType.DIVIDE),
                new CalculationDto(1,3, OperatorType.ADD),
                new CalculationDto(10,-20,OperatorType.SUBTRACT));

        final var calculationDtoJsonString= objectMapper.writeValueAsString(calculationDtoList);
        mockMvc.perform(
                post(ADD_CALCULATION_PATH)
                        .content(calculationDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
    @SneakyThrows
    @Test
    void addingCalculationShouldReturnBadRequestWhenListContainsMultipleInvalidCalculation(){
        final var calculationDtoList = Arrays.asList(
                new CalculationDto(4,0, OperatorType.DIVIDE),
                new CalculationDto(null,3, OperatorType.ADD),
                new CalculationDto(10,-20,OperatorType.SUBTRACT));

        final var calculationDtoJsonString= objectMapper.writeValueAsString(calculationDtoList);
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