package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    /**
     * Get a list of all user records.
     *
     * @return list<User>
     */
    public List<User> getAll() {
        return userMapper.getAll();
    }

    /**
     * Get a user record by user id
     *
     * @return user
     */
    public User get(int userId) {
        return userMapper.getUser(userId);
    }

    /**
     * Adds a new user record
     *
     * @param user
     */
    public void add(User user) {
        userMapper.add(user);
    }

    /**
     * Update user record existing
     *
     * @param user
     */
    public void update(User user) {
        userMapper.update(user);
    }

    /**
     * Deletes a user record by user id
     *
     * @param userId
     */
    public void deleteByID(int userId) {
        userMapper.deleteByID(userId);
    }

    /**
     * Loads user details by username for authentication
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.getByUserName(username);
    }

    /**
     * Get current user id from the security context.
     *
     * @return user|null
     */
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            return user.getUserId();
        }
        return null;
    }
}
