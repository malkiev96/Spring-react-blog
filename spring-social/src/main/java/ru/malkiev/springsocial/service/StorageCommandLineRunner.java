package ru.malkiev.springsocial.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StorageCommandLineRunner implements CommandLineRunner {

    @Resource
    private ImageStorageService storageService;

    @Override
    public void run(String... args) throws Exception {
        storageService.init();
    }
}
