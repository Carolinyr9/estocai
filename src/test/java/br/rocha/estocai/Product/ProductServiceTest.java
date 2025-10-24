package br.rocha.estocai.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import br.rocha.estocai.exceptions.ResourceNotFoundException;
import br.rocha.estocai.mappers.CategoryMapper;
import br.rocha.estocai.mappers.ProductMapper;
import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.Product;
import br.rocha.estocai.model.dtos.CategoryResponseDto;
import br.rocha.estocai.model.dtos.ProductRequestDto;
import br.rocha.estocai.model.dtos.ProductResponseDto;
import br.rocha.estocai.repository.ProductRepository;
import br.rocha.estocai.service.CategoryService;
import br.rocha.estocai.service.MovementService;
import br.rocha.estocai.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;
    
    @Mock 
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private MovementService movementService;


    @Test
    void createProduct_ValidArgs(){
        ProductRequestDto productRequestDto = new ProductRequestDto("Name", "Description", 10.00, 12, 1L);

        Product product = new Product("Name", "Description", 10.00, 12);
        ReflectionTestUtils.setField(product, "id", 1L);

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Nome", "Des");

        Category category = new Category("Nome", "Des");
        ReflectionTestUtils.setField(category, "id", 1L);


        ProductResponseDto productResponseDto = new ProductResponseDto(1L, "Name", "Description", 10.00, 12, category);

        when(mapper.productRequestDtoToProduct(productRequestDto)).thenReturn(product);
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponseDto);
        when(categoryMapper.categoryResponseDtoToCategory(categoryResponseDto)).thenReturn(category);
        when(repository.save(product)).thenReturn(product);
        when(mapper.productToProductResponseDto(product)).thenReturn(productResponseDto);
        doNothing().when(movementService).createProduct(any(Product.class));

        ProductResponseDto result = service.createProduct(productRequestDto);

        assertEquals(productResponseDto, result);

    }

    @Test
    void getProductById_ValidId(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Long idProduct = 1L;
        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", idProduct);

        ProductResponseDto responseDto = new ProductResponseDto(idProduct, "Product", "Description", 11.99, 12, category);

        when(repository.findById(idProduct)).thenReturn(Optional.of(product));

        when(mapper.productToProductResponseDto(product)).thenReturn(responseDto);

        doNothing().when(movementService).consultProduct(any(Product.class));

        ProductResponseDto result = service.getProductById(idProduct);

        assertEquals(responseDto, result);

    }

    @Test
    void getProductById_InvalidId(){
        Long id = 9999L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getProductById(id));
    }


    @Test
    void getProductByName_ValidName(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        String name = "Product";
        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        ProductResponseDto responseDto = new ProductResponseDto(1L, "Product", "Description", 11.99, 12, category);

        when(repository.findByName(name)).thenReturn(product);

        when(mapper.productToProductResponseDto(product)).thenReturn(responseDto);

        doNothing().when(movementService).consultProduct(any(Product.class));

        ProductResponseDto result = service.getProductByName(name);

        assertEquals(responseDto, result);

    }

    @Test
    void getProductByName_InvalidName(){
        String name = "Product Not Exists";
        when(repository.findByName(name)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> service.getProductByName(name));
    }

    @Test
    void getAllProducts_ValidPageable(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        Product product2 = new Product("Product 2", "Description 2", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 2L);

        ProductResponseDto responseDto = new ProductResponseDto(1L, "Product", "Description", 11.99, 12, category);

        ProductResponseDto responseDto2 = new ProductResponseDto(2L, "Product 2", "Description 2", 11.99, 12, category);

        List<Product> productList = List.of(product, product2);
        Page<Product> page = new PageImpl<>(productList);
        

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.productToProductResponseDto(product)).thenReturn(responseDto);
        when(mapper.productToProductResponseDto(product2)).thenReturn(responseDto2);

        Page<ProductResponseDto> result = service.getAllProducts(org.springframework.data.domain.Pageable.unpaged());

        assertEquals(2, result.getTotalElements());
        assertEquals("Product", result.getContent().get(0).name());
        assertEquals("Product 2", result.getContent().get(1).name());

    }

    @Test
    void getAllProducts_WithNoProducts(){
        Page<Product> page = new PageImpl<>(Collections.emptyList());

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductResponseDto> result = service.getAllProducts(Pageable.unpaged());

        assertEquals(0, result.getTotalElements());
    }

    @Test
    void deleteProduct_ValidId(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Long idProduct = 1L;
        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", idProduct);

        when(repository.findById(idProduct)).thenReturn(Optional.of(product));

        service.deleteProduct(idProduct);

        verify(repository).delete(product);
    }

    @Test
    void deleteProduct_InvalidId(){
        Long id = 999L;
        
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,  () -> service.deleteProduct(id));

    }
    
}
