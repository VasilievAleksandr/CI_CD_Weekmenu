package by.weekmenu.api.service;

import by.weekmenu.api.dto.CookingMethodDTO;
import by.weekmenu.api.entity.CookingMethod;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.CookingMethodRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CookingMethodServiceImpl implements CookingMethodService{

    private final CookingMethodRepository cookingMethodRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final ModelMapper modelMapper;

    @Override
    public CookingMethodDTO save(CookingMethodDTO entityDTO) {
        return convertToDTO(cookingMethodRepository.save(convertToEntity(entityDTO)));
    }

    @Override
    public CookingMethodDTO findById(Integer id) {
        return convertToDTO(cookingMethodRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Integer id) {
        cookingMethodRepository.deleteById(id);
    }

    @Override
    public List<CookingMethodDTO> findAll() {
        return cookingMethodRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CookingMethod findByName(String name) {
        return cookingMethodRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> getAllCookingMethodNames() {
        return cookingMethodRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(CookingMethod::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> checkConnectedElements(Integer id) {
        List<String> list = new ArrayList<>();
        //TODO check connected recipes
        return list;
    }

    @Override
    @Transactional
    public void moveToRecycleBin(CookingMethodDTO cookingMethodDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(cookingMethodDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.COOKING_METHOD);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        cookingMethodRepository.softDelete(cookingMethodDTO.getId());
    }

    private CookingMethod convertToEntity(CookingMethodDTO cookingMethodDTO) {
        return modelMapper.map(cookingMethodDTO, CookingMethod.class);
    }

    private CookingMethodDTO convertToDTO(CookingMethod cookingMethod) {
        return modelMapper.map(cookingMethod, CookingMethodDTO.class);
    }
}
