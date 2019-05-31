package by.weekmenu.api.service;

import by.weekmenu.api.dto.UnitOfMeasureDto;
import by.weekmenu.api.entity.UnitOfMeasure;

public interface UnitOfMeasureService {

    UnitOfMeasure findByShortName(String shortName);
    UnitOfMeasure findByFullName(String fullName);
}
