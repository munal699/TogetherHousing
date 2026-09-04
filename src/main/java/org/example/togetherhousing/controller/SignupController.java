
package org.example.togetherhousing.controller;

import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final userRepository userRepository;

    @GetMapping("/signup")
    public String signup() {
        return "signupPage";
    }

    @PostMapping("/signup")
    public String signupPost(HttpServletRequest request) {

        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserTbl user = new UserTbl();

        user.setFullname(fullname);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return "loginPage";
    }
}