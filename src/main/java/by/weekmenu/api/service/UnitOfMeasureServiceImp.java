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
    @Transactional
    public void delete(Long id) {
        UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findById(id).orElse(null);
        if (unitOfMeasure!=null) {
            RecycleBin recycleBin = new RecycleBin();
            recycleBin.setElementName(unitOfMeasure.getFullName());
            recycleBin.setEntityName(EntityNamesConsts.UNIT_OF_MEASURE);
            recycleBin.setDeleteDate(LocalDateTime.now());
            recycleBinRepository.save(recycleBin);
            unitOfMeasureRepository.softDelete(id);
        }
    }

    @Override
    public List<UnitOfMeasureDTO> findAll() {
        List<UnitOfMeasure> list = new ArrayList<>();
        list.addAll(unitOfMeasureRepository.findAllByIsArchivedIsFalse());
        return list.stream()
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
        if (ingredientUnitOfMeasures.size() > 0 || ingredientPrices.size() > 0 || recipeIngredients.size() > 0) {
            list.add("ингредиенты: " + ingredientUnitOfMeasures.size());
            list.add("цены ингредиентов: " + ingredientPrices.size());
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
}
