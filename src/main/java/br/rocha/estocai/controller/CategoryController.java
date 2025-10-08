package br.rocha.estocai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.rocha.estocai.model.dtos.CategoryPatchDto;
import br.rocha.estocai.model.dtos.CategoryRequestDto;
import br.rocha.estocai.model.dtos.CategoryResponseDto;
import br.rocha.estocai.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/categories")
@Tag(name = "Category", description = "Operations about categories")
@Validated
public class CategoryController {
	@Autowired
    CategoryService categoryService;

    @Operation(
        summary = "Search for categories",
        description = "Return a pageable list of registered categories",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid requisition")
        }
    )
    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getAllCategories(Pageable pageable) {
        Page<CategoryResponseDto> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    @Operation(
        summary = "Search for categories by id",
        description = "Return a category registred",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid requisition")
        }
    )
    @GetMapping("{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(
        summary = "Create category",
        description = "Create a category from the param",
        responses = {
            @ApiResponse(responseCode = "201", description = "Category created"),
            @ApiResponse(responseCode = "409", description = "Category already registred"),
            @ApiResponse(responseCode = "400", description = "Invalid requisition")
        }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto data){
        CategoryResponseDto category = categoryService.createCategory(data);
        return category;
    }
    
    @Operation(
        summary = "Update partial of a category",
        description = "Partially changes an existing category",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category changed"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid requisition")
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategoryPartial(@PathVariable Long id, @Valid @RequestBody CategoryPatchDto data){
        CategoryResponseDto category = categoryService.updateCategoryPartial(id, data);
        return ResponseEntity.ok(category);
    }

    @Operation(
        summary = "Update complete of a category",
        description = "Completely changes an existing category",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category changed"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid requisition")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto data){
        CategoryResponseDto category = categoryService.updateCategory(id, data);
        return ResponseEntity.ok(category);
    }

    @Operation(
        summary = "Delete a category registred",
        description = "Delete a category already registred",
        responses = {
            @ApiResponse(responseCode = "204", description = "Category deleted, no content more"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid requisition")
        }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }
}
