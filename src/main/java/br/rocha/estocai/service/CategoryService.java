package br.rocha.estocai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.rocha.estocai.exceptions.ResourceNotFoundException;
import br.rocha.estocai.mappers.CategoryMapper;
import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.dtos.CategoryPatchDto;
import br.rocha.estocai.model.dtos.CategoryRequestDto;
import br.rocha.estocai.model.dtos.CategoryResponseDto;
import br.rocha.estocai.repository.CategoryRepository;
import jakarta.transaction.Transactional;

public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper mapper;
    
    public CategoryResponseDto createCategory(CategoryRequestDto data){
        Category category = mapper.categoryRequestDtoToCategory(data);

        categoryRepository.save(category);

        CategoryResponseDto response = mapper.categoryToCategoryResponseDto(category);

        return response;

    }

    @Transactional
    public CategoryResponseDto updateCategoryPartial(Long id, CategoryPatchDto data){
        Category existingCategory = findExistingCategory(id);

        data.name().ifPresent(existingCategory::setName);
        data.description().ifPresent(existingCategory::setDescription);

        Category categorySaved = categoryRepository.save(existingCategory);
        return mapper.categoryToCategoryResponseDto(categorySaved);
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto data){
        Category existingCategory = findExistingCategory(id);

        existingCategory.setName(data.name());
        existingCategory.setDescription(data.description());

        Category categorySaved = categoryRepository.save(existingCategory);
        return mapper.categoryToCategoryResponseDto(categorySaved);
    }

    @Transactional
    public Page<CategoryResponseDto> getAllCategories(Pageable pageable){
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(category -> mapper.categoryToCategoryResponseDto(category));
    }

    public CategoryResponseDto getCategoryById(Long id){
        Category category = findExistingCategory(id);
        return mapper.categoryToCategoryResponseDto(category);
    }

    public CategoryResponseDto getCategoryByName(String name){
        Category category = categoryRepository.findByName(name);
        return mapper.categoryToCategoryResponseDto(category);
    }

    public void deleteCategory(Long id){
        findExistingCategory(id);
        categoryRepository.deleteById(id);
    }

    private Category findExistingCategory(Long id){
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found " + id));
        return existingCategory;
    }

}
