package ru.hack.operator.services;

import org.springframework.web.multipart.MultipartFile;
import ru.hack.operator.dto.ImageResultDto;

public interface ImageService {
    void save(MultipartFile[] files);

    void saveResult(ImageResultDto imageResultDto);

    void saveTaggedResult(String storageFileName, MultipartFile multipartFile, Integer[] codes);
}
