package ru.malkiev.blog.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.malkiev.blog.entity.Image;
import ru.malkiev.blog.exception.ImageNotFoundException;
import ru.malkiev.blog.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class ImageStorageService {

    private final ImageRepository imageRepository;
    private final Path root = Paths.get("images");

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException ignored) {}
    }

    public Image save(MultipartFile file) {
        try {
            String name = UUID.randomUUID() + "." + file.getOriginalFilename()
                            .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            Files.copy(file.getInputStream(), this.root.resolve(name));
            Image image = new Image();
            image.setType(file.getContentType());
            image.setName(name);
            image.setPath(root.toUri().getPath() + "/" + name);
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            image.setWidth(bufferedImage.getWidth());
            image.setHeight(bufferedImage.getHeight());

            return imageRepository.save(image);

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ImageNotFoundException(filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}