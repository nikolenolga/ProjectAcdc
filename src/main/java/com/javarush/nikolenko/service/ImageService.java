package com.javarush.nikolenko.service;

import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
public class ImageService {
    private static final String DEFAULT_IMAGE_PNG = "default.png";
    private static final List<String> EXTENSIONS = List.of(
            ".jpg", ".jpeg", ".png", ".bmp", ".gif", ".webp"
    );

    public final Path WEB_INF = Paths.get(URI.create(
                    Objects.requireNonNull(
                            ImageService.class.getResource("/")
                    ).toString()))
            .getParent();

    private final Path imagesFolder = WEB_INF.resolve(UrlHelper.IMAGE_DIRECTORY);

    @SneakyThrows
    public ImageService() {
        Files.createDirectories(imagesFolder);
    }


    @SneakyThrows
    public Path getImagePath(String filename) {
        return EXTENSIONS.stream()
                .map(ext -> imagesFolder.resolve(filename + ext))
                .filter(Files::exists)
                .findAny()
                .orElse(imagesFolder.resolve(filename.substring(0, filename.lastIndexOf("-") + 1)
                        + DEFAULT_IMAGE_PNG));
    }

    public void uploadImage(HttpServletRequest req, String imageId) throws IOException, ServletException {
        Part data = req.getPart(Key.FILE);
        if (Objects.nonNull(data) && data.getInputStream().available() > 0) {
            String filename = data.getSubmittedFileName();
            String ext = filename.substring(filename.lastIndexOf("."));
            deleteOldFiles(imageId);
            filename = imageId + ext;
            uploadImageInternal(filename, data.getInputStream());
            log.debug("Image uploaded: {}", filename);
        }
    }

    private void deleteOldFiles(String filename) {
        EXTENSIONS.stream()
                .map(ext -> imagesFolder.resolve(filename + ext))
                .filter(Files::exists)
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException e) {
                        log.error("Deleting old file: {} failed", filename);
                        throw new QuestException(e);
                    }
                });
    }

    @SneakyThrows
    private void uploadImageInternal(String name, InputStream data) {
        try (data) {
            if (data.available() > 0) {
                Files.copy(data, imagesFolder.resolve(name), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
