package br.rocha.estocai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.rocha.estocai.model.dtos.CategoryPatchDto;
import br.rocha.estocai.model.dtos.CategoryRequestDto;
import br.rocha.estocai.model.dtos.CategoryResponseDto;
import br.rocha.estocai.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category", description = "Operations about categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
        summary = "Get all categories",
        description = "Return a pageable list of registered categories",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category list returned")
        }
    )
    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getAllCategories(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @Operation(
        summary = "Get category by ID",
        description = "Return a single category by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(
        summary = "Get category by name",
        description = "Return a category by its name",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
        }
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @Operation(
        summary = "Create category",
        description = "Create a new category",
        responses = {
            @ApiResponse(responseCode = "201", description = "Category created")
        }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto data) {
        return categoryService.createCategory(data);
    }

    @Operation(
        summary = "Update category partially",
        description = "Partially update an existing category",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "404", description = "Category not found")
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategoryPartial(
            @PathVariable Long id,
            @Valid @RequestBody CategoryPatchDto data) {
        return ResponseEntity.ok(categoryService.updateCategoryPartial(id, data));
    }

    @Operation(
        summary = "Update category completely",
        description = "Completely replace an existing category",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "404", description = "Category not found")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto data) {
        return ResponseEntity.ok(categoryService.updateCategory(id, data));
    }

    @Operation(
        summary = "Delete category",
        description = "Delete a registered category",
        responses = {
            @ApiResponse(responseCode = "204", description = "Category deleted")
        }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
