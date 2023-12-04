package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.FileException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserService userService;

    private String fileNotification;

    /**
     * Maps the data from a MultipartFile object to a FileModel object.
     * @param multipartfile
     * @param file
     */
    public void mapMultipartFile(MultipartFile multipartfile, FileModel file) {
        if(multipartfile == null){
            throw new FileException("Error: File is null, Please try again!");
        }
        file.setFileName(multipartfile.getOriginalFilename());
        file.setContentType(multipartfile.getContentType());
        file.setFileSize(Long.toString(multipartfile.getSize()));
    }

    /**
     * Get all file record from database
     * @return
     */
    public List<FileModel> getAll() {
        return fileMapper.getAll();
    }

    /**
     * Get file record by file id
     * @param fileId
     * @return
     */
    public FileModel get(int fileId) {
        return fileMapper.get(fileId);
    }

    /**
     * Adds a new file record
     * @param file
     * @throws FileException
     */
    public void add(FileModel file) throws FileException {
        String fileName = file.getFileName();

        for(FileModel fileModelDB : this.getAll()) {
            if(fileModelDB.getFileName().equals(file.getFileName())) {
                throw new FileException("File name is already exists, Please try again");
            }
        }

        if (!isValidFileFormat(fileName)) {
            throw new FileException("Error: Invalid File Format, Please try again!");
        }
        file.setUserId(userService.getCurrentUserId());
        fileMapper.add(file);
    }

    /**
     * Updates a file record
     * @param file
     */
    public void update(FileModel file) {
        fileMapper.update(file);
    }

    /**
     * Deletes a record by file id
     * @param fileId
     */
    public void deleteByID(int fileId) {
        fileMapper.deleteByID(fileId);
    }

    /**
     * Get list file by user id in database
     * @param userId
     * @return
     */
    public List<FileModel> getAllByUserId(int userId) {
        return fileMapper.getAllByUserId(userId);
    }

    public String getFileNotification() {
        return fileNotification;
    }

    public void setFileNotification(String fileNotification) {
        this.fileNotification = fileNotification;
    }

    public FileMapper getFileMapper() {
        return fileMapper;
    }

    /**
     * Make resource directory
     * @return
     */
    public String getResourceDirectory() {
        String resourceDirectory = System.getProperty("user.dir");
        return String.valueOf(Paths.get(resourceDirectory, "starter",
                        "cloudstorage", "src", "main", "resources", "document"))
                .concat(File.separator);
    }

    /**
     * Checks valid format
     *
     * @param fileName
     * @return
     */
    public boolean isValidFileFormat(String fileName) {
        String regex = ".*\\.(pdf|jpg|jpeg|png|zip)$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);

        return matcher.matches();
    }
}
