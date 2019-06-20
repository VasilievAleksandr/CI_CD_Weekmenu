package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.entity.Ingredient;
import by.weekmenu.api.entity.IngredientUnitOfMeasure;
import by.weekmenu.api.entity.OwnershipName;
import by.weekmenu.api.entity.UnitOfMeasure;
import by.weekmenu.api.repository.IngredientRepository;
import by.weekmenu.api.repository.IngredientUnitOfMeasureRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final OwnershipRepository ownershipRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public IngredientDto save(IngredientDto entityDto) {
        Ingredient ingredient = convertToEntity(entityDto);
        if (entityDto.getId() != null) {
            ingredientUnitOfMeasureRepository.deleteIngredientUnitOfMeasuresById_IngredientId(ingredient.getId());
        }
        ownershipRepository.findByName(OwnershipName.ADMIN).ifPresent(ingredient::setOwnership);
        ingredientRepository.save(ingredient);
        if (entityDto.getUnitOfMeasureEquivalent()!=null) {
            Map<String, BigDecimal> map = entityDto.getUnitOfMeasureEquivalent();
            map.forEach((k, v) -> {
                UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByFullNameIgnoreCase(k).orElse(null);
                IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure();
                ingredientUnitOfMeasure.setId(new IngredientUnitOfMeasure.Id(ingredient.getId(), unitOfMeasure.getId()));
                ingredientUnitOfMeasure.setIngredient(ingredient);
                ingredientUnitOfMeasure.setUnitOfMeasure(unitOfMeasure);
                ingredientUnitOfMeasure.setEquivalent(v);
                ingredientUnitOfMeasureRepository.save(ingredientUnitOfMeasure);
            });
        }
        return convertToDto(ingredient);
    }

    @Override
    public IngredientDto findById(Long id) {
        return convertToDto(ingredientRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ingredientUnitOfMeasureRepository.deleteIngredientUnitOfMeasuresById_IngredientId(id);
        ingredientRepository.deleteById(id);
    }

    @Override
    public List<IngredientDto> findAll() {
        return StreamSupport.stream(ingredientRepository.findAll().spliterator(), false)
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Ingredient convertToEntity(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, Ingredient.class);
    }

    private IngredientDto convertToDto(Ingredient ingredient) {
        IngredientDto ingredientDto = modelMapper.map(ingredient, IngredientDto.class);
        List<IngredientUnitOfMeasure> ingredientUOMlist = ingredientUnitOfMeasureRepository.findAllById_IngredientId(ingredient.getId());
        Map<String, BigDecimal> map = new HashMap<>();
        ingredientUOMlist.forEach(ingredientUnitOfMeasure -> map.put(unitOfMeasureRepository.findById(ingredientUnitOfMeasure.getUnitOfMeasure().getId()).orElse(null).getFullName(),
                ingredientUnitOfMeasure.getEquivalent()));
        ingredientDto.setUnitOfMeasureEquivalent(map);
        return ingredientDto;
    }

    @Override
    public Ingredient findByName(String name) {
        return ingredientRepository.findByNameIgnoreCase(name).orElse(null);
    }
}
