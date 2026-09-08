
package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final userRepository uRepo;

    // Open signup page
    @GetMapping("/signup")
    public String signup() {
        return "signupPage";
    }

    @GetMapping("/login")
    public String login()
    {
        return "loginPage";
    }

    // Process signup form
    @PostMapping("/signup")
    public String signupPost(HttpServletRequest request) {

        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        //md5 algorithm, this is basic algorithm, anyone can hack this

        // Check if email already exists
        if (uRepo.existsByEmailAndPassword(email,hashPassword)) {
            return "redirect:/signup?error=email";
        }

        // Create new user
        UserTbl user = new UserTbl();

        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setPassword(password);
        user.setRole(role);

        // Save user to TiDB
        uRepo.save(user);

        // Signup successful → go to login page
        return "redirect:/login?success=true";
    }
    @PostMapping("/login")
    public String loginPost(HttpServletRequest request, Model m) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Convert entered password into MD5 hash
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        // Check email and password in database
        if (uRepo.existsByEmailAndPassword(email, hashPassword)) {

            // Create session
            HttpSession session = request.getSession();

            // Store logged-in user's email in session
            session.setAttribute("email", email);

            // Login successful
            return "home";
        }

        // Login failed
        m.addAttribute("error", "Email or password is incorrect");

        return "loginPage";
    }
}