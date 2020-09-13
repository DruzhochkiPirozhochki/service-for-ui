package ru.hack.operator.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hack.operator.dto.ImageResultDto;
import ru.hack.operator.models.FileInfo;
import ru.hack.operator.models.Image;
import ru.hack.operator.models.Trouble;
import ru.hack.operator.repositories.FileStorageRepository;
import ru.hack.operator.repositories.ImagesRepository;
import ru.hack.operator.repositories.TroubleRepository;
import ru.hack.operator.services.CheckService;
import ru.hack.operator.services.FilesService;
import ru.hack.operator.services.ImageService;
import ru.hack.operator.utils.CoordinatesUtil;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${storage.path}")
    private String fileStoragePath;

    @Autowired
    private FilesService filesService;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private CoordinatesUtil coordinatesUtil;
    @Autowired
    private CheckService checkService;
    @Autowired
    private FileStorageRepository fileInfoRepository;
    @Autowired
    private TroubleRepository troubleRepository;


    @Override
    @Transactional
    public void save(MultipartFile[] files) {
        for (int i = 0; i < files.length; i++) {
            FileInfo fileInfo = filesService.save(files[i]);

            Image image = Image.builder()
                    .savedAt(LocalDateTime.now())
                    .lastUpdate(LocalDateTime.now())
                    .status(Image.Status.CHECKING)
                    .fileInfo(fileInfo)
                    .build();

            imagesRepository.save(image);

            fileInfo.setImage(image);

            image.setStatus(Image.Status.CHECKING);

            checkService.sendToCheck(image);

        }
    }

    @Override
    public void saveResult(ImageResultDto imageResultDto) {
        Image image = fileInfoRepository.findOneByStorageFileName(imageResultDto.getImageStorageName())
                .get().getImage();
        image.setStatus(Image.Status.valueOf(imageResultDto.getStatus()));
        image.setStatus(Image.Status.TROUBLED);

        Trouble trouble = Trouble.builder()
                .types(imageResultDto.getTroubles().stream().map(Trouble.Type::valueOf).collect(Collectors.toList()))
                .status(Trouble.Status.valueOf(imageResultDto.getStatus()))
                .build();

        troubleRepository.save(trouble);
    }

    @Override
    @Transactional
    public void saveTaggedResult(String storageFileName, MultipartFile file, Integer[] codes) {
        Image image = fileInfoRepository.findOneByStorageFileName(storageFileName)
                .get().getImage();
        image.setStatus(Image.Status.TROUBLED);
        Trouble trouble = Trouble.builder()
                .types(Arrays.stream(codes).map(code -> {
                    switch (code) {
                        case 0:
                            return Trouble.Type.TROUBLE_0;
                        case 1:
                            return Trouble.Type.TROUBLE_1;
                        case 2:
                            return Trouble.Type.TROUBLE_2;
                    }
                    return Trouble.Type.TROUBLE_0;
                }).collect(Collectors.toList()))
                .status(Trouble.Status.TO_DO)
                .user(null)
                .image(image)
                .build();
        troubleRepository.save(trouble);

        File oldFile = filesService.getFileByStorageName(image.getFileInfo().getStorageFileName());
        if (!oldFile.delete()) {
            throw new IllegalArgumentException("no access to file");
        }
        try {
            Files.copy(file.getInputStream(), Paths.get(fileStoragePath, storageFileName));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
