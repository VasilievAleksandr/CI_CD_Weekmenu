package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecycleBinDTO;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.EntityNamesConsts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecycleBinServiceImpl implements RecycleBinService {

    private final RecycleBinRepository recycleBinRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CurrencyRepository currencyRepository;
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientService ingredientService;
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final CookingMethodRepository cookingMethodRepository;
    private final RecipeSubcategoryRepository recipeSubcategoryRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MealTypeRepository mealTypeRepository;
    private final MenuRepository menuRepository;
    private final MenuService menuService;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void restoreElement(Long id) {
        Optional<RecycleBin> recycleBinElement = recycleBinRepository.findById(id);
        if (recycleBinElement.isPresent()) {
             String entityName = recycleBinElement.get().getEntityName();
             switch (entityName) {
                 case EntityNamesConsts.UNIT_OF_MEASURE:
                     unitOfMeasureRepository
                             .findByFullNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(uom -> unitOfMeasureRepository.restore(uom.getId()));
                     break;
                 case EntityNamesConsts.CURRENCY:
                     currencyRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(cur -> currencyRepository.restore(cur.getId()));
                     break;
                 case EntityNamesConsts.COUNTRY:
                     countryRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(country -> countryRepository.restore(country.getId()));
                     break;
                 case EntityNamesConsts.REGION:
                     regionRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(region -> regionRepository.restore(region.getId()));
                     break;
                 case EntityNamesConsts.INGREDIENT:
                     ingredientRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(ingredient -> ingredientRepository.restore(ingredient.getId()));
                     break;
                 case EntityNamesConsts.RECIPE:
                     recipeRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(recipe -> recipeRepository.restore(recipe.getId()));
                     break;
                 case EntityNamesConsts.RECIPE_CATEGORY:
                     recipeCategoryRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(recipeCategory -> recipeCategoryRepository.restore(recipeCategory.getId()));
                     break;
                 case EntityNamesConsts.RECIPE_SUBCATEGORY:
                     recipeSubcategoryRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(recipeSubcategory -> recipeSubcategoryRepository.restore(recipeSubcategory.getId()));
                     break;
                 case EntityNamesConsts.COOKING_METHOD:
                     cookingMethodRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(cookingMethod -> cookingMethodRepository.restore(cookingMethod.getId()));
                     break;
                 case EntityNamesConsts.MENU_CATEGORY:
                     menuCategoryRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(menuCategory -> menuCategoryRepository.restore(menuCategory.getId()));
                     break;
                 case EntityNamesConsts.MEAL_TYPE:
                     mealTypeRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(mealType -> mealTypeRepository.restore(mealType.getId()));
                     break;
                 case EntityNamesConsts.MENU:
                     menuRepository.findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(menu -> menuRepository.restore(menu.getId()));
                     break;
                 case EntityNamesConsts.INGREDIENT_CATEGORY:
                     ingredientCategoryRepository
                             .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                             .ifPresent(ingredientCategory -> ingredientCategoryRepository.restore(ingredientCategory.getId()));
                     break;
             }
            recycleBinRepository.delete(recycleBinElement.get());
        }
    }

    @Override
    public List<RecycleBinDTO> findAll() {
        return StreamSupport.stream(recycleBinRepository.findAll().spliterator(), false)
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteElement(Long id) {
        Optional<RecycleBin> recycleBinElement = recycleBinRepository.findById(id);
        if (recycleBinElement.isPresent()) {
            String entityName = recycleBinElement.get().getEntityName();
            switch (entityName) {
                case EntityNamesConsts.UNIT_OF_MEASURE:
                    unitOfMeasureRepository
                            .findByFullNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(uom -> unitOfMeasureRepository.deleteById(uom.getId()));
                    break;
                case EntityNamesConsts.CURRENCY:
                    currencyRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(cur -> currencyRepository.deleteById(cur.getId()));
                    break;
                case EntityNamesConsts.COUNTRY:
                    countryRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(country -> countryRepository.deleteById(country.getId()));
                    break;
                case EntityNamesConsts.REGION:
                    regionRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(region -> regionRepository.deleteById(region.getId()));
                    break;
                case EntityNamesConsts.INGREDIENT:
                    ingredientRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(ingredient -> ingredientService.delete(ingredient.getId()));
                    break;
                case EntityNamesConsts.RECIPE:
                    recipeRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(recipe -> recipeService.delete(recipe.getId()));
                    break;
                case EntityNamesConsts.RECIPE_CATEGORY:
                    recipeCategoryRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(recipeCategory -> recipeCategoryRepository.deleteById(recipeCategory.getId()));
                    break;
                case EntityNamesConsts.COOKING_METHOD:
                    cookingMethodRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(cookingMethod -> cookingMethodRepository.deleteById(cookingMethod.getId()));
                    break;
                case EntityNamesConsts.RECIPE_SUBCATEGORY:
                    recipeSubcategoryRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(recipeSubcategory -> recipeSubcategoryRepository.deleteById(recipeSubcategory.getId()));
                    break;
                case EntityNamesConsts.MENU_CATEGORY:
                    menuCategoryRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(menuCategory -> menuCategoryRepository.deleteById(menuCategory.getId()));
                    break;
                case EntityNamesConsts.MEAL_TYPE:
                    mealTypeRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(mealType -> mealTypeRepository.deleteById(mealType.getId()));
                    break;
                case EntityNamesConsts.MENU:
                    menuRepository.findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(menu -> menuService.delete(menu.getId()));
                    break;
                case EntityNamesConsts.INGREDIENT_CATEGORY:
                    ingredientCategoryRepository
                            .findByNameIgnoreCase(recycleBinElement.get().getElementName())
                            .ifPresent(ingredientCategory -> ingredientCategoryRepository.deleteById(ingredientCategory.getId()));
                    break;
            }
            recycleBinRepository.deleteById(id);
        }
    }

    @Override
    public RecycleBin findById(Long id) {
        return recycleBinRepository.findById(id).orElse(null);
    }

    private RecycleBinDTO convertToDto(RecycleBin recycleBin) {
        return modelMapper.map(recycleBin, RecycleBinDTO.class);
    }
}
