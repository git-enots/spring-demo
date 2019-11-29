package com.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class City {
    @Id
    private Long id;
    private String cityName;
    private String cityCode;
    private String description;
}
