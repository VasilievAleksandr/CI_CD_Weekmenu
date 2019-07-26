package by.weekmenu.api.service;


import by.weekmenu.api.dto.SubcategoryDto;

public interface SubcategoryService extends CrudService<SubcategoryDto, Long> {

    SubcategoryDto findByName(String name);
}
