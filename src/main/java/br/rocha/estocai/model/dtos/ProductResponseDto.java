package br.rocha.estocai.model.dtos;

import java.util.Locale.Category;

public record ProductResponseDto(Long id, String name, String description, Double price, Integer quantity, Category category) {
    
}
