package nl.lexfelix.simplecalculator.calculationserver.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calculation {

    public static final String CALCULATION_REGEX = "(?:((?:[-1-9][0-9]*)|0)([-+*\\/])((?:[-1-9][0-9]*)|0))(?:([-+*\\/])((?:[-1-9][0-9]*)|0))*";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = CALCULATION_REGEX, message = "Must be a valid calculation")
    private String calculation;
    private Double result;

    public Calculation(Long id, String calculation) {
        this.id = id;
        this.calculation = calculation;
    }
    public Calculation(String calculation) {
        this.calculation = calculation;
    }
}
