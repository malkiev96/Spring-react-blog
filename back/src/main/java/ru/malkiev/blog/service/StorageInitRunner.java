package ru.malkiev.blog.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StorageInitRunner implements CommandLineRunner {

    @Resource
    private ImageStorageService storageService;

    @Override
    public void run(String... args) throws Exception {
        storageService.init();
    }
}
