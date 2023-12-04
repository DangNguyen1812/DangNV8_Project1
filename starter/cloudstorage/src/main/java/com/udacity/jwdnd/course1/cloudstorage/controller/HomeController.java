package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    /**
     * Get list data of file, note, credential from database
     * 
     * @param model
     * @return
     */
    @GetMapping("home")
    public String homePage(Model model) {
        // Get current userId
        int userId = userService.getCurrentUserId();

        // Add specific list file, note, and credential of user to the model
        model.addAttribute("fileList", fileService.getAllByUserId(userId));
        model.addAttribute("noteList", noteService.getAllByUserId(userId));
        model.addAttribute("credentialList", credentialService.getAllByUserId(userId));

        // Add file-error, note-error, and credential-error to the model
        model.addAttribute("fileNotification", fileService.getFileNotification());
        model.addAttribute("noteNotification", noteService.getMsgNotification());
        model.addAttribute("creNotification", credentialService.getMsgNotification());

        model.addAttribute("noteModel", new Note());
        model.addAttribute("credentialModel", new Credential());

        return "home";
    }
}
