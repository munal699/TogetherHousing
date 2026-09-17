
package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.EmailService;
import org.example.togetherhousing.service.NotificationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {

    private final JavaMailSender jms;
    private final userRepository uRepo;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @org.springframework.beans.factory.annotation.Value("${spring.mail.username:togetherhousing.notifications@gmail.com}")
    private String fromEmail;

    public SignupController(JavaMailSender jms, userRepository uRepo, EmailService emailService, NotificationService notificationService) {
        this.jms = jms;
        this.uRepo = uRepo;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

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
        if (uRepo.existsByEmail(email)) {
            return "redirect:/signup?error=email";
        }

        // Create new user
        UserTbl user = new UserTbl();

        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setPassword(hashPassword);
        user.setRole(role);

        // Save user to TiDB
        uRepo.save(user);

        // mail sender using JavaMailSender (jms)
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            if (fromEmail != null && !fromEmail.trim().isEmpty()) {
                message.setFrom(fromEmail);
            }
            message.setTo(user.getEmail());
            message.setSubject("Registration Successful - Welcome to Together Housing");
            message.setText("Congratulations " + user.getFullname() + "!\n\nYou have successfully signed up for Together Housing.\nYou can now log in, explore shared properties, calculate installments, and reserve your future home.\n\nBest regards,\nTogether Housing Team");
            jms.send(message);
            System.out.println("📧 [GMAIL SMTP SUCCESS] Signup confirmation email delivered to: " + user.getEmail());
        } catch (Exception ex) {
            System.err.println("⚠️ [GMAIL SMTP NOTICE] Could not send live email to " + user.getEmail() + " : " + ex.getMessage());
            System.err.println("   👉 Set your Gmail address & 16-character App Password in application.properties to receive live emails.");
        }

        notificationService.sendNotification(user, null, "Welcome to Together Housing! Confirmation email dispatched to " + user.getEmail(), "REGISTRATION");

        // Signup successful → go to login page
        return "redirect:/login?success=true";
    }
    @PostMapping("/login")
    public String loginPost(HttpServletRequest request, Model m) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Convert entered password into MD5 hash
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        // Check email & password in database
        if (uRepo.existsByEmailAndPassword(email, hashPassword)) {

            UserTbl user = uRepo.findByEmail(email).orElse(null);

            if (user != null) {
                // Create session
                HttpSession session = request.getSession();

                // Store logged-in user details in session
                session.setAttribute("user", user);
                session.setAttribute("email", user.getEmail());
                session.setAttribute("userId", user.getId());
                session.setAttribute("fullname", user.getFullname());
                session.setAttribute("role", user.getRole());

                // Send email on login using JavaMailSender (jms)
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    if (fromEmail != null && !fromEmail.trim().isEmpty()) {
                        message.setFrom(fromEmail);
                    }
                    message.setTo(user.getEmail());
                    message.setSubject("Security Alert: Successful Login to Together Housing");
                    message.setText("Hello " + user.getFullname() + ",\n\nYou have successfully logged in to your Together Housing account.\n\nTime: " + new java.util.Date() + "\n\nIf this was you, you can safely disregard this email.\nIf you did not initiate this login, please secure your account immediately.\n\nBest regards,\nTogether Housing Security Team");
                    jms.send(message);
                    System.out.println("📧 [GMAIL SMTP SUCCESS] Login notification email delivered to: " + user.getEmail());
                } catch (Exception ex) {
                    System.err.println("⚠️ [GMAIL SMTP NOTICE] Could not send live email to " + user.getEmail() + " : " + ex.getMessage());
                    System.err.println("   👉 Set your Gmail address & 16-character App Password in application.properties to receive live emails.");
                }

                notificationService.sendNotification(user, null, "Security Alert: Successful login to your account from " + user.getEmail(), "LOGIN_ALERT");

                String role = user.getRole() != null ? user.getRole().toUpperCase().trim() : "";

                // Redirect based on user role
                if ("BUYER".equals(role)) {
                    return "redirect:/buyer-dashboard";
                } else if ("SELLER".equals(role)) {
                    return "redirect:/seller-dashboard";
                } else if ("ADMIN".equals(role)) {
                    return "redirect:/admin-dashboard";
                }

                return "redirect:/home";
            }
        }

        // Login failed
        m.addAttribute("error", "Email or password is incorrect");
        return "loginPage";
    }

    // Logout endpoint
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout=true";
    }
}