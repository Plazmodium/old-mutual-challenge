package oldmutual.spring.boot.oldmutualchallenge.services;

import oldmutual.spring.boot.oldmutualchallenge.dtos.CountryDto;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class CountryMapperTest {

    private final CountryMapper mapper = Mappers.getMapper(CountryMapper.class);

    @Test
    void toModel_shouldMapDtoToModel() {
        // Given
        CountryDto.NativeNameDto nativeNameDto = new CountryDto.NativeNameDto("Republiek van Suid-Afrika", "Suid-Afrika");
        CountryDto.NameDto nameDto = new CountryDto.NameDto(
                "South Africa",
                "Republic of South Africa",
                java.util.Map.of("afr", nativeNameDto)
        );
        CountryDto.FlagsDto flagsDto = new CountryDto.FlagsDto(
                "https://flagcdn.com/w320/za.png",
                "https://flagcdn.com/za.svg",
                "The flag of South Africa has two equal horizontal bands of red (top) and blue separated by a central green band..."
        );
        CountryDto dto = new CountryDto(
            nameDto,
            "ZA",
            "ZAF",
            flagsDto,
            java.util.List.of("Pretoria"),
            "Africa",
            "Southern Africa",
            59308690L
        );

        // When
        Country model = mapper.toModel(dto);

        // Then
        assertThat(model).isNotNull();
        assertThat(model.getCommonName()).isEqualTo("South Africa");
        assertThat(model.getOfficialName()).isEqualTo("Republic of South Africa");
        assertThat(model.getCca2()).isEqualTo("ZA");
        assertThat(model.getCca3()).isEqualTo("ZAF");
        assertThat(model.getFlagPng()).isEqualTo("https://flagcdn.com/w320/za.png");
        assertThat(model.getFlagSvg()).isEqualTo("https://flagcdn.com/za.svg");
        assertThat(model.getCapital()).isEqualTo("Pretoria");
        assertThat(model.getRegion()).isEqualTo("Africa");
        assertThat(model.getSubregion()).isEqualTo("Southern Africa");
        assertThat(model.getPopulation()).isEqualTo(59308690L);
    }
}
