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

    public static class ConversationSummary {
        private final UserTbl otherUser;
        private final ChatMessage lastMessage;
        private final long unreadCount;

        public ConversationSummary(UserTbl otherUser, ChatMessage lastMessage, long unreadCount) {
            this.otherUser = otherUser;
            this.lastMessage = lastMessage;
            this.unreadCount = unreadCount;
        }

        public UserTbl getOtherUser() {
            return otherUser;
        }

        public ChatMessage getLastMessage() {
            return lastMessage;
        }

        public long getUnreadCount() {
            return unreadCount;
        }
    }

    public List<ConversationSummary> getUserConversations(UserTbl user) {
        if (user == null) {
            return java.util.Collections.emptyList();
        }
        List<ChatMessage> allMessages = chatMessageRepository.findAllByUserOrderBySentAtDesc(user);
        java.util.Map<Integer, ConversationSummary> summaryMap = new java.util.LinkedHashMap<>();

        for (ChatMessage m : allMessages) {
            UserTbl other = m.getSender().getId().equals(user.getId()) ? m.getReceiver() : m.getSender();
            if (other == null) continue;

            if (!summaryMap.containsKey(other.getId())) {
                long unread = (!m.isRead() && m.getReceiver().getId().equals(user.getId())) ? 1 : 0;
                summaryMap.put(other.getId(), new ConversationSummary(other, m, unread));
            } else if (!m.isRead() && m.getReceiver().getId().equals(user.getId())) {
                ConversationSummary current = summaryMap.get(other.getId());
                summaryMap.put(other.getId(), new ConversationSummary(other, current.getLastMessage(), current.getUnreadCount() + 1));
            }
        }

        return new java.util.ArrayList<>(summaryMap.values());
    }

    public long getUnreadMessageCount(UserTbl user) {
        if (user == null) return 0;
        return chatMessageRepository.countByReceiverAndIsReadFalse(user);
    }
}