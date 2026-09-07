package org.example.togetherhousing.controller;


import org.example.togetherhousing.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
//Controller - manages http requests : Get Mapping, Post Mapping,etc
public class AllController {
    @Autowired
    private userRepository uRepo;

    @GetMapping("/")
    public String firstPage() {
        return "firstPage";
    }
}


