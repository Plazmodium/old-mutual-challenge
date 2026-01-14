package oldmutual.spring.boot.oldmutualchallenge.services;

import oldmutual.spring.boot.oldmutualchallenge.dtos.CountryDto;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    @Mapping(target = "commonName", source = "name.common")
    @Mapping(target = "officialName", source = "name.official")
    @Mapping(target = "flagPng", source = "flags.png")
    @Mapping(target = "flagSvg", source = "flags.svg")
    @Mapping(target = "capital", source = "capital", qualifiedByName = "firstElement")
    Country toModel(CountryDto dto);

    @Named("firstElement")
    default String firstElement(List<String> list) {
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }
}