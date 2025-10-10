package br.rocha.estocai.model.enums;

public enum MovementDescription {
    ADDED("added"),
    QUANTITY_DECREASED("quantity decreased"),
    QUANTITY_INCREASED("quantity increased"),
    EDITED("edited"),
    REMOVED("removed"),
    CONSULT("consult");

    private final String value;
    
    MovementDescription(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
