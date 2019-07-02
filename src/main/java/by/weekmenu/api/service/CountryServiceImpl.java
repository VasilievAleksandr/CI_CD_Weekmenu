package by.weekmenu.api.service;

import by.weekmenu.api.dto.CountryDto;
import by.weekmenu.api.entity.Country;
import by.weekmenu.api.repository.CountryRepository;
import by.weekmenu.api.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final ModelMapper modelMapper;

    @Override
    public CountryDto save(CountryDto entityDto) {
        return convertToDto(countryRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public CountryDto findById(Long id) {
        return convertToDto(countryRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {
        countryRepository.deleteById(id);
    }

    @Override
    public List<CountryDto> findAll() {
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false)
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

    private Country convertToEntity(CountryDto countryDto) {
        Country country = modelMapper.map(countryDto, Country.class);
        currencyRepository.findByCodeIgnoreCase(countryDto.getCurrencyCode()).ifPresent(country::setCurrency);
        return country;
    }

    private CountryDto convertToDto(Country country) {
        return modelMapper.map(country, CountryDto.class);
    }


}
