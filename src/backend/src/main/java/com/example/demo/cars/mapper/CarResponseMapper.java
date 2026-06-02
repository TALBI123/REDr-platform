package com.example.demo.cars.mapper;

import com.example.demo.cars.dto.CarResponseDTO;
import com.example.demo.models.agency.Car;
import com.example.demo.models.agency.Photo;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CarResponseMapper {

    @Mapping(target = "agencyId", source = "agency.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "photoUrls", source = "photos", qualifiedByName = "photoUrls")
    CarResponseDTO toDto(Car car);

    List<CarResponseDTO> toDtos(List<Car> cars);

    @Named("photoUrls")
    default List<String> mapPhotoUrls(List<Photo> photos) {
        if (photos == null || photos.isEmpty()) {
            return Collections.emptyList();
        }
        return photos.stream()
                .map(photo -> photo.getSecureUrl() != null ? photo.getSecureUrl() : photo.getUrl())
                .filter(Objects::nonNull)
                .toList();
    }
}
