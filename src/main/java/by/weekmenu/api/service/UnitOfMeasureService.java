package by.weekmenu.api.service;

import by.weekmenu.api.dto.UnitOfMeasureDto;
import by.weekmenu.api.entity.UnitOfMeasure;

public interface UnitOfMeasureService extends CrudService<UnitOfMeasureDto, Long> {

    UnitOfMeasure findByShortName(String shortName);
    UnitOfMeasure findByFullName(String fullName);
}
