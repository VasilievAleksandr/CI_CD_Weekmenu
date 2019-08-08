package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientDTO;
import by.weekmenu.api.dto.IngredientPriceDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.EntityNamesConsts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final OwnershipRepository ownershipRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;
    private final RegionRepository regionRepository;
    private final IngredientPriceRepository ingredientPriceRepository;
    private final RecipeService recipeService;
    private final RecycleBinRepository recycleBinRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public IngredientDTO save(IngredientDTO entityDto) {
        if (entityDto.getId() != null) {
            ingredientUnitOfMeasureRepository.deleteIngredientUnitOfMeasuresById_IngredientId(entityDto.getId());
            ingredientPriceRepository.deleteIngredientPricesById_IngredientId(entityDto.getId());
        }
        Ingredient ingredient = convertToEntity(entityDto);
        ownershipRepository.findByName("ADMIN").ifPresent(ingredient::setOwnership);
        ingredientRepository.save(ingredient);
        if (entityDto.getUnitOfMeasureEquivalent()!=null) {
            saveIngredientUOM(entityDto, ingredient);
        }
        if (entityDto.getIngredientPrices()!=null) {
            saveIngredientPrice(entityDto, ingredient);
        }
        recipeService.updateRecipes(ingredient.getId());
        return convertToDto(ingredient);
    }

    private void saveIngredientPrice(IngredientDTO entityDto, Ingredient ingredient) {
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

    private void saveIngredientUOM(IngredientDTO entityDto, Ingredient ingredient) {
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
    public IngredientDTO findById(Long id) {
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
    public List<IngredientDTO> findAll() {
        return ingredientRepository.findAllByIsArchivedIsFalse().stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Ingredient convertToEntity(IngredientDTO ingredientDto) {
        return modelMapper.map(ingredientDto, Ingredient.class);
    }

    private IngredientDTO convertToDto(Ingredient ingredient) {
        if (ingredient!=null) {
            IngredientDTO ingredientDto = modelMapper.map(ingredient, IngredientDTO.class);
            List<IngredientUnitOfMeasure> ingredientUOMlist = ingredientUnitOfMeasureRepository.findAllById_IngredientId(ingredient.getId());
            Map<String, BigDecimal> map = new HashMap<>();
            ingredientUOMlist.forEach(ingredientUnitOfMeasure ->
                    map.put(Objects.requireNonNull(unitOfMeasureRepository.findById(ingredientUnitOfMeasure.getUnitOfMeasure().getId())
                                    .orElse(null)).getFullName(),
                            ingredientUnitOfMeasure.getEquivalent()));
            ingredientDto.setUnitOfMeasureEquivalent(map);

            Set<IngredientPriceDTO> ingredientPriceDTOS = new HashSet<>();
            Set<IngredientPrice> ingredientPrices = ingredientPriceRepository.findAllById_IngredientId(ingredient.getId());
            for (IngredientPrice tempIngredientPrice : ingredientPrices) {
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
        } else {
            return null;
        }
    }

    @Override
    public Ingredient findByName(String name) {
        return ingredientRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> findAllUnitsOfMeasure(String name) {
        Optional<Ingredient> ingredient = ingredientRepository.findByNameIgnoreCase(name);
        List<String> unitOfMeasureShortNames = new ArrayList<>();
        if (ingredient.isPresent()) {
            List<IngredientUnitOfMeasure> unitOfMeasureList =
                    ingredientUnitOfMeasureRepository.findAllById_IngredientId(ingredient.get().getId());
            for (IngredientUnitOfMeasure ingredientUnitOfMeasure : unitOfMeasureList) {
                unitOfMeasureShortNames.add(ingredientUnitOfMeasure.getUnitOfMeasure().getShortName());
            }
        }
        return unitOfMeasureShortNames;
    }

    @Override
    public List<String> checkConnectedElements(Long id) {
        List<String> list = new ArrayList<>();
        Set<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllById_IngredientId(id);
        if (recipeIngredients.size() > 0) {
            list.add("Рецепты: " + recipeIngredients.size());
        }
        return list;
    }

    @Override
    @Transactional
    public void moveToRecycleBin(IngredientDTO ingredientDto) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(ingredientDto.getName());
        recycleBin.setEntityName(EntityNamesConsts.INGREDIENT);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        ingredientRepository.softDelete(ingredientDto.getId());
    }
}
