package br.rocha.estocai.Category;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.rocha.estocai.exceptions.NameConflictException;
import br.rocha.estocai.exceptions.ResourceNotFoundException;
import br.rocha.estocai.mappers.CategoryMapper;
import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.dtos.CategoryPatchDto;
import br.rocha.estocai.model.dtos.CategoryRequestDto;
import br.rocha.estocai.model.dtos.CategoryResponseDto;
import br.rocha.estocai.repository.CategoryRepository;
import br.rocha.estocai.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;

    @Test
    void getCategoryById_ValidId() {
        // Given
        Long id = 1L;
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", id);

        CategoryResponseDto dto = new CategoryResponseDto(id, "Category", "Description");

        // When
        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(mapper.categoryToCategoryResponseDto(category)).thenReturn(dto);

        CategoryResponseDto categoryResponse = service.getCategoryById(id);

        // Then
        assertEquals(id, categoryResponse.id());
    }

    @Test
    void getCategoryById_InvalidId() {
        Long id = 9999L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getCategoryById(id));
    }

    @Test
    void getCategoryByName_ValidName() {
        // Given
        Long id = 1L;
        String name = "Category";
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", id);

        CategoryResponseDto dto = new CategoryResponseDto(id, "Category", "Description");

        // When
        when(repository.findByName(name)).thenReturn(category);
        when(mapper.categoryToCategoryResponseDto(category)).thenReturn(dto);

        CategoryResponseDto categoryResponse = service.getCategoryByName(name);

        // Then
        assertEquals(name, categoryResponse.name());
    }

    @Test
    void getCategoryByName_InvalidName() {
        String name = "Invalid Name";
        when(repository.findByName(name)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> service.getCategoryByName(name));
    }

    @Test
    void getAllCategories_ValidPageable() {
        // Given
        Category category1 = new Category("Category1", "Description1");
        ReflectionTestUtils.setField(category1, "id", 1L);

        Category category2 = new Category("Category2", "Description2");
        ReflectionTestUtils.setField(category2, "id", 2L);

        List<Category> categoryList = List.of(category1, category2);
        Page<Category> page = new PageImpl<>(categoryList);

        CategoryResponseDto dto1 = new CategoryResponseDto(1L, "Category1", "Description1");
        CategoryResponseDto dto2 = new CategoryResponseDto(2L, "Category2", "Description2");

        // When
        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.categoryToCategoryResponseDto(category1)).thenReturn(dto1);
        when(mapper.categoryToCategoryResponseDto(category2)).thenReturn(dto2);

        Page<CategoryResponseDto> result = service.getAllCategories(Pageable.unpaged());

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals("Category1", result.getContent().get(0).name());
        assertEquals("Category2", result.getContent().get(1).name());
    }

    @Test
    void getAllCategories_WithNoCategories() {
        // Given
        Page<Category> page = new PageImpl<>(Collections.emptyList());

        // When
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<CategoryResponseDto> result = service.getAllCategories(Pageable.unpaged());

        // Then
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void createCategory_ValidArgs() {
        // Given
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("Name", "Description");
        Category category = new Category("Name", "Description");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Name", "Description");

        // When
        when(repository.save(category)).thenReturn(category);
        when(mapper.categoryRequestDtoToCategory(categoryRequestDto)).thenReturn(category);
        when(mapper.categoryToCategoryResponseDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto result = service.createCategory(categoryRequestDto);

        // Then
        assertEquals(categoryResponseDto, result);
    }

    @Test
    void createCategory_NameAlreadyExists() {
        // Given
        CategoryRequestDto dto = new CategoryRequestDto("ExistingCategory", "Any description");
        Category existing = new Category("ExistingCategory", "Old description");

        // When
        when(repository.findByName("ExistingCategory")).thenReturn(existing);

        // Then
        assertThrows(NameConflictException.class, () -> service.createCategory(dto));
        verify(repository, never()).save(any(Category.class));
    }

    @Test
    void updateCategory_ValidArgs() {
        // Given
        Long id = 1L;
        CategoryRequestDto dto = new CategoryRequestDto("UpdatedCategory", "Updated description");

        Category existing = new Category("OldCategory", "Old description");
        ReflectionTestUtils.setField(existing, "id", id);

        CategoryResponseDto responseDto = new CategoryResponseDto(id, "UpdatedCategory", "Updated description");

        // When
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.findByName(dto.name())).thenReturn(null);
        when(repository.save(existing)).thenReturn(existing);
        when(mapper.categoryToCategoryResponseDto(existing)).thenReturn(responseDto);

        CategoryResponseDto result = service.updateCategory(id, dto);

        // Then
        assertEquals(responseDto, result);
    }

    @Test
    void updateCategory_NameConflict() {
        Long id = 1L;
        CategoryRequestDto dto = new CategoryRequestDto("ExistingCategory", "Any description");

        Category existing = new Category("OldCategory", "Old description");
        ReflectionTestUtils.setField(existing, "id", id);

        Category otherCategoryWithSameName = new Category("ExistingCategory", "Other description");
        ReflectionTestUtils.setField(otherCategoryWithSameName, "id", 2L);

        when(repository.findByName(dto.name())).thenReturn(otherCategoryWithSameName);

        assertThrows(NameConflictException.class, () -> service.updateCategory(id, dto));
        verify(repository, never()).save(any());
    }

    @Test
    void updateCategoryPartial_ValidNameArgs() {
        // Given
        Long id = 1L;
        CategoryPatchDto dto = new CategoryPatchDto(Optional.of("UpdatedCategory"), Optional.empty());

        Category existing = new Category("OldCategory", "Old description");
        ReflectionTestUtils.setField(existing, "id", id);

        CategoryResponseDto responseDto = new CategoryResponseDto(id, "UpdatedCategory", "Old description");

        // When
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        when(mapper.categoryToCategoryResponseDto(existing)).thenReturn(responseDto);

        CategoryResponseDto result = service.updateCategoryPartial(id, dto);

        // Then
        assertEquals(responseDto, result);
    }

    @Test
    void updateCategoryPartial_ValidDescArgs() {
        // Given
        Long id = 1L;
        CategoryPatchDto dto = new CategoryPatchDto(Optional.empty(), Optional.of("New description"));

        Category existing = new Category("ExistingCategory", "Old description");
        ReflectionTestUtils.setField(existing, "id", id);

        CategoryResponseDto responseDto = new CategoryResponseDto(id, "ExistingCategory", "New description");

        // When
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        when(mapper.categoryToCategoryResponseDto(existing)).thenReturn(responseDto);

        CategoryResponseDto result = service.updateCategoryPartial(id, dto);

        // Then
        assertEquals(responseDto, result);
    }

    @Test
    void updateCategoryPartial_NoArgs() {
        // Given
        Long id = 1L;
        CategoryPatchDto dto = new CategoryPatchDto(Optional.empty(), Optional.empty());

        Category existing = new Category("ExistingCategory", "Old description");
        ReflectionTestUtils.setField(existing, "id", id);

        CategoryResponseDto responseDto = new CategoryResponseDto(id, "ExistingCategory", "Old description");

        // When
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        when(mapper.categoryToCategoryResponseDto(existing)).thenReturn(responseDto);

        CategoryResponseDto result = service.updateCategoryPartial(id, dto);

        // Then
        assertEquals(responseDto, result);
    }

    @Test
    void deleteCategory_ValidId() {
        // Given
        Long id = 1L;
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", id);

        // When
        when(repository.findById(id)).thenReturn(Optional.of(category));

        service.deleteCategory(id);

        // Then
        verify(repository).delete(category);
    }

    @Test
    void deleteCategory_InvalidId() {
        // Given
        Long id = 999L;

        // When
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> service.deleteCategory(id));
    }

}
