package org.example.togetherhousing.service;

import lombok.RequiredArgsConstructor;
import org.example.togetherhousing.model.ChatMessage;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // Get conversation between two users
    public List<ChatMessage> getConversation(UserTbl user1, UserTbl user2) {

        return chatMessageRepository
                .findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtAsc(
                        user1,
                        user2,
                        user2,
                        user1
                );
    }

    // Send a new message
    public ChatMessage sendMessage(
            UserTbl sender,
            UserTbl receiver,
            String message
    ) {

        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setMessage(message);
        chatMessage.setSentAt(LocalDateTime.now());
        chatMessage.setRead(false);

        return chatMessageRepository.save(chatMessage);
    }

    // Mark received messages as read
    public void markMessagesAsRead(UserTbl sender, UserTbl receiver) {

        List<ChatMessage> messages =
                chatMessageRepository
                        .findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtAsc(
                                sender,
                                receiver,
                                receiver,
                                sender
                        );

        for (ChatMessage message : messages) {

            if (message.getReceiver().getId().equals(receiver.getId())) {
                message.setRead(true);
            }
        }

        chatMessageRepository.saveAll(messages);
    }

    // Get unread messages
    public List<ChatMessage> getUnreadMessages(UserTbl receiver) {
        return chatMessageRepository.findByReceiverAndIsReadFalse(receiver);
    }
}