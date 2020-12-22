package nl.lexfelix.simplecalculator.calculationserver.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
@NoArgsConstructor
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer a;
    @NotNull
    private Integer b;
    @NotNull
    @Enumerated(EnumType.STRING)
    private OperatorType operator;

    private Double result;

    public Calculation(@NotNull Integer a, @NotNull Integer b, @NotNull OperatorType operator) {
        this.a = a;
        this.b = b;
        this.operator = operator;
    }
}
