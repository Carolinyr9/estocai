package br.rocha.estocai.model.enums;

public enum TipoMovimentacao {
    ENTRADA ("entrada"),
    SAIDA ("saida"),
    EDITADO ("editado");

    private final String value;

    TipoMovimentacao(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
