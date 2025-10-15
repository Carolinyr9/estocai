package br.rocha.estocai.model.dtos;

import br.rocha.estocai.model.Category;

public record ProductResponseDto(Long id, String name, String description, Double price, Integer quantity, Category category) {
    
}
