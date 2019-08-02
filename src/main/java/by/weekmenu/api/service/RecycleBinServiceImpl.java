package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecycleBinDTO;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.entity.UnitOfMeasure;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.utils.EntityNamesConsts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecycleBinServiceImpl implements RecycleBinService {

    private final RecycleBinRepository recycleBinRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void restoreElement(Long id) {
        Optional<RecycleBin> recycleBinElement = recycleBinRepository.findById(id);
        if (recycleBinElement.isPresent()) {
             String entityName = recycleBinElement.get().getEntityName();
             switch (entityName) {
                 case EntityNamesConsts.UNIT_OF_MEASURE:
                     Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository
                             .findByFullNameIgnoreCase(recycleBinElement.get().getElementName());
                     unitOfMeasure.ifPresent(uom -> unitOfMeasureRepository.restore(uom.getId()));
                     recycleBinRepository.delete(recycleBinElement.get());
                     break;
             }
        }
    }

    @Override
    public List<RecycleBinDTO> findAll() {
        return StreamSupport.stream(recycleBinRepository.findAll().spliterator(), false)
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteElement(Long id) {
        Optional<RecycleBin> recycleBinElement = recycleBinRepository.findById(id);
        if (recycleBinElement.isPresent()) {
            String entityName = recycleBinElement.get().getEntityName();
            switch (entityName) {
                case EntityNamesConsts.UNIT_OF_MEASURE:
                    Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository
                            .findByFullNameIgnoreCase(recycleBinElement.get().getElementName());
                    unitOfMeasure.ifPresent(uom -> unitOfMeasureRepository.deleteById(uom.getId()));
                    recycleBinRepository.deleteById(id);
                    break;
            }
        }
    }

    @Override
    public RecycleBin findById(Long id) {
        return recycleBinRepository.findById(id).orElse(null);
    }

    private RecycleBinDTO convertToDto(RecycleBin recycleBin) {
        return modelMapper.map(recycleBin, RecycleBinDTO.class);
    }
}
