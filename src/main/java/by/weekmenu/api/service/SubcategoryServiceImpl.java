package by.weekmenu.api.service;


import by.weekmenu.api.dto.SubcategoryDto;
import by.weekmenu.api.entity.Subcategory;
import by.weekmenu.api.repository.CategoryRepository;
import by.weekmenu.api.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubcategoryServiceImpl implements SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public SubcategoryDto save(SubcategoryDto entityDto) {
        return convertToDto(subcategoryRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public SubcategoryDto findById(Long id) {
        return convertToDto(subcategoryRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {
        subcategoryRepository.deleteById(id);
    }

    @Override
    public List<SubcategoryDto> findAll() {
        List<Subcategory> subcategory = new ArrayList<>();
        subcategoryRepository.findAll().forEach(subcategory::add);
        return subcategory.stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubcategoryDto findByName(String name) {
        Optional<Subcategory> subcategory = subcategoryRepository.findByNameIgnoreCase(name);
        return subcategory.map(this::convertToDto).orElse(null);
    }

    private SubcategoryDto convertToDto(Subcategory region) { return modelMapper.map(region, SubcategoryDto.class);
    }

    private Subcategory convertToEntity(SubcategoryDto subcategoryDto) {
        Subcategory subcategory = modelMapper.map(subcategoryDto, Subcategory.class);
        categoryRepository.findByNameIgnoreCase(subcategoryDto.getCategoryName()).ifPresent(subcategory::setCategory);
        return subcategory;
    }
}
