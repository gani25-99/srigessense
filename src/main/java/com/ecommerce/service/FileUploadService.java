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


    // =========================================================
    // PRODUCT IMAGE UPLOAD
    // =========================================================

    public String uploadFile(
            MultipartFile file)
            throws IOException {

        if (file == null ||
                file.isEmpty()) {

            throw new RuntimeException(
                    "Please select an image.");
        }


        String originalName =
                file.getOriginalFilename();


        if (originalName == null ||
                originalName.isBlank()) {

            originalName = "image.jpg";
        }


        // =====================================================
        // REMOVE PATH FROM ORIGINAL FILENAME
        // =====================================================

        String safeName =
                Paths.get(originalName)
                        .getFileName()
                        .toString();


        String fileName =
                System.currentTimeMillis()
                        + "_"
                        + safeName;


        // =====================================================
        // CREATE UPLOAD FOLDER
        // =====================================================

        Path folder =
                Paths.get(
                        System.getProperty(
                                "user.dir"),
                        uploadPath);


        Files.createDirectories(
                folder);


        // =====================================================
        // SAVE FILE
        // =====================================================

        Path destination =
                folder.resolve(
                        fileName);


        file.transferTo(
                destination.toFile());


        return fileName;
    }


    // =========================================================
    // PROFILE IMAGE UPLOAD
    // =========================================================

    public String uploadProfileImage(
            MultipartFile file)
            throws IOException {

        if (file == null ||
                file.isEmpty()) {

            throw new RuntimeException(
                    "Please select an image.");
        }


        String originalName =
                file.getOriginalFilename();


        if (originalName == null ||
                originalName.isBlank()) {

            originalName = "profile.jpg";
        }


        // =====================================================
        // SAFE FILENAME
        // =====================================================

        String safeName =
                Paths.get(originalName)
                        .getFileName()
                        .toString();


        String fileName =
                System.currentTimeMillis()
                        + "_"
                        + safeName;


        // =====================================================
        // PROFILE DIRECTORY
        // =====================================================

        Path folder =
                Paths.get(
                        System.getProperty(
                                "user.dir"),
                        "uploads/profile");


        Files.createDirectories(
                folder);


        Path destination =
                folder.resolve(
                        fileName);


        // =====================================================
        // RESIZE PROFILE IMAGE
        // =====================================================

        Thumbnails.of(
                        file.getInputStream())
                .size(
                        300,
                        300)
                .keepAspectRatio(true)
                .toFile(
                        destination.toFile());


        return fileName;
    }
}