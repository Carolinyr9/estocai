package br.rocha.estocai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.rocha.estocai.model.dtos.ProductPatchDto;
import br.rocha.estocai.model.dtos.ProductRequestDto;
import br.rocha.estocai.model.dtos.ProductResponseDto;
import br.rocha.estocai.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description ="Operations about products")
public class ProductController {
    @Autowired
    ProductService productService;

    @Operation(
        summary = "Create product",
        description = "Create a product from the param",
        responses = {
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "409", description = "Product already registred"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@Valid @RequestBody ProductRequestDto data) {
        ProductResponseDto product = productService.createProduct(data);
        return product;
    }

    @Operation(
        summary = "Update completely a product",
        description = "Update all attributes from a product",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto data) {
        ProductResponseDto product = productService.updateProduct(id, data);
        return ResponseEntity.ok(product);
    }

    @Operation(
        summary = "Update partialy a product",
        description = "Update some attributes from a product",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductPatchDto data) {
        ProductResponseDto product = productService.updateProductPartial(id, data);
        return ResponseEntity.ok(product);
    }

    @Operation(
        summary = "Search for products",
        description = "Return a pageable list of all registred products",
        responses = {
            @ApiResponse(responseCode = "200", description = "Products founds"),
            @ApiResponse(responseCode = "404", description = "Products not founds"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable){
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(
        summary = "Search for a product by id",
        description = "Return a product by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id){
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(
        summary = "Search for a product by name",
        description = "Return a product by name",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductResponseDto> getProductByName(@PathVariable String name){
        ProductResponseDto product = productService.getProductByName(name);
        return ResponseEntity.ok(product);
    }
    
    @Operation(
        summary = "Delete a product",
        description = "Delete a product by id",
        responses = {
            @ApiResponse(responseCode = "204", description = "Product deleted, no content more"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }
}
