package by.weekmenu.api.service;

import by.weekmenu.api.dto.MenuDTO;
import by.weekmenu.api.dto.MenuRecipeDTO;
import by.weekmenu.api.entity.Menu;
import by.weekmenu.api.entity.MenuRecipe;
import by.weekmenu.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;
    private final OwnershipRepository ownershipRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final RecipeRepository recipeRepository;
    private final MealTypeRepository mealTypeRepository;
    private final MenuRecipeRepository menuRecipeRepository;
    private final ModelMapper modelMapper;

    @Override
    public void updateMenus(Long recipeId) {

    }

    @Override
    public MenuDTO save(MenuDTO menuDTO) {
        Menu menu = convertToEntity(menuDTO);
        menuRepository.save(menu);
        if (menuDTO.getMenuRecipeDTOS()!=null) {
            //TODO do calculations
            saveMenuRecipes(menuDTO, menu);
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
        return null;
    }

    @Override
    public void moveToRecycleBin(MenuDTO entityDto) {

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
}
