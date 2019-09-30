package by.weekmenu.api.service;

import by.weekmenu.api.dto.MealTypeDTO;
import by.weekmenu.api.entity.MealType;

import java.util.List;

public interface MealTypeService extends CrudService<MealTypeDTO, Short> {

    MealType findByName(String name);
    List<String> checkConnectedElements(Short id);

}
