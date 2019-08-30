package by.weekmenu.api.service;

import by.weekmenu.api.dto.CountryDTO;
import by.weekmenu.api.entity.Country;

import java.util.List;

public interface CountryService extends CrudService<CountryDTO, Long> {

    Country findByName(String name);
    Country findByAlphaCode2(String alphaCode2);
    List<String> getAllCountryNames();
    List<String> checkConnectedElements(Long id);
    void moveToRecycleBin (CountryDTO countryDTO);
}
