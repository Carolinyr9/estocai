package br.rocha.estocai.exceptions;

public class NameConflictException extends RuntimeException {
    public NameConflictException(String message){
        super(message);
    }

    public NameConflictException(){
        super("Already a resource with this name");
    }
}
