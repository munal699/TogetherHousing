package org.example.togetherhousing.repository;

import org.example.togetherhousing.model.ChatMessage;
import org.example.togetherhousing.model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    // Get all messages between two users
    List<ChatMessage> findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtAsc(
            UserTbl sender1,
            UserTbl receiver1,
            UserTbl sender2,
            UserTbl receiver2
    );

    // Get unread messages received by a user
    List<ChatMessage> findByReceiverAndIsReadFalse(UserTbl receiver);
}