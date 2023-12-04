package com.udacity.jwdnd.course1.cloudstorage.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.udacity.jwdnd.course1.cloudstorage.exception.FileException;
import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * Upload file
     *
     * @param file
     * @return
     * @throws FileException
     */
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file) {
        FileModel fileModel = new FileModel();
        try {
            fileService.mapMultipartFile(file, fileModel);
            String filePath = fileService.getResourceDirectory() + fileModel.getFileName();
            file.transferTo(new File(filePath));
            fileService.add(fileModel);
            fileService.setFileNotification("Success: Upload file successfully");
        } catch (FileException e) {
            fileService.setFileNotification(e.getMessage());
        } catch (Exception e) {
            fileService.setFileNotification("Error: Upload file occur an expected error");
        }

        return "redirect:/home";
    }

    /**
     * Download file by fileName
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String fileName) throws IOException {
        String resourceDirectory = fileService.getResourceDirectory();
        Path filePath = Paths.get(resourceDirectory).resolve(fileName);
        FileSystemResource resource = new FileSystemResource(filePath.toFile());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok().headers(header).body(resource);
    }

    /**
     * Delete file by id
     *
     * @param fileId
     * @return
     */
    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable String fileId) {
        FileModel filemodel = fileService.get(Integer.valueOf(fileId));
        String resourceDirectory = fileService.getResourceDirectory();
        String filePath = resourceDirectory + filemodel.getFileName();

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            fileService.deleteByID(Integer.valueOf(fileId));
            fileService.setFileNotification("Success: The file has been successfully delete");
        }
        return "redirect:/home";
    }
}
