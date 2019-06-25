package by.weekmenu.api.service;

import by.weekmenu.api.dto.RegionDto;

public interface RegionService extends CrudService<RegionDto, Long> {

    RegionDto findByName(String name);
}
