package by.weekmenu.api.service;

import by.weekmenu.api.dto.*;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.EntityNamesConsts;
import by.weekmenu.api.utils.RecipeCalculation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final OwnershipRepository ownershipRepository;
    private final CookingMethodRepository cookingMethodRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final CookingStepRepository cookingStepRepository;
    private final RecipePriceRepository recipePriceRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final MenuRecipeRepository menuRecipeRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final RecipeSubcategoryRepository recipeSubcategoryRepository;
    private final RecipeCalculation recipeCalculation;
    private final MenuService menuService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RecipeDTO save(RecipeDTO entityDto) {
        if (entityDto.getId()!=null) {
            recipeIngredientRepository.deleteById_RecipeId(entityDto.getId());
            recipePriceRepository.deleteById_RecipeId(entityDto.getId());
            cookingStepRepository.deleteAllByRecipe_Id(entityDto.getId());
        }
        Recipe recipe = convertToEntity(entityDto);
        recipeRepository.save(recipe);
        if (entityDto.getRecipeIngredients()!=null) {
            recipeCalculation.calculateRecipePrice(entityDto, recipe);
            saveRecipeIngredients(entityDto, recipe);
            recipeCalculation.calculateCPFC(entityDto, recipe);
            recipeCalculation.calculateGramsPerPortion(entityDto, recipe);
            recipeRepository.save(recipe);
        }
        if (entityDto.getCookingSteps()!=null) {
            saveCookingSteps(entityDto, recipe);
        }
        menuService.updateMenus(recipe.getId());
        return convertToDto(recipe);
    }

    private void saveCookingSteps(RecipeDTO entityDto, Recipe recipe) {
        Set<CookingStepDTO> cookingSteps = entityDto.getCookingSteps();
        cookingSteps.forEach(cookingStepDTO -> {
            CookingStep cookingStep = modelMapper.map(cookingStepDTO, CookingStep.class);
            cookingStep.setRecipe(recipe);
            cookingStepRepository.save(cookingStep);
        });
    }

    private void saveRecipeIngredients(RecipeDTO entityDto, Recipe recipe) {
        Set<RecipeIngredientDTO> recipeIngredients = entityDto.getRecipeIngredients();
        recipeIngredients.forEach(recipeIngredientDTO -> {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(recipeIngredientDTO.getIngredientName()).orElse(null);
            if (ingredient!=null) {
                recipeIngredient.setId(new RecipeIngredient.Id(ingredient.getId(), recipe.getId()));
                recipeIngredient.setIngredient(ingredient);
            }
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setQty(recipeIngredientDTO.getQuantity());
            unitOfMeasureRepository
                    .findByShortNameIgnoreCase(recipeIngredientDTO.getUnitOfMeasureShortName()).ifPresent(recipeIngredient::setUnitOfMeasure);
            recipeIngredientRepository.save(recipeIngredient);
        });
    }

    @Override
    public RecipeDTO findById(Long id) {
        return convertToDto(recipeRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cookingStepRepository.deleteAllByRecipe_Id(id);
        recipeIngredientRepository.deleteById_RecipeId(id);
        recipePriceRepository.deleteById_RecipeId(id);
        recipeRepository.deleteById(id);
    }

    @Override
    public List<RecipeDTO> findAllByFilter(String recipeName, Short totalCookingTime,
                                           String recipeCategoryName, String recipeSubcategoryName,
                                           BigDecimal recipeCalories) {
        List<RecipeDTO> result = new ArrayList<>();
        List<RecipeDTO> allByRecipeCategory = recipeRepository.findAllByRecipeCategoryName(recipeCategoryName)
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
        List<RecipeDTO> byRecipeFilter = recipeRepository.findAllByFilter(recipeName, totalCookingTime, recipeCalories).stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
        List<RecipeDTO> allByRecipeSubcategory = recipeRepository.findAllByRecipeSubcategoryName(recipeSubcategoryName)
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
        if (StringUtils.isEmpty(recipeName) && totalCookingTime == null &&
                recipeCalories == null && StringUtils.isEmpty(recipeSubcategoryName)) {
            result.addAll(allByRecipeCategory);
        } else if (StringUtils.isEmpty(recipeCategoryName) && StringUtils.isEmpty(recipeSubcategoryName)) {
            result.addAll(byRecipeFilter);
        } else if (StringUtils.isEmpty(recipeName) && totalCookingTime == null &&
                recipeCalories == null && StringUtils.isEmpty(recipeCategoryName)) {
            result.addAll(allByRecipeSubcategory);
        } else if (StringUtils.isEmpty(recipeCategoryName)) {
            result.addAll(byRecipeFilter.stream()
                    .filter(allByRecipeSubcategory::contains)
                    .collect(Collectors.toList()));
        } else if(StringUtils.isEmpty(recipeSubcategoryName)) {
            result.addAll(byRecipeFilter.stream()
                    .filter(allByRecipeCategory::contains)
                    .collect(Collectors.toList()));
        } else {
            result.addAll(byRecipeFilter.stream()
                    .filter(allByRecipeCategory::contains)
                    .filter(allByRecipeSubcategory::contains)
                    .collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public List<RecipeDTO> findAll() {
        return recipeRepository.findAllByIsArchivedIsFalse().stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RecipeDTO convertToDto(Recipe recipe) {
        if (recipe!=null) {
            RecipeDTO recipeDto = modelMapper.map(recipe, RecipeDTO.class);
            recipeDto.setCookingTime(recipe.getCookingTime().toString());
            recipeDto.setPreparingTime(recipe.getPreparingTime().toString());
            recipeDto.setCookingMethodName(recipe.getCookingMethod().getName());
            recipeDto.setCategoryNames(recipe.getRecipeCategories()
                    .stream().map(RecipeCategory::getName).collect(Collectors.toSet()));
            recipeDto.setSubcategoryNames(recipe.getRecipeSubcategories()
                    .stream().map(RecipeSubcategory::getName).collect(Collectors.toSet()));

            Set<RecipeIngredientDTO> recipeIngredientsDTOS = new HashSet<>();
            Set<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllById_RecipeId(recipe.getId());
            recipeIngredients.forEach(recipeIngredient -> {
                RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
                recipeIngredientDTO.setIngredientName(recipeIngredient.getIngredient().getName());
                recipeIngredientDTO.setQuantity(recipeIngredient.getQty());
                recipeIngredientDTO.setUnitOfMeasureShortName(recipeIngredient.getUnitOfMeasure().getShortName());
                recipeIngredientsDTOS.add(recipeIngredientDTO);
            });
            recipeDto.setRecipeIngredients(recipeIngredientsDTOS);

            Set<CookingStepDTO> cookingStepDTOS = new HashSet<>();
            Set<CookingStep> cookingSteps = cookingStepRepository.findAllByRecipe_Id(recipe.getId());
            cookingSteps.forEach(cookingStep -> {
                CookingStepDTO cookingStepDto = new CookingStepDTO();
                cookingStepDto.setId(cookingStep.getId());
                cookingStepDto.setPriority(cookingStep.getPriority());
                cookingStepDto.setDescription(cookingStep.getDescription());
                cookingStepDto.setImageLink(cookingStep.getImageLink());
                cookingStepDTOS.add(cookingStepDto);
            });
            recipeDto.setCookingSteps(cookingStepDTOS);

            List<RecipePrice> recipePrices = recipePriceRepository.findAllById_RecipeId(recipe.getId());
            Set<RecipePriceDTO> recipePriceDTOS = new HashSet<>();
            for (RecipePrice recipePrice : recipePrices) {
                RecipePriceDTO recipePriceDto = new RecipePriceDTO();
                recipePriceDto.setRecipeName(recipePrice.getRecipe().getName());
                recipePriceDto.setRegionName(recipePrice.getRegion().getName());
                recipePriceDto.setPriceValue(String.valueOf(recipePrice.getPriceValue()));
                recipePriceDto.setCurrencyCode(recipePrice.getRegion().getCountry().getCurrency().getCode());
                recipePriceDTOS.add(recipePriceDto);
            }
            recipeDto.setRecipePrices(recipePriceDTOS);
            return recipeDto;
        } else {
            return null;
        }
    }

    private Recipe convertToEntity(RecipeDTO recipeDto) {
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        ownershipRepository.findByName(recipeDto.getOwnershipName()).ifPresent(recipe::setOwnership);
        cookingMethodRepository.findByNameIgnoreCase(recipeDto.getCookingMethodName()).ifPresent(recipe::setCookingMethod);
        if (recipeDto.getCategoryNames()!=null) {
            for (String categoryName : recipeDto.getCategoryNames()) {
                recipeCategoryRepository.findByNameIgnoreCase(categoryName)
                        .ifPresent(recipe::addRecipeCategory);
            }
        }
        if (recipeDto.getSubcategoryNames()!=null) {
            for (String subcategoryName : recipeDto.getSubcategoryNames()) {
                recipeSubcategoryRepository.findByNameIgnoreCase(subcategoryName)
                        .ifPresent(recipe::addRecipeSubcategory);
            }
        }
        return recipe;
    }

    @Override
    public Recipe findByName(String name) {
        return recipeRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public void updateRecipes(Long ingredientId) {
        Set<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllById_IngredientId(ingredientId);
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            Recipe recipe = recipeIngredient.getRecipe();
            RecipeDTO recipeDTO = convertToDto(recipe);
            recipeCalculation.calculateCPFC(recipeDTO, recipe);
            recipeCalculation.calculateRecipePrice(recipeDTO, recipe);
            recipeCalculation.calculateGramsPerPortion(recipeDTO, recipe);
            recipeRepository.save(recipe);
        }
    }

    @Override
    public List<String> checkConnectedElements(Long id) {
        List<String> list = new ArrayList<>();
        List<MenuRecipe> menuRecipes = menuRecipeRepository.findAllByRecipe_Id(id);
        if (menuRecipes.size() > 0) {
            list.add("меню: " + menuRecipes.size());
        }
        return list;
    }

    @Override
    @Transactional
    public void moveToRecycleBin(RecipeDTO recipeDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(recipeDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.RECIPE);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        recipeRepository.softDelete(recipeDTO.getId());
    }
}
