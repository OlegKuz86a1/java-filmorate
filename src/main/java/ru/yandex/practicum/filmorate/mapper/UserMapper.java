package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.control.DeepClone;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserBaseDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.UserEntity;

@Service
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, mappingControl = DeepClone.class)
public abstract class UserMapper implements EntityMapper<UserDto, UserEntity> {

    public abstract UserEntity toEntity(UserBaseDto userBaseDto);
}
