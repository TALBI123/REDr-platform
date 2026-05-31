package com.example.demo.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyPublicDTO {

    private String id;
    private String name;
    private String city;
    private Float rating;
    private String logoUrl;
    private String description;
}