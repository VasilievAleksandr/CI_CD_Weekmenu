package by.weekmenu.api.service;

import by.weekmenu.api.dto.RegionDTO;
import by.weekmenu.api.entity.IngredientPrice;
import by.weekmenu.api.entity.RecipePrice;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.entity.Region;
import by.weekmenu.api.repository.*;
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
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final IngredientPriceRepository ingredientPriceRepository;
    private final RecipePriceRepository recipePriceRepository;
    private final ModelMapper modelMapper;

    @Override
    public RegionDTO save(RegionDTO entityDto) {
        return convertToDto(regionRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public RegionDTO findById(Long id) {
        return convertToDto(regionRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void moveToRecycleBin (RegionDTO regionDTO) {
                   RecycleBin recycleBin = new RecycleBin();
            recycleBin.setElementName(regionDTO.getName());
            recycleBin.setEntityName(EntityNamesConsts.REGION);
            recycleBin.setDeleteDate(LocalDateTime.now());
            recycleBinRepository.save(recycleBin);
            regionRepository.softDelete(regionDTO.getId());
        }

    @Override
    public List<RegionDTO> findAll() {
        return regionRepository.findAllByIsArchivedIsFalse().stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RegionDTO findByName(String name) {
        Optional<Region> region = regionRepository.findByNameIgnoreCase(name);
        return region.map(this::convertToDto).orElse(null);
    }

    @Override
    public List<String> checkConnectedElements(Long id) {
        List<String> list = new ArrayList<>();
        List<IngredientPrice> ingredientPrices = ingredientPriceRepository.findAllById_RegionId(id);
        List<RecipePrice> recipePrices = recipePriceRepository.findAllById_RegionId(id);
        if (ingredientPrices.size() > 0) {
            list.add("цены ингредиентов: " + ingredientPrices.size());
        }
        if (recipePrices.size() > 0) {
            list.add("цены рецептов: " + recipePrices.size());
        }
        return list;
    }

    private RegionDTO convertToDto(Region region) { return modelMapper.map(region, RegionDTO.class);
    }

    private Region convertToEntity(RegionDTO regionDto) {
        Region region = modelMapper.map(regionDto, Region.class);
        countryRepository.findByNameIgnoreCase(regionDto.getCountryName()).ifPresent(region::setCountry);
        return region;
    }
}
