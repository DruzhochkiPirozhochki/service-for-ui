package ru.hack.operator.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class ImagePostDto {
    private String[] coordinates;
}
