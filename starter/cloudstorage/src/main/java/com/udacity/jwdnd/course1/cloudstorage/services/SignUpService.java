package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.SignUpException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    @Autowired
    private UserMapper mapper;

    @Autowired
    private HashService hashService;

    private String msgErr;

    public String getMsgErr() {
        return msgErr;
    }

    public void setMsgErr(String msgErr) {
        this.msgErr = msgErr;
    }

    /**
     * get all user record
     *
     * @return
     */
    public List<User> getAll() {
        return mapper.getAll();
    }

    /**
     * get user record by user id
     *
     * @param id
     * @return
     */
    public User get(int id) {
        return mapper.getUser(id);
    }

    /**
     * Get user record by user name
     *
     * @param userName
     * @return
     */
    public User getByUserName(String userName) {
        return mapper.getByUserName(userName);
    }

    /**
     * Adds a new user record
     *
     * @param user
     */
    public void add(User user) {
        User userModel = this.getByUserName(user.getUserName());
        if (userModel != null) {
            throw new SignUpException("Error: UserName Already Exists, Please try again!");
        }
        user.setSalt(hashService.encodedSalt());
        user.setPassword(hashService.getHashedValue(user.getPassword(), user.getSalt()));
        mapper.add(user);
    }


    /**
     * Updates an existing user record
     *
     * @param user
     */
    public void update(User user) {
        mapper.update(user);
    }

    /**
     * Deletes a user record from the database based on the provided ID.
     *
     * @param id
     */
    public void deleteByID(int id) {
        mapper.deleteByID(id);
    }
}
