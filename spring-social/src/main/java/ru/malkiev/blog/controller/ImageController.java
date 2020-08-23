package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.malkiev.blog.assembler.ImageAssembler;
import ru.malkiev.blog.entity.Image;
import ru.malkiev.blog.exception.ImageNotFoundException;
import ru.malkiev.blog.model.ImageModel;
import ru.malkiev.blog.repository.ImageRepository;
import ru.malkiev.blog.service.ImageStorageService;
import ru.malkiev.blog.specification.ImageSpecification;

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
    public ResponseEntity<ImageModel> getOne(@PathVariable int id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    @GetMapping("/images")
    public ResponseEntity<PagedModel<ImageModel>> page(ImageSpecification spec,
                                                       @PageableDefault Pageable pageable) {
        return spec.get()
                .map(s -> repository.findAll(s, pageable))
                .map(assembler::toPagedModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
