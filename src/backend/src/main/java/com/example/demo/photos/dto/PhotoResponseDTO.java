package com.example.demo.photos.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponseDTO {

    private String id;
    private String url;
    private String secureUrl;
    private String publicId;
    private String description;
    private LocalDateTime createdAt;
    private String carId;
    private String conditionReportId;
}
