package br.rocha.estocai.model.enums;

public enum MovementType {
    ENTRY("entry"),
    EXIT("exit"),
    EDITED("edited"),
    NONE("none");

    private final String value;

    MovementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
