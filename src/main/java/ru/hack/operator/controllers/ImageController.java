package ru.hack.operator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.hack.operator.dto.ImageResultDto;
import ru.hack.operator.services.ImageService;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;


    @PostMapping("/image")
    @PreAuthorize("isAuthenticated()")
    public void postImage(@RequestParam MultipartFile[] files) {
        imageService.save(files);
    }

    @PostMapping("/image/result/{id}")
    public void postResult(@RequestBody ImageResultDto imageResultDto) {
        imageService.saveResult(imageResultDto);
    }
}
