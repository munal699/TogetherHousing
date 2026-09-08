package org.example.togetherhousing.controller;


import org.example.togetherhousing.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
//Controller - manages http requests : Get Mapping, Post Mapping,etc
public class AllController {
    @Autowired
    private userRepository uRepo;

    @GetMapping("/")
    public String firstPage() {
        return "firstPage";
    }

    @GetMapping("/buyer-dashboard")
    public String buyerDashboard() {
        return "buyer-dashboard";
    }
    @GetMapping("/{page}.html")
    public String servePage(@PathVariable String page) {
        return page;
    }

@GetMapping("/home")
public String homeGet(Model m) {

    return "home";
}

}

