package br.rocha.estocai.model.dtos;

public record ProductRequestDto(String name, String description, Double price, int quantity, Long categoryId) {
    
}
