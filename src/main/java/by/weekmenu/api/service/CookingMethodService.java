package by.weekmenu.api.service;

import by.weekmenu.api.dto.CookingMethodDTO;
import by.weekmenu.api.entity.CookingMethod;

import java.util.List;

public interface CookingMethodService extends CrudService<CookingMethodDTO, Integer> {

    CookingMethod findByName(String name);
    List<String> checkConnectedElements(Integer id);
    void moveToRecycleBin(CookingMethodDTO cookingMethodDTO);
}