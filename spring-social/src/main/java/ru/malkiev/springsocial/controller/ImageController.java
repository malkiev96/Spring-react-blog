package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.malkiev.springsocial.assembler.ImageAssembler;
import ru.malkiev.springsocial.entity.Image;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
import ru.malkiev.springsocial.model.ImageModel;
import ru.malkiev.springsocial.repository.ImageRepository;
import ru.malkiev.springsocial.service.ImageStorageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
public class ImageController {

    private final ImageRepository repository;
    private final ImageAssembler assembler;
    private final ImageStorageService storageService;

    @GetMapping("/images/{id}")
    public ImageModel getOne(@PathVariable int id) {
        Image image = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));
        return assembler.toModel(image);
    }

    @PostMapping("/images")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> upload(@RequestParam("files") MultipartFile[] files) {
        try {
            List<Image> images = new ArrayList<>();
            Arrays.stream(files).forEach(file -> images.add(storageService.save(file)));
            return ResponseEntity.ok(assembler.toCollectionModel(images));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    @GetMapping("/images/name/{name:.+}")
    public ResponseEntity<?> getFile(@PathVariable String name, HttpServletResponse response) {
        try {
            Resource file = storageService.load(name);
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
            StreamUtils.copy(file.getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
