package com.ecommerce.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class FileUploadService {

    @Value("${upload.path}")
    private String uploadPath;

    public String uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("No file selected");
        }

        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()) {
            originalName = "image.jpg";
        }

        String fileName = System.currentTimeMillis() + "_" + originalName;

        Path folder = Paths.get(uploadPath);
        Files.createDirectories(folder);

        Path destination = folder.resolve(fileName);

        // Save directly
        file.transferTo(destination.toFile());

        return fileName;
    }

    public String uploadProfileImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("No file selected");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path folder = Paths.get("C:/ecommerce/uploads/profile/");
        Files.createDirectories(folder);

        Path destination = folder.resolve(fileName);

        Thumbnails.of(file.getInputStream())
                .size(300, 300)
                .keepAspectRatio(true)
                .toFile(destination.toFile());

        return fileName;
    }
}