package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeCategoryDTO;
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
    public RecipeCategoryDTO save(RecipeCategoryDTO entityDTO) {
        return convertToDTO(recipecategoryRepository.save(convertToEntity(entityDTO)));
    }

    @Override
    public RecipeCategoryDTO findById(Long id) {
        return convertToDTO(recipecategoryRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {recipecategoryRepository.deleteById(id);
    }

    @Override
    public List<RecipeCategoryDTO> findAll() {
        return StreamSupport.stream(recipecategoryRepository.findAll().spliterator(), false)
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
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

    private RecipeCategory convertToEntity(RecipeCategoryDTO recipecategoryDTO) {
        return modelMapper.map(recipecategoryDTO, RecipeCategory.class);
    }

    private RecipeCategoryDTO convertToDTO(RecipeCategory recipecategory) {
        return modelMapper.map(recipecategory, RecipeCategoryDTO.class);
    }


}

