package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeCategoryDTO;
import by.weekmenu.api.entity.RecipeCategory;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.RecipeCategoryRepository;
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
public class RecipeCategoryServiceImpl implements RecipeCategoryService{

    private final RecipeCategoryRepository recipecategoryRepository;
    private final RecycleBinRepository recycleBinRepository;
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
    public void delete(Long id) {
        recipecategoryRepository.deleteById(id);
    }

    @Override
    public List<RecipeCategoryDTO> findAll() {
        return recipecategoryRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeCategory findByName(String name) {
        return recipecategoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> checkConnectedElements(Long id) {
        List<String> list = new ArrayList<>();
        Optional<RecipeCategory> recipeCategory = recipecategoryRepository.findById(id);
        if (recipeCategory.isPresent() && recipeCategory.get().getRecipes().size()>0) {
            list.add("рецепты: " + recipeCategory.get().getRecipes().size());
        }
        return list;
    }

    @Override
    @Transactional
    public void moveToRecycleBin(RecipeCategoryDTO recipeCategoryDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(recipeCategoryDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.RECIPE_CATEGORY);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        recipecategoryRepository.softDelete(recipeCategoryDTO.getId());
    }

    private RecipeCategory convertToEntity(RecipeCategoryDTO recipeCategoryDTO) {
        return modelMapper.map(recipeCategoryDTO, RecipeCategory.class);
    }

    private RecipeCategoryDTO convertToDTO(RecipeCategory recipeCategory) {
        return modelMapper.map(recipeCategory, RecipeCategoryDTO.class);
    }
}
