package oldmutual.spring.boot.oldmutualchallenge.dtos;

import java.util.List;
import java.util.Map;

public record CountryDto(
        NameDto name,
        String cca2,
        String cca3,
        FlagsDto flags,
        List<String> capital,
        String region,
        String subregion,
        Long population
) {
    public record NameDto(
            String common,
            String official,
            Map<String, NativeNameDto> nativeName
    ) {}

    public record NativeNameDto(
            String official,
            String common
    ) {}

    public record FlagsDto(
            String png,
            String svg,
            String alt
    ) {}
}