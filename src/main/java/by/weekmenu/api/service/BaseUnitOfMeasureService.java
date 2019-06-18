package by.weekmenu.api.service;

import by.weekmenu.api.entity.BaseUnitOfMeasure;

public interface BaseUnitOfMeasureService {

    BaseUnitOfMeasure findByShortName(String shortName);
    BaseUnitOfMeasure findByFullName(String fullName);
}
