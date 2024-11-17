package com.yen.SpringMapStruct.entity.vo;

// https://www.tpisoftware.com/tpu/articleDetails/2443

import com.yen.SpringMapStruct.entity.dto.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDTO toDTO(PersonVO personVO);

    PersonVO toVO(PersonDTO personDTO);
}