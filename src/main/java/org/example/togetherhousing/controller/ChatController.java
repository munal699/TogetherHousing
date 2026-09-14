package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final userRepository userRepository;

    public ChatController(ChatService chatService, userRepository userRepository) {
        this.chatService = chatService;
        this.userRepository = userRepository;
    }


    // Open chat with another user
    @GetMapping("/{userId}")
    public String openChat(
            @PathVariable Integer userId,
            HttpSession session,
            Model model
    ) {

        // Get logged-in user's email from session
        String email = (String) session.getAttribute("email");

        // User is not logged in
        if (email == null) {
            return "redirect:/login";
        }

        // Find logged-in user from database
        UserTbl loggedInUser =
                userRepository.findByEmail(email).orElse(null);

        // Logged-in user not found
        if (loggedInUser == null) {
            session.invalidate();
            return "redirect:/login";
        }

        // Find the other user
        UserTbl otherUser =
                userRepository.findById(userId).orElse(null);

        // Other user does not exist
        if (otherUser == null) {
            return "redirect:/home";
        }

        // Get conversation between both users
        model.addAttribute(
                "messages",
                chatService.getConversation(loggedInUser, otherUser)
        );

        // Send users to the HTML page
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("otherUser", otherUser);

        // Mark messages received by logged-in user as read
        chatService.markMessagesAsRead(
                otherUser,
                loggedInUser
        );

        return "chat";
    }


    // Send a message
    @PostMapping("/send")
    public String sendMessage(
            @RequestParam Integer receiverId,
            @RequestParam String message,
            HttpSession session
    ) {

        // Get logged-in user's email
        String email = (String) session.getAttribute("email");

        // User is not logged in
        if (email == null) {
            return "redirect:/login";
        }

        // Find sender
        UserTbl sender =
                userRepository.findByEmail(email).orElse(null);

        // Sender not found
        if (sender == null) {
            session.invalidate();
            return "redirect:/login";
        }

        // Find receiver
        UserTbl receiver =
                userRepository.findById(receiverId).orElse(null);

        // Receiver does not exist
        if (receiver == null) {
            return "redirect:/home";
        }

        // Prevent empty messages
        if (message != null && !message.trim().isEmpty()) {

            chatService.sendMessage(
                    sender,
                    receiver,
                    message.trim()
            );
        }

        // Return to conversation
        return "redirect:/chat/" + receiverId;
    }
}