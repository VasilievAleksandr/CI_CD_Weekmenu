package by.weekmenu.api.service;

import by.weekmenu.api.dto.MenuDTO;
import by.weekmenu.api.dto.MenuRecipeDTO;
import by.weekmenu.api.entity.Menu;
import by.weekmenu.api.entity.MenuRecipe;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.MenuCalculations;
import by.weekmenu.api.utils.EntityNamesConsts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;
    private final OwnershipRepository ownershipRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final RecipeRepository recipeRepository;
    private final MealTypeRepository mealTypeRepository;
    private final MenuRecipeRepository menuRecipeRepository;
    private final MenuCalculations menuCalculations;
    private final RecycleBinRepository recycleBinRepository;
    private final ModelMapper modelMapper;

    @Override
    public void updateMenus(Long recipeId) {
        List<MenuRecipe> menuRecipes = menuRecipeRepository.findAllByRecipe_Id(recipeId);
        menuRecipes.forEach(menuRecipe -> {
            Menu menu = menuRecipe.getMenu();
            MenuDTO menuDTO = convertToDTO(menu);
            menuCalculations.calculateCPFC(menuDTO, menu);
            menuRepository.save(menu);
        });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        menuRecipeRepository.deleteAllByMenu_Id(id);
        //TODO delete recipePrices
        menuRepository.deleteById(id);
    }

    @Override
    @Transactional
    public MenuDTO save(MenuDTO menuDTO) {
        if (menuDTO.getId()!=null) {
            menuRecipeRepository.deleteAllByMenu_Id(menuDTO.getId());
        }
        Menu menu = convertToEntity(menuDTO);
        menuRepository.save(menu);
        if (menuDTO.getMenuRecipeDTOS()!=null) {
            saveMenuRecipes(menuDTO, menu);
            //TODO do calculations
            menuCalculations.calculateCPFC(menuDTO, menu);
            menuRepository.save(menu);
        }
        return convertToDTO(menu);
    }

    private void saveMenuRecipes(MenuDTO menuDTO, Menu menu) {
        menuDTO.getMenuRecipeDTOS().forEach(menuRecipeDTO -> {
                MenuRecipe menuRecipe = new MenuRecipe();
                menuRecipe.setMenu(menu);
                recipeRepository.findByNameIgnoreCase(menuRecipeDTO.getRecipeName()).ifPresent(menuRecipe::setRecipe);
                mealTypeRepository.findByNameIgnoreCase(menuRecipeDTO.getMealTypeName()).ifPresent(menuRecipe::setMealType);
                menuRecipe.setDayOfWeek(menuRecipeDTO.getDayOfWeek());
                menuRecipeRepository.save(menuRecipe);
            });
    }

    @Override
    public MenuDTO findById(Long id) {
        return convertToDTO(menuRepository.findById(id).orElse(null));
    }

    @Override
    public List<MenuDTO> findAll() {
        return menuRepository.findAllByIsArchivedIsFalse().stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void moveToRecycleBin(MenuDTO menuDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(menuDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.MENU);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        menuRepository.softDelete(menuDTO.getId());
    }

    private Menu convertToEntity(MenuDTO menuDTO) {
        Menu menu = modelMapper.map(menuDTO, Menu.class);
        ownershipRepository.findByName(menuDTO.getOwnershipName()).ifPresent(menu::setOwnership);
        if (menuDTO.getMenuCategoryName()!=null) {
            menuCategoryRepository.findByNameIgnoreCase(menuDTO.getMenuCategoryName()).ifPresent(menu::setMenuCategory);
        }
        return menu;
    }

    private MenuDTO convertToDTO(Menu menu) {
        if (menu!=null) {
            MenuDTO menuDTO = modelMapper.map(menu, MenuDTO.class);
            menuDTO.setMenuCategoryName(menu.getMenuCategory().getName());
            menuDTO.setOwnershipName(menu.getOwnership().getName());

            Set<MenuRecipeDTO> menuRecipeDTOS = new HashSet<>();
            menuRecipeRepository.findAllByMenu_Id(menu.getId())
                .forEach(menuRecipe -> menuRecipeDTOS.add(modelMapper.map(menuRecipe, MenuRecipeDTO.class)));
            menuDTO.setMenuRecipeDTOS(menuRecipeDTOS);
            return menuDTO;
        } else {
            return null;
        }
    }

    @Override
    public List<String> checkConnectedElements(Long id) {
        return new ArrayList<>();
    }
}
