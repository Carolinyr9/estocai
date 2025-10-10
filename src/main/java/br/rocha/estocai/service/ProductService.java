package br.rocha.estocai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.rocha.estocai.exceptions.ResourceNotFoundException;
import br.rocha.estocai.mappers.CategoryMapper;
import br.rocha.estocai.mappers.ProductMapper;
import br.rocha.estocai.model.Product;
import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.dtos.CategoryResponseDto;
import br.rocha.estocai.model.dtos.ProductPatchDto;
import br.rocha.estocai.model.dtos.ProductRequestDto;
import br.rocha.estocai.model.dtos.ProductResponseDto;
import br.rocha.estocai.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductMapper mapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    MovementService movementService;


    public ProductResponseDto createProduct(ProductRequestDto data){
        Product product = mapper.productRequestDtoToProduct(data);

        product.setCategory(findCategory(data.categoryId()));

        movementService.createProduct(product);

        return mapper.productToProductResponseDto(productRepository.save(product));
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto data){
        Product existingProduct = thereIsProduct(id);

        existingProduct.setName(data.name());
        existingProduct.setDescription(data.description());
        existingProduct.setPrice(data.price());
        existingProduct.setQuantity(data.quantity());
        existingProduct.setCategory(findCategory(data.categoryId()));

        Product productSaved = productRepository.save(existingProduct);

        movementService.updateProduct(productSaved);

        return mapper.productToProductResponseDto(productSaved);
    }

    public ProductResponseDto updateProductPartial(Long id, ProductPatchDto data){
        Product existingProduct = thereIsProduct(id);

        data.name().ifPresent(existingProduct::setName);
        data.description().ifPresent(existingProduct::setDescription);

        data.price().ifPresent(existingProduct::setPrice);
        data.quantity().ifPresent(existingProduct::setQuantity);
        data.categoryId().ifPresent(categoryId -> {
            Category category = findCategory(categoryId);
            existingProduct.setCategory(category);
        });

        Product productSaved = productRepository.save(existingProduct);

        movementService.updateProduct(productSaved);

        return mapper.productToProductResponseDto(productSaved);
    }

    public Page<ProductResponseDto> getAllProducts(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);

        products.forEach(product -> movementService.consultProduct(product));

        return products.map(product -> mapper.productToProductResponseDto(product));
    }

    public ProductResponseDto getProductById(Long id){
        Product product = thereIsProduct(id);

        movementService.consultProduct(product);

        return mapper.productToProductResponseDto(product);
    }

    public ProductResponseDto getProductByName(String name){
        Product product = productRepository.findByName(name);
        
        movementService.consultProduct(product);
        
        return mapper.productToProductResponseDto(product);
    }

    public void deleteProduct(Long id){
        thereIsProduct(id);

       productRepository.findById(id).ifPresent(movementService::removeProduct);

        productRepository.deleteById(id);
    }

    public ProductResponseDto decreaseQuantity(Long id){
        Product product = thereIsProduct(id);
        product.setQuantity(product.getQuantity() - 1);
        Product saved = productRepository.save(product);

        movementService.decreaseQuantity(product);

        return mapper.productToProductResponseDto(saved);
    }

    public ProductResponseDto setSpecificQuantity(Long id, Integer quantity){
        Product product = thereIsProduct(id);

        product.setQuantity(quantity);
        Product saved = productRepository.save(product);

        verifyAndMapMovement(product.getQuantity(), quantity, saved);

        return mapper.productToProductResponseDto(saved);
    }

    public ProductResponseDto increaseQuantity(Long id, Integer quantity){
        Product product = thereIsProduct(id);
        product.setQuantity(product.getQuantity() + 1);
        Product saved = productRepository.save(product);
        return mapper.productToProductResponseDto(saved);
    }

    private Product thereIsProduct(Long id){
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found " + id));
        return existingProduct;
    }

    private Category findCategory(Long categoryId){
        CategoryResponseDto category = categoryService.getCategoryById(categoryId);
        return categoryMapper.categoryResponseDtoToCategory(category);
    }

    private void verifyAndMapMovement(Integer quantityBefore, Integer quantityNow, Product product){
        if(quantityBefore > quantityNow){
            movementService.decreaseQuantity(product);
        } else {
            movementService.increaseQuantity(product);
        }
    }
}
