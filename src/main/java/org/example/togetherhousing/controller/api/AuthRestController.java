package org.example.togetherhousing.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.togetherhousing.dto.ApiResponse;
import org.example.togetherhousing.dto.LoginRequest;
import org.example.togetherhousing.dto.SignupRequest;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final userRepository uRepo;
    private final EmailService emailService;

    public AuthRestController(userRepository uRepo, EmailService emailService) {
        this.uRepo = uRepo;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest request,
                                                                  HttpServletRequest httpRequest) {
        String hashPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());

        if (!uRepo.existsByEmailAndPassword(request.getEmail(), hashPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
        }

        UserTbl user = uRepo.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("User not found"));
        }

        // Setup session
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("email", user.getEmail());
        session.setAttribute("userId", user.getId());
        session.setAttribute("fullname", user.getFullname());
        session.setAttribute("role", user.getRole());

        // Send login alert email
        emailService.sendLoginNotification(user.getEmail(), user.getFullname());

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("fullname", user.getFullname());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole());
        userData.put("phone", user.getPhone());
        userData.put("address", user.getAddress());

        return ResponseEntity.ok(ApiResponse.ok("Login successful", userData));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> signup(@Valid @RequestBody SignupRequest request) {
        if (uRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email address is already registered"));
        }

        String hashPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());

        UserTbl user = new UserTbl();
        user.setFullname(request.getFullname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setPassword(hashPassword);
        user.setRole(request.getRole() != null ? request.getRole().toUpperCase().trim() : "BUYER");

        UserTbl saved = uRepo.save(user);

        // Send welcome email
        emailService.sendRegistrationEmail(saved.getEmail(), saved.getFullname());

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", saved.getId());
        userData.put("fullname", saved.getFullname());
        userData.put("email", saved.getEmail());
        userData.put("role", saved.getRole());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Registration successful", userData));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Not authenticated"));
        }

        UserTbl user = uRepo.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found"));
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("fullname", user.getFullname());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole());
        userData.put("phone", user.getPhone());
        userData.put("address", user.getAddress());

        return ResponseEntity.ok(ApiResponse.ok("Current user profile retrieved", userData));
    }
}
