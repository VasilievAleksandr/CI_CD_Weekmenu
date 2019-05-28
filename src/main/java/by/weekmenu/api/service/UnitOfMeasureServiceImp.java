package by.weekmenu.api.service;

import by.weekmenu.api.dto.UnitOfMeasureDto;
import by.weekmenu.api.entity.UnitOfMeasure;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class UnitOfMeasureServiceImp implements CrudService<UnitOfMeasureDto, Long>, UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UnitOfMeasureDto save(UnitOfMeasureDto entityDto) {
        return convertToDto(unitOfMeasureRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public UnitOfMeasureDto findById(Long id) {
        return convertToDto(unitOfMeasureRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        unitOfMeasureRepository.deleteById(id);
    }

    @Override
    public List<UnitOfMeasureDto> findAll() {
        List<UnitOfMeasure> list = new ArrayList<>();
        unitOfMeasureRepository.findAll().forEach(list::add);

        return list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UnitOfMeasure findByShortName(String shortName) {
        Optional<UnitOfMeasure> uom = unitOfMeasureRepository
                .findByShortNameIgnoreCase(shortName);
        return uom.orElse(null);
    }

    @Override
    public UnitOfMeasure findByFullName(String fullName) {
        Optional<UnitOfMeasure> uom = unitOfMeasureRepository
                .findByFullNameIgnoreCase(fullName);
        return uom.orElse(null);
    }

    private UnitOfMeasure convertToEntity(UnitOfMeasureDto unitOfMeasureDto) {
        return modelMapper.map(unitOfMeasureDto, UnitOfMeasure.class);
    }

    private UnitOfMeasureDto convertToDto(UnitOfMeasure unitOfMeasure) {
        return modelMapper.map(unitOfMeasure, UnitOfMeasureDto.class);
    }
}
