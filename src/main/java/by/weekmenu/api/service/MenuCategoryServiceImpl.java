package by.weekmenu.api.service;

import by.weekmenu.api.dto.MenuCategoryDTO;
import by.weekmenu.api.entity.MenuCategory;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.MenuCategoryRepository;
import by.weekmenu.api.repository.MenuRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.utils.EntityNamesConsts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuCategoryServiceImpl implements MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    @Override
    public MenuCategoryDTO save(MenuCategoryDTO entityDTO) {
        return convertToDTO(menuCategoryRepository.save(convertToEntity(entityDTO)));
    }

    @Override
    public MenuCategoryDTO findById(Integer id) {
        return convertToDTO(menuCategoryRepository.findById(id).orElse(null));
    }

    @Override
    public List<MenuCategoryDTO> findAll() {
        return menuCategoryRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MenuCategory findByName(String name) {
        return menuCategoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> checkConnectedElements(Integer id) {
        List<String> list = new ArrayList<>();
        Optional<MenuCategory> menuCategory = menuCategoryRepository.findById(id);
        Integer size = menuRepository.findAllByMenuCategory_IdAndIsArchivedIsFalse(id).size();
        if (menuCategory.isPresent() && size > 0) {
            list.add("меню: " + size);
        }
        return list;
    }

    @Override
    @Transactional
    public void moveToRecycleBin(MenuCategoryDTO menuCategoryDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(menuCategoryDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.MENU_CATEGORY);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        menuCategoryRepository.softDelete(menuCategoryDTO.getId());
    }

    private MenuCategory convertToEntity(MenuCategoryDTO menuCategoryDTO) {
        return modelMapper.map(menuCategoryDTO, MenuCategory.class);
    }

    private MenuCategoryDTO convertToDTO(MenuCategory menuCategory) {
        return modelMapper.map(menuCategory, MenuCategoryDTO.class);
    }
}
