package by.weekmenu.api.service;

import by.weekmenu.api.dto.BaseUnitOfMeasureDto;
import by.weekmenu.api.entity.BaseUnitOfMeasure;
import by.weekmenu.api.repository.BaseUnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class BaseUnitOfMeasureServiceImp implements CrudService<BaseUnitOfMeasureDto, Long>, BaseUnitOfMeasureService {

    private final BaseUnitOfMeasureRepository baseUnitOfMeasureRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BaseUnitOfMeasureDto save(BaseUnitOfMeasureDto entityDto) {
        return convertToDto(baseUnitOfMeasureRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public BaseUnitOfMeasureDto findById(Long id) {
        return convertToDto(baseUnitOfMeasureRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        baseUnitOfMeasureRepository.deleteById(id);
    }

    @Override
    public List<BaseUnitOfMeasureDto> findAll() {
        List<BaseUnitOfMeasure> list = new ArrayList<>();
        baseUnitOfMeasureRepository.findAll().forEach(list::add);

        return list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BaseUnitOfMeasure findByShortName(String shortName) {
        return baseUnitOfMeasureRepository
                .findByShortNameIgnoreCase(shortName).orElse(null);
    }

    @Override
    public BaseUnitOfMeasure findByFullName(String fullName) {
        return baseUnitOfMeasureRepository
                .findByFullNameIgnoreCase(fullName).orElse(null);
    }

    private BaseUnitOfMeasure convertToEntity(BaseUnitOfMeasureDto baseUnitOfMeasureDto) {
        return modelMapper.map(baseUnitOfMeasureDto, BaseUnitOfMeasure.class);
    }

    private BaseUnitOfMeasureDto convertToDto(BaseUnitOfMeasure baseUnitOfMeasure) {
        return modelMapper.map(baseUnitOfMeasure, BaseUnitOfMeasureDto.class);
    }
}
