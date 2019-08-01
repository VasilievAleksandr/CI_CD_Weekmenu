package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeCategoryDto;
import by.weekmenu.api.entity.RecipeCategory;
import by.weekmenu.api.repository.RecipeCategoryRepository;
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
public class RecipeCategoryServiceImpl implements RecipeCategoryService{

    private final RecipeCategoryRepository recipecategoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public RecipeCategoryDto save(RecipeCategoryDto entityDto) {
        return convertToDto(recipecategoryRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public RecipeCategoryDto findById(Long id) {
        return convertToDto(recipecategoryRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {recipecategoryRepository.deleteById(id);
    }

    @Override
    public List<RecipeCategoryDto> findAll() {
        return StreamSupport.stream(recipecategoryRepository.findAll().spliterator(), false)
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeCategory findByName(String name) {
        return recipecategoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> getAllRecipeCategoryNames() {
        return recipecategoryRepository.findAll().
                stream()
                .filter(Objects::nonNull)
                .map(RecipeCategory::getName)
                .collect(Collectors.toList());
    }

    private RecipeCategory convertToEntity(RecipeCategoryDto recipecategoryDto) {
        return modelMapper.map(recipecategoryDto, RecipeCategory.class);
    }

    private RecipeCategoryDto convertToDto(RecipeCategory recipecategory) {
        return modelMapper.map(recipecategory, RecipeCategoryDto.class);
    }


}

