package ru.hack.operator.services;

import org.springframework.web.multipart.MultipartFile;
import ru.hack.operator.models.FileInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

public interface FilesService {
    FileInfo save(MultipartFile file);

    void writeFileToResponse(String fileName, HttpServletResponse response);

    File getFileByStorageName(String storageFileName);
}
