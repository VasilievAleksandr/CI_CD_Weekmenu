package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeSubcategoryDTO;
import by.weekmenu.api.entity.RecipeSubcategory;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.RecipeSubcategoryRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.utils.EntityNamesConsts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeSubcategoryServiceImpl implements RecipeSubcategoryService {

    private final RecipeSubcategoryRepository recipeSubcategoryRepository;
    private final RecycleBinRepository recycleBinRepository;
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
    public List<RecipeSubcategoryDTO> findAll() {
        return recipeSubcategoryRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeSubcategory findByName(String name) {
        return recipeSubcategoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> checkConnectedElements(Long id) {
        List<String> list = new ArrayList<>();
        Optional<RecipeSubcategory> recipeSubcategory = recipeSubcategoryRepository.findById(id);
        if (recipeSubcategory.isPresent() && recipeSubcategory.get().getRecipes().size() > 0) {
            list.add("рецепты: " + recipeSubcategory.get().getRecipes().size());
        }
        return list;
    }

    @Override
    @Transactional
    public void moveToRecycleBin(RecipeSubcategoryDTO recipeSubcategoryDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(recipeSubcategoryDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.RECIPE_SUBCATEGORY);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        recipeSubcategoryRepository.softDelete(recipeSubcategoryDTO.getId());
    }

    private RecipeSubcategory convertToEntity(RecipeSubcategoryDTO recipeSubcategoryDTO) {
        return modelMapper.map(recipeSubcategoryDTO, RecipeSubcategory.class);
    }

    private RecipeSubcategoryDTO convertToDTO(RecipeSubcategory recipeSubcategory) {
        return modelMapper.map(recipeSubcategory, RecipeSubcategoryDTO.class);
    }


}

