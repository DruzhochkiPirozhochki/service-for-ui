package ru.hack.operator.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hack.operator.dto.ImagePostDto;
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
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
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
    public void save(ImagePostDto imagePostDto) {
    }

    @Override
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
}
