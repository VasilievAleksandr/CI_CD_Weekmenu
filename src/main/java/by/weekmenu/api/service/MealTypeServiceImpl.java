package by.weekmenu.api.service;

import by.weekmenu.api.dto.MealTypeDTO;
import by.weekmenu.api.entity.MealType;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.MenuRecipeRepository;
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
public class MealTypeServiceImpl implements MealTypeService {

    private final MealTypeRepository mealTypeRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final MenuRecipeRepository menuRecipeRepository;
    private final ModelMapper modelMapper;

    @Override
    public MealTypeDTO save(MealTypeDTO entityDTO) {
        return convertToDTO(mealTypeRepository.save(convertToEntity(entityDTO)));
    }

    @Override
    public MealTypeDTO findById(Short id) {
        return convertToDTO(mealTypeRepository.findById(id).orElse(null));
    }

    @Override
    public List<MealTypeDTO> findAll() {
        return mealTypeRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MealType findByName(String name) {
        return mealTypeRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public  MealType findByPriority (Integer priority){ return mealTypeRepository.findByPriority(priority).orElse(null);}

    @Override
    public List<String> checkConnectedElements(Short id) {
        List<String> list = new ArrayList<>();
        Optional<MealType> mealType = mealTypeRepository.findById(id);
        Integer size = menuRecipeRepository.findAllByMealType_Id(id).size();
        if (mealType.isPresent() && size > 0) {
            list.add("рецепты меню: " + size);
        }
        return list;
    }

    @Override
    @Transactional
    public void moveToRecycleBin(MealTypeDTO mealTypeDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(mealTypeDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.MEAL_TYPE);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        mealTypeRepository.softDelete(mealTypeDTO.getId());
    }

    private MealType convertToEntity(MealTypeDTO mealTypeDTO) {
        return modelMapper.map(mealTypeDTO, MealType.class);
    }

    private MealTypeDTO convertToDTO(MealType mealType) {
        return modelMapper.map(mealType, MealTypeDTO.class);
    }
}
