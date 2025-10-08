package br.rocha.estocai.mappers;

import org.mapstruct.Mapper;

import br.rocha.estocai.model.Product;
import br.rocha.estocai.model.dtos.ProductRequestDto;
import br.rocha.estocai.model.dtos.ProductResponseDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product productRequestDtoToProduct(ProductRequestDto productRequestDto);

    ProductRequestDto productToProductRequestDto(Product product);

    Product productResponseDtoToProduct(ProductResponseDto product);

    ProductResponseDto productToProductResponseDto(Product product);
}
