package ru.hack.operator.services.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hack.operator.models.Image;
import ru.hack.operator.models.Trouble;
import ru.hack.operator.repositories.TroubleRepository;
import ru.hack.operator.services.CheckService;
import ru.hack.operator.services.FilesService;

import javax.transaction.Transactional;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.defaultCharset;

@Service
public class CheckServiceImpl implements CheckService {

    @Value("${address}")
    private String address;

    @Autowired
    private FilesService filesService;

    @Autowired
    private TroubleRepository troubleRepository;

    @Transactional
    @SneakyThrows
    @Override
    public void sendToCheck(Image image) {
        File file = filesService.getFileByStorageName(image.getFileInfo().getStorageFileName());
        String CRLF = "\r\n";
        String param = "value";
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String url = address + "?name=" + image.getFileInfo().getStorageFileName();
        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, defaultCharset()), true);
        ) {
            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(String.valueOf(defaultCharset())).append(CRLF);
            writer.append(CRLF).append(param).append(CRLF).flush();

            // Send text file.
            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"").append(defaultCharset().name()).append("\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(String.valueOf(defaultCharset())).append(CRLF); // Text file itself must be saved in this charset!
            writer.append(CRLF).flush();
            output.flush(); // Important before continuing with writer!
            Files.copy(file.toPath(), output);
            writer.append(CRLF).flush();
            writer.append("--").append(boundary).append("--").append(CRLF).flush();
        }

        // Request is lazily fired whenever you need to obtain information about response.
//        int responseCode = ((HttpURLConnection) connection).getResponseCode();
//        if (responseCode != 200) {
//            List<String> response = (List<String>) ((HttpURLConnection) connection).getContent();
//            image.setStatus(Image.Status.TROUBLED);
//            Trouble trouble = Trouble.builder()
//                    .image(image)
//                    .status(Trouble.Status.TO_DO)
//                    .types(response.stream()
//                            .map(Trouble.Type::valueOf)
//                            .collect(Collectors.toList()))
//                    .build();
//            troubleRepository.save(trouble);
//        } else {
//            image.setStatus(Image.Status.GOOD);
//        }

    }
}
