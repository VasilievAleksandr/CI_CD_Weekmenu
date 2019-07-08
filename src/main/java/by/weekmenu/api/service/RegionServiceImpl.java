package by.weekmenu.api.service;

import by.weekmenu.api.dto.RegionDto;
import by.weekmenu.api.entity.Region;
import by.weekmenu.api.repository.CountryRepository;
import by.weekmenu.api.repository.RegionRepository;
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
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;

    @Override
    public RegionDto save(RegionDto entityDto) {
        return convertToDto(regionRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public RegionDto findById(Long id) {
        return convertToDto(regionRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {
        regionRepository.deleteById(id);
    }

    @Override
    public List<RegionDto> findAll() {
        List<Region> regions = new ArrayList<>();
        regionRepository.findAll().forEach(regions::add);
        return regions.stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RegionDto findByName(String name) {
        Optional<Region> region = regionRepository.findByNameIgnoreCase(name);
        return region.map(this::convertToDto).orElse(null);
    }

    private RegionDto convertToDto(Region region) { return modelMapper.map(region, RegionDto.class);
    }

    private Region convertToEntity(RegionDto regionDto) {
        Region region = modelMapper.map(regionDto, Region.class);
        countryRepository.findByNameIgnoreCase(regionDto.getCountryName()).ifPresent(region::setCountry);
        return region;
    }
}
