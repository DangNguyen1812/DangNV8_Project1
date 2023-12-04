package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.CredentialException;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;
    
    /**
     * Get CredentialModel by id
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Credential> getCredential(@PathVariable String id) {
        Credential credentialModel = credentialService.getBycredentialId(Integer.parseInt(id));
        return new ResponseEntity<>(credentialModel, HttpStatus.OK);
    }

    /**
     * Create a credential
     * @param credentialModel
     * @return
     */
    @PostMapping("/add")
    public String addCredential(@ModelAttribute Credential credentialModel) {
        try {
            Credential cre = credentialService.getBycredentialId(credentialModel.getCredentialId());
            if (cre != null) {
                credentialService.update(credentialModel);
                credentialService.setMsgNotification("Success: The credential has been successfully updated.");
            }else {
                credentialService.add(credentialModel);
                credentialService.setMsgNotification("Success: The credential has been successfully created ");
            }
        } catch (CredentialException e) {
            credentialService.setMsgNotification(e.getMessage());
        } catch (Exception e) {
            credentialService.setMsgNotification("Error: An unexpected error has occurred");
        }
        return "redirect:/home";
    }

    /**
     * Delete credential by id
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public String deleteCredential(@PathVariable String id) {
        credentialService.deleteByID(Integer.parseInt(id));
        credentialService.setMsgNotification("Success: The credential has been successfully delete.");
        return "redirect:/home";
    }

}
