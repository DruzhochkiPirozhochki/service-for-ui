package ru.hack.operator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hack.operator.dto.ImageResultDto;
import ru.hack.operator.services.ImageService;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;


    @PostMapping("/image")
    @PreAuthorize("isAuthenticated()")
    public void postImage(@RequestParam(name = "files") MultipartFile[] files) {
        imageService.save(files);
    }

    @PostMapping("/image/result/{id}")
    public void postResult(@RequestBody ImageResultDto imageResultDto) {
        imageService.saveResult(imageResultDto);
    }

    @PostMapping("/image/tagged/{storageFileName}")
    public void postTaggedResult(@RequestParam("file") MultipartFile file,
                                 @RequestParam("codes") Integer[] codes,
                                 @PathVariable("storageFileName") String storageFileName) {
        imageService.saveTaggedResult(storageFileName, file, codes);
    }
}
