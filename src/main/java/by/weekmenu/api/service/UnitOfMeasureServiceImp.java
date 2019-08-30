package by.weekmenu.api.service;

import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.EntityNamesConsts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class UnitOfMeasureServiceImp implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;
    private final IngredientPriceRepository ingredientPriceRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UnitOfMeasureDTO save(UnitOfMeasureDTO entityDto) {
        return convertToDto(unitOfMeasureRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public UnitOfMeasureDTO findById(Long id) {
        return convertToDto(unitOfMeasureRepository.findById(id).orElse(null));
    }

    @Override
    public List<UnitOfMeasureDTO> findAll() {
        return unitOfMeasureRepository.findAllByIsArchivedIsFalse().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UnitOfMeasure findByShortName(String shortName) {
        return unitOfMeasureRepository
                .findByShortNameIgnoreCase(shortName).orElse(null);
    }

    @Override
    public UnitOfMeasure findByFullName(String fullName) {
        return unitOfMeasureRepository
                .findByFullNameIgnoreCase(fullName).orElse(null);
    }

    @Override
    public List<String> checkConnectedElements(Long id) {
        List<String> list = new ArrayList<>();
        List<IngredientUnitOfMeasure> ingredientUnitOfMeasures = ingredientUnitOfMeasureRepository.findAllById_UnitOfMeasureId(id);
        List<IngredientPrice> ingredientPrices = ingredientPriceRepository.findAllByUnitOfMeasure_Id(id);
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllByUnitOfMeasure_Id(id);
        if (ingredientUnitOfMeasures.size() > 0) {
            list.add("ингредиенты: " + ingredientUnitOfMeasures.size());
        }
        if (ingredientPrices.size() > 0) {
            list.add("цены ингредиентов: " + ingredientPrices.size());
        }
        if (recipeIngredients.size() > 0) {
            list.add("рецепты: " + recipeIngredients.size());
        }
        return list;
    }

    private UnitOfMeasure convertToEntity(UnitOfMeasureDTO unitOfMeasureDto) {
        return modelMapper.map(unitOfMeasureDto, UnitOfMeasure.class);
    }

    private UnitOfMeasureDTO convertToDto(UnitOfMeasure unitOfMeasure) {
        return modelMapper.map(unitOfMeasure, UnitOfMeasureDTO.class);
    }

    @Override
    @Transactional
    public void moveToRecycleBin(UnitOfMeasureDTO unitOfMeasureDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(unitOfMeasureDTO.getFullName());
        recycleBin.setEntityName(EntityNamesConsts.UNIT_OF_MEASURE);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        unitOfMeasureRepository.softDelete(unitOfMeasureDTO.getId());
    }
}
