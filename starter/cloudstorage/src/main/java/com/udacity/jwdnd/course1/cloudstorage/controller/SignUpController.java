package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.SignUpException;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.udacity.jwdnd.course1.cloudstorage.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private SignUpService service;

    /**
     * Get signup page
     * 
     * @param model
     * @return
     */
    @GetMapping()
    public String signupPage(Model model) {
        model.addAttribute("account", new User());
        return "signup";
    }

    /**
     * Create a account
     * 
     * @param user
     * @param model
     * @return
     */
    @PostMapping("/add")
    public String createAccount(@ModelAttribute User user, Model model) {
        String status = null;
        try {
            service.add(user);
            service.setMsgErr("success");
        } catch (SignUpException e) {
            status = e.getMessage();
            return signupPage(model);
        } catch (Exception e) {
            status = e.getMessage();
            return signupPage(model);
        } finally {
            model.addAttribute("status", status);
        }

        return "redirect:/login";
    }

}
