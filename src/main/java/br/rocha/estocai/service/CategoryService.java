package br.rocha.estocai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.rocha.estocai.exceptions.ResourceNotFoundException;
import br.rocha.estocai.mappers.CategoryMapper;
import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.dtos.CategoryPatchDto;
import br.rocha.estocai.model.dtos.CategoryRequestDto;
import br.rocha.estocai.model.dtos.CategoryResponseDto;
import br.rocha.estocai.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper mapper;

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto data){
        Category category = mapper.categoryRequestDtoToCategory(data);
        Category saved = categoryRepository.save(category);
        return mapper.categoryToCategoryResponseDto(saved);
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto data){
        Category existingCategory = findExistingCategory(id);
        existingCategory.setName(data.name());
        existingCategory.setDescription(data.description());
        return mapper.categoryToCategoryResponseDto(categoryRepository.save(existingCategory));
    }

    @Transactional
    public CategoryResponseDto updateCategoryPartial(Long id, CategoryPatchDto data){
        Category existingCategory = findExistingCategory(id);
        data.name().ifPresent(existingCategory::setName);
        data.description().ifPresent(existingCategory::setDescription);
        return mapper.categoryToCategoryResponseDto(categoryRepository.save(existingCategory));
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponseDto> getAllCategories(Pageable pageable){
        return categoryRepository.findAll(pageable)
                .map(mapper::categoryToCategoryResponseDto);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long id){
        Category category = findExistingCategory(id);
        return mapper.categoryToCategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryByName(String name){
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found: " + name);
        }
        return mapper.categoryToCategoryResponseDto(category);
    }

    @Transactional
    public void deleteCategory(Long id){
        Category category = findExistingCategory(id);
        categoryRepository.delete(category);
    }

    private Category findExistingCategory(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found " + id));
    }


    private Category findExistingCategoryByName(String name){
        return categoryRepository.findByName(name);
    }
}
