package ru.hack.operator.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ImageResultDto {
    private String status;
    private List<String> troubles;
    private String imageStorageName;
}
