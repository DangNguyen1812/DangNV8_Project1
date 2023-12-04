package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.CredentialException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CredentialService {
    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserService userService;

    private String msgNotification;

    /**
     * Get all credential from database
     * @return
     */
    public List<Credential> getAll() {
        return credentialMapper.getAll();
    }

    /**
     * Get all credential by user id
     * @param uerId
     * @return
     */
    public List<Credential> getAllByUserId(int uerId) {
        return credentialMapper.getAllByUserId(uerId);
    }

    /**
     * Get credential record by credentialId
     * @param credentialId
     * @return
     */
    public Credential getBycredentialId(int credentialId) {
        Credential credential = credentialMapper.getBycredentialId(credentialId);
        if(credential != null){
            credential.setPassword(
                    encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
            return credential;
        }else{
            return null;
        }
    }

    /**
     * Adds a new credential record
     * @param credential
     */
    public void add(Credential credential) {
        credential.setUserId(userService.getCurrentUserId());
        for(Credential credentialDB: this.getAllByUserId(credential.getUserId())) {
            if (credentialDB.getUrl().equals(credential.getUrl())) {
                throw new CredentialException("Error: URL is duplicate, Please try again!");
            }
        }
        credential.setKey(encryptionService.generateKey());
        credential.setPassword(
                encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        credential.setUserId(userService.getCurrentUserId());
        credentialMapper.add(credential);
    }

    /**
     * Updates a credential record
     * @param credential
     */
    public void update(Credential credential) {
        credential.setUserId(userService.getCurrentUserId());
        credential.setKey(encryptionService.generateKey());
        credential.setPassword(
                encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        credential.setUserId(userService.getCurrentUserId());
        credentialMapper.update(credential);
    }

    /**
     * Deletes a record by credentialId
     * @param credentialId
     */
    public void deleteByID(int credentialId) {
        credentialMapper.deleteByID(credentialId);
    }

    public String getMsgNotification() {
        return msgNotification;
    }

    public void setMsgNotification(String msgNotification) {
        this.msgNotification = msgNotification;
    }
}
