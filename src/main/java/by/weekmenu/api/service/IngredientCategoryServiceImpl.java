package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientCategoryDTO;
import by.weekmenu.api.entity.IngredientCategory;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.IngredientCategoryRepository;
import by.weekmenu.api.repository.IngredientRepository;
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
public class IngredientCategoryServiceImpl implements IngredientCategoryService {

    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    @Override
    public IngredientCategoryDTO save(IngredientCategoryDTO entityDTO) {
        return convertToDTO(ingredientCategoryRepository.save(convertToEntity(entityDTO)));
    }

    @Override
    public IngredientCategoryDTO findById(Integer id) {
        return convertToDTO(ingredientCategoryRepository.findById(id).orElse(null));
    }

    @Override
    public List<IngredientCategoryDTO> findAll() {
        return ingredientCategoryRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IngredientCategory findByName(String name) {
        return ingredientCategoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> checkConnectedElements(Integer id) {
        List<String> list = new ArrayList<>();
        Optional<IngredientCategory> ingredientCategory = ingredientCategoryRepository.findById(id);
        Integer size = ingredientRepository.findAllByIngredientCategory_Id(id).size();
        if (ingredientCategory.isPresent() && size > 0) {
            list.add("ингредиенты: " + size);
        }
        return list;
    }

    @Override
    @Transactional
    public void moveToRecycleBin(IngredientCategoryDTO ingredientCategoryDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(ingredientCategoryDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.INGREDIENT_CATEGORY);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        ingredientCategoryRepository.softDelete(ingredientCategoryDTO.getId());
    }

    private IngredientCategory convertToEntity(IngredientCategoryDTO ingredientCategoryDTO) {
        return modelMapper.map(ingredientCategoryDTO, IngredientCategory.class);
    }

    private IngredientCategoryDTO convertToDTO(IngredientCategory ingredientCategory) {
        return modelMapper.map(ingredientCategory, IngredientCategoryDTO.class);
    }
}
