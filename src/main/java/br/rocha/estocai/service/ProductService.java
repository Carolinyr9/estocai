package br.rocha.estocai.service;

import java.security.InvalidParameterException;

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
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    MovementService movementService;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto data){
        Product product = productMapper.productRequestDtoToProduct(data);

        product.setCategory(findCategory(data.categoryId()));

        movementService.createProduct(product);

        return productMapper.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto data){
        Product existingProduct = findExistingProduct(id);

        existingProduct.setName(data.name());
        existingProduct.setDescription(data.description());
        existingProduct.setPrice(data.price());
        existingProduct.setQuantity(data.quantity());
        existingProduct.setCategory(findCategory(data.categoryId()));

        Product productSaved = productRepository.save(existingProduct);

        movementService.updateProduct(productSaved);

        return productMapper.productToProductResponseDto(productSaved);
    }

    @Transactional
    public ProductResponseDto updateProductPartial(Long id, ProductPatchDto data){
        Product existingProduct = findExistingProduct(id);

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

        return productMapper.productToProductResponseDto(productSaved);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);

        products.forEach(movementService::consultProduct);

        return products.map(product -> productMapper.productToProductResponseDto(product));
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id){
        Product product = findExistingProduct(id);

        movementService.consultProduct(product);

        return productMapper.productToProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductByName(String name){
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found: " + name);
        }
        
        movementService.consultProduct(product);

        return productMapper.productToProductResponseDto(product);
    }

    public void deleteProduct(Long id){
        Product product = findExistingProduct(id);

        movementService.removeProduct(product);

        productRepository.delete(product);
    }

    @Transactional
    public ProductResponseDto decreaseQuantity(Long id, Integer quantity){
        Product product = findExistingProduct(id);

        if(quantity > product.getQuantity()){
            throw new InvalidParameterException("The new quantity cannot be negative");
        }

        product.setQuantity(product.getQuantity() - quantity);
        Product saved = productRepository.save(product);

        movementService.decreaseQuantity(product);

        return productMapper.productToProductResponseDto(saved);
    }

    @Transactional
    public ProductResponseDto setSpecificQuantity(Long id, Integer quantity){
        Product product = findExistingProduct(id);

        int before = product.getQuantity();
        product.setQuantity(quantity);
        Product saved = productRepository.save(product);

        verifyAndMapMovement(before, quantity, saved);

        return productMapper.productToProductResponseDto(saved);
    }

    @Transactional
    public ProductResponseDto increaseQuantity(Long id, Integer quantity){
        Product product = findExistingProduct(id);
        product.setQuantity(product.getQuantity() + quantity);
        Product saved = productRepository.save(product);

        movementService.increaseQuantity(saved);

        return productMapper.productToProductResponseDto(saved);
    }

    private Product findExistingProduct(Long id){
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found " + id));
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
