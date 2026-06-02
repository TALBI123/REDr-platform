package com.example.demo.photos.mapper;

import com.example.demo.models.agency.Photo;
import com.example.demo.photos.dto.PhotoResponseDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoResponseMapper {

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "conditionReportId", source = "conditionReport.id")
    PhotoResponseDTO toDto(Photo photo);

    List<PhotoResponseDTO> toDtos(List<Photo> photos);
}
