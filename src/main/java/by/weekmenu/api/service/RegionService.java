package by.weekmenu.api.service;

import by.weekmenu.api.dto.RegionDto;
import by.weekmenu.api.entity.Region;

public interface RegionService extends CrudService<RegionDto, Long> {

    Region findByName(String name);
}
