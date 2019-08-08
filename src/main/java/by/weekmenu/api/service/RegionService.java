package by.weekmenu.api.service;

import by.weekmenu.api.dto.RegionDTO;

import java.util.List;

public interface RegionService extends CrudService<RegionDTO, Long> {

    RegionDTO findByName(String name);
    List<String> checkConnectedElements(Long id);
}
