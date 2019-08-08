package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeSubcategoryDTO;
import by.weekmenu.api.entity.RecipeSubcategory;
import by.weekmenu.api.repository.RecipeSubcategoryRepository;
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
public class RecipeSubcategoryServiceImpl implements RecipeSubcategoryService {

    private final RecipeSubcategoryRepository recipeSubcategoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public RecipeSubcategoryDTO save(RecipeSubcategoryDTO entityDTO) {
        return convertToDTO(recipeSubcategoryRepository.save(convertToEntity(entityDTO)));
    }

    @Override
    public RecipeSubcategoryDTO findById(Long id) {
        return convertToDTO(recipeSubcategoryRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {
        recipeSubcategoryRepository.deleteById(id);
    }

    @Override
    public List<RecipeSubcategoryDTO> findAll() {
        return StreamSupport.stream(recipeSubcategoryRepository.findAll().spliterator(), false)
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeSubcategory findByName(String name) {
        return recipeSubcategoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> getAllRecipeSubcategoryNames() {
        return recipeSubcategoryRepository.findAll().
                stream()
                .filter(Objects::nonNull)
                .map(RecipeSubcategory::getName)
                .collect(Collectors.toList());
    }

    private RecipeSubcategory convertToEntity(RecipeSubcategoryDTO recipeSubcategoryDTO) {
        return modelMapper.map(recipeSubcategoryDTO, RecipeSubcategory.class);
    }

    private RecipeSubcategoryDTO convertToDTO(RecipeSubcategory recipeSubcategory) {
        return modelMapper.map(recipeSubcategory, RecipeSubcategoryDTO.class);
    }


}

