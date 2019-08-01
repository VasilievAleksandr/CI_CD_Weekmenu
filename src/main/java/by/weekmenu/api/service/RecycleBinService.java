package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecycleBinDTO;
import by.weekmenu.api.entity.RecycleBin;

import java.util.List;

public interface RecycleBinService {

    void restoreElement(Long id);
    List<RecycleBinDTO> findAll();
    void deleteElement(Long id);
    RecycleBin findById(Long id);
}
