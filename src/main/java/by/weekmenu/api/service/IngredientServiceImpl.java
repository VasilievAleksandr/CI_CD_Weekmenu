package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.dto.IngredientPriceDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
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
    private final RegionRepository regionRepository;
    private final IngredientPriceRepository ingredientPriceRepository;
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
            saveIngredientUOM(entityDto, ingredient);
        }
        if (entityDto.getIngredientPrices()!=null) {
            saveIngredientPrice(entityDto, ingredient);
        }
        return convertToDto(ingredient);
    }

    private void saveIngredientPrice(IngredientDto entityDto, Ingredient ingredient) {
        Set<IngredientPriceDTO> ingredientPricesDTO = entityDto.getIngredientPrices();
        ingredientPricesDTO.forEach(ingredientPriceDTO -> {
            IngredientPrice ingredientPrice = new IngredientPrice();
            Region region = regionRepository.findByNameIgnoreCase(ingredientPriceDTO.getRegionName()).orElse(null);
            ingredientPrice.setId(new IngredientPrice.Id(ingredient.getId(), Objects.requireNonNull(region).getId()));
            UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByFullNameIgnoreCase(ingredientPriceDTO.getUnitOfMeasureName()).orElse(null);
            ingredientPrice.setUnitOfMeasure(unitOfMeasure);
            ingredientPrice.setIngredient(ingredient);
            ingredientPrice.setRegion(region);
            ingredientPrice.setPriceValue(ingredientPriceDTO.getPriceValue());
            ingredientPrice.setQuantity(ingredientPriceDTO.getQuantity());
            ingredientPriceRepository.save(ingredientPrice);
        });
    }

    private void saveIngredientUOM(IngredientDto entityDto, Ingredient ingredient) {
        Map<String, BigDecimal> map = entityDto.getUnitOfMeasureEquivalent();
        map.forEach((k, v) -> {
            UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByFullNameIgnoreCase(k).orElse(null);
            IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure();
            ingredientUnitOfMeasure.setId(new IngredientUnitOfMeasure.Id(ingredient.getId(), Objects.requireNonNull(unitOfMeasure).getId()));
            ingredientUnitOfMeasure.setIngredient(ingredient);
            ingredientUnitOfMeasure.setUnitOfMeasure(unitOfMeasure);
            ingredientUnitOfMeasure.setEquivalent(v);
            ingredientUnitOfMeasureRepository.save(ingredientUnitOfMeasure);
        });
    }

    @Override
    public IngredientDto findById(Long id) {
        return convertToDto(ingredientRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ingredientUnitOfMeasureRepository.deleteIngredientUnitOfMeasuresById_IngredientId(id);
        ingredientPriceRepository.deleteIngredientPricesById_IngredientId(id);
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
        ingredientUOMlist.forEach(ingredientUnitOfMeasure ->
                map.put(Objects.requireNonNull(unitOfMeasureRepository.findById(ingredientUnitOfMeasure.getUnitOfMeasure().getId())
                                .orElse(null)).getFullName(),
                ingredientUnitOfMeasure.getEquivalent()));
        ingredientDto.setUnitOfMeasureEquivalent(map);

        Set<IngredientPriceDTO> ingredientPriceDTOS = new HashSet<>();
        Set<IngredientPrice> ingredientPrices = ingredientPriceRepository.findAllById_IngredientId(ingredient.getId());
        for(IngredientPrice tempIngredientPrice : ingredientPrices) {
            IngredientPriceDTO ingredientPriceDTO = new IngredientPriceDTO();
            ingredientPriceDTO.setUnitOfMeasureName(tempIngredientPrice.getUnitOfMeasure().getFullName());
            ingredientPriceDTO.setRegionName(tempIngredientPrice.getRegion().getName());
            ingredientPriceDTO.setPriceValue(tempIngredientPrice.getPriceValue());
            ingredientPriceDTO.setQuantity(tempIngredientPrice.getQuantity());
            regionRepository.findByNameIgnoreCase(tempIngredientPrice.getRegion().getName())
                    .ifPresent(region -> ingredientPriceDTO.setCurrencyCode(region.getCountry().getCurrency().getCode()));
            ingredientPriceDTOS.add(ingredientPriceDTO);
        }
        ingredientDto.setIngredientPrices(ingredientPriceDTOS);
        return ingredientDto;
    }

    @Override
    public Ingredient findByName(String name) {
        return ingredientRepository.findByNameIgnoreCase(name).orElse(null);
    }
}
