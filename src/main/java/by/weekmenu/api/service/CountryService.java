package by.weekmenu.api.service;

import by.weekmenu.api.entity.Country;

public interface CountryService {

    Country findByName(String name);
    Country findByAlphaCode2(String alphaCode2);
}
