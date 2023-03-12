package by.weekmenu.api.service;

import by.weekmenu.api.dto.CountryDTO;
import by.weekmenu.api.entity.Country;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.entity.Region;
import by.weekmenu.api.repository.CountryRepository;
import by.weekmenu.api.repository.CurrencyRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.repository.RegionRepository;
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
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;

    @Override
    public CountryDTO save(CountryDTO entityDto) {
        return convertToDto(countryRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public CountryDTO findById(Long id) {
        return convertToDto(countryRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void moveToRecycleBin(CountryDTO countryDTO) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(countryDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.COUNTRY);
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBinRepository.save(recycleBin);
        countryRepository.softDelete(countryDTO.getId());
    }

    @Override
    public List<CountryDTO> findAll() {
        return countryRepository.findAllByIsArchivedIsFalse().stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Country findByName(String name) {
        return countryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public Country findByAlphaCode2(String alphaCode2) {
        return countryRepository.findByAlphaCode2IgnoreCase(alphaCode2).orElse(null);
    }

    @Override
    public List<String> getAllCountryNames() {

        return countryRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(Country::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> checkConnectedElements(Long id) {
        List<String> list = new ArrayList<>();
        List<Region> regions = regionRepository.findAllByCountry_Id(id);
        if (regions.size() > 0) {
            list.add("регионы :" + regions.size());
        }
        return list;
    }

    private Country convertToEntity(CountryDTO countryDto) {
        Country country = modelMapper.map(countryDto, Country.class);
        currencyRepository.findByCodeIgnoreCase(countryDto.getCurrencyCode()).ifPresent(country::setCurrency);
        return country;
    }

    private CountryDTO convertToDto(Country country) {
        return modelMapper.map(country, CountryDTO.class);
    }
}
