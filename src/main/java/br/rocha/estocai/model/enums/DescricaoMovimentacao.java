package br.rocha.estocai.model.enums;

public enum DescricaoMovimentacao {
    ADICIONADO ("adicionado"),
    QUANTIDADE_DECRESCIDA("descrecido"),
    QUANTIDADE_ACRESCIDA("acrescido"),
    EDITADO("editado"),
    REMOVIDO("removido");

    private final String value;
    
    DescricaoMovimentacao(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
