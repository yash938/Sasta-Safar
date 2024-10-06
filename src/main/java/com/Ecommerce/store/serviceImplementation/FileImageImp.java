package com.Ecommerce.store.serviceImplementation;

import com.Ecommerce.store.exceptions.BadApiRequest;
import com.Ecommerce.store.services.ImageFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileImageImp implements ImageFile {

    Logger logger = LoggerFactory.getLogger(FileImageImp.class);

    @Override
    public String UploadFile(MultipartFile multipartFile, String path) {

        String originalFilename = multipartFile.getOriginalFilename();
        logger.info("Original filename: {}", originalFilename);

        // Extract extension from the original filename
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        logger.info("File extension: {}", extension);

        // Generate a unique filename
        String fileName = UUID.randomUUID().toString();
        String fileNameWithExtension = fileName + extension;
        String fullPathFileName = path +fileNameWithExtension;

        // Validate allowed file types
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {

            // Create the directory if it doesn't exist
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            // Copy the file to the target location
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(fullPathFileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new BadApiRequest("Failed to upload file: " + e.getMessage());
            }

        } else {
            throw new BadApiRequest("File with extension " + extension + " is not allowed");
        }

        return fileNameWithExtension; // Return the unique filename
    }

    @Override
    public InputStream getResource(String path, String name) {
        try {
            String fileInput = path + File.separator + name;
            InputStream inputStream = new FileInputStream(fileInput);
            return inputStream;
        }catch (Exception e){
            throw new BadApiRequest("FIle is not found");
        }
    }
}
