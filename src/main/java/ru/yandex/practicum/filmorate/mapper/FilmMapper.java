package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.control.DeepClone;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmBaseDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.*;

import java.util.List;

@Service
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, mappingControl = DeepClone.class)
public abstract class FilmMapper implements EntityMapper<FilmDto, FilmEntity> {

    public abstract FilmDto toDto(FilmEntity entity);

    public abstract FilmEntity toEntity(FilmDto dto);

    public abstract FilmDto toDto(FilmEntity entity, List<GenreDto> genres);

    public abstract FilmEntity mapToEntity(FilmBaseDto baseDto);

    public abstract GenreDto mapToGenreDto(GenreEntity genre);

    public abstract List<GenreDto> mapToGenreDto(List<GenreEntity> genre);

    public abstract List<GenreDto> mapToGenreDtoFromInteger(List<Integer> genre);

    public abstract List<MpaDto> mapToMpaDtoFromInteger(List<Integer> mpa);

    public List<Integer> mapToIntegerDtoFromGenreDto(List<GenreDto> genre) {
        return genre.stream()
                .map(GenreDto::getId)
                .toList();
    }

    public List<Integer> mapToIntegerFromMpaDto(List<MpaDto> mpa) {
        return mpa.stream()
                .map(MpaDto::getId)
                .toList();
    }

    public GenreDto mapToGenreDto(Integer genre) {
        return GenreDto.builder().id(genre).build();
    }

    public MpaDto mapToMpaDto(Integer mpa) {
        return ru.yandex.practicum.filmorate.dto.MpaDto.builder().id(mpa).build();
    }

    public abstract MpaDto mapToMpaDto(MpaEntity rating);

    public abstract List<MpaDto> mapToMpaDto(List<MpaEntity> rating);

}
