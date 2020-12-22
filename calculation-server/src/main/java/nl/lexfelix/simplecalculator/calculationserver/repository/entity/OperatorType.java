package nl.lexfelix.simplecalculator.calculationserver.repository.entity;

import lombok.Getter;

@Getter
public enum OperatorType {
    ADD("+"), SUBTRACT("-"),MULTIPLY("*"),DIVIDE("/");

    private final String value;

    OperatorType(String value) {
        this.value = value;
    }
}
