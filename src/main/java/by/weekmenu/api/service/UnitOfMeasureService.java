package by.weekmenu.api.service;

import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.entity.UnitOfMeasure;

import java.util.List;

public interface UnitOfMeasureService extends CrudService<UnitOfMeasureDTO, Long> {

    UnitOfMeasure findByShortName(String shortName);
    UnitOfMeasure findByFullName(String fullName);
    List<String> checkConnectedElements(Long id);
    void moveToRecycleBin(UnitOfMeasureDTO unitOfMeasureDTO);

}
