package by.weekmenu.api.service;

import by.weekmenu.api.dto.RegionDTO;

public interface RegionService extends CrudService<RegionDTO, Long> {

    RegionDTO findByName(String name);
}
