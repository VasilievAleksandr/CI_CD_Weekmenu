package by.weekmenu.api.service;

import by.weekmenu.api.dto.CountryDto;
import by.weekmenu.api.entity.Country;

public interface CountryService extends CrudService<CountryDto, Long> {

    Country findByName(String name);
    Country findByAlphaCode2(String alphaCode2);
}
