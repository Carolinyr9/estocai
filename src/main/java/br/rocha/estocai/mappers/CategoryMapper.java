package br.rocha.estocai.mappers;

import org.mapstruct.Mapper;

import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.dtos.CategoryRequestDto;
import br.rocha.estocai.model.dtos.CategoryResponseDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryResponseDto categoryToCategoryResponseDto(Category category);

    Category categoryResponseDtoToCategory(CategoryResponseDto categoryResponseDto);

    Category categoryRequestDtoToCategory(CategoryRequestDto categoryRequest);

    CategoryRequestDto categoryToCategoryRequestDto(Category category);    
 
}
