package ru.hack.operator.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hack.operator.models.FileInfo;
import ru.hack.operator.repositories.FileStorageRepository;
import ru.hack.operator.services.FilesService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${storage.path}")
    private String fileStoragePath;

    @Autowired
    private FileStorageRepository fileStorageRepository;


    @Override
    @Transactional
    public FileInfo save(MultipartFile file) {
        String storageFileName = createStorageName(file.getOriginalFilename());
        FileInfo fileInfo = FileInfo.builder()
                .originalFileName(file.getOriginalFilename())
                .storageFileName(storageFileName)
                .size(file.getSize())
                .type(file.getContentType())
                .url(fileStoragePath + "\\" + storageFileName)
                .build();


        fileStorageRepository.save(fileInfo);

        try {
            Files.copy(file.getInputStream(), Paths.get(fileStoragePath, storageFileName));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return fileInfo;
    }


    @Override
    public void writeFileToResponse(String fileName, HttpServletResponse response) {
        FileInfo fileInfo = get(fileName);
        response.setContentType(fileInfo.getType());
        try {
            InputStream inputStream = new FileInputStream(new java.io.File(fileInfo.getUrl()));
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (
                IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public File getFileByStorageName(String storageFileName) {
        File result = new File(storageFileName + File.separator + storageFileName);
        return result;
    }


    @Transactional
    public FileInfo get(String name) {
        Optional<FileInfo> fileInfoCandidate = fileStorageRepository.findOneByStorageFileName(name);
        return fileInfoCandidate.orElse(null);
    }


    private String createStorageName(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        String newFileName = UUID.randomUUID().toString();
        return newFileName + "." + extension;
    }
}