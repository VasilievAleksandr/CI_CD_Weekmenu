package by.weekmenu.api.service;

import by.weekmenu.api.dto.CategoryDto;
import by.weekmenu.api.entity.Category;
import by.weekmenu.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto save(CategoryDto entityDto) {
        return convertToDto(categoryRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public CategoryDto findById(Long id) {
        return convertToDto(categoryRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> findAll() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> getAllCategoryNames() {
        return categoryRepository.findAll().
                stream()
                .filter(Objects::nonNull)
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    private CategoryDto convertToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }


}

