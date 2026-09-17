package org.example.togetherhousing;

import org.example.togetherhousing.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailNotificationIntegrationTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@togetherhousing.com");
    }

    @Test
    void testRegistrationEmailSent() {
        emailService.sendRegistrationEmail("buyer@example.com", "John Doe");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();
        assertEquals("buyer@example.com", msg.getTo()[0]);
        assertEquals("noreply@togetherhousing.com", msg.getFrom());
        assertTrue(msg.getSubject().contains("Registration Confirmed"));
        assertTrue(msg.getText().contains("John Doe"));
    }

    @Test
    void testLoginNotificationEmailSent() {
        emailService.sendLoginNotification("buyer@example.com", "John Doe");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();
        assertEquals("buyer@example.com", msg.getTo()[0]);
        assertEquals("noreply@togetherhousing.com", msg.getFrom());
        assertTrue(msg.getSubject().contains("Security Alert"));
        assertTrue(msg.getText().contains("John Doe"));
    }

    @Test
    void testBookingCreatedDispatchesToBuyerAndSeller() {
        emailService.sendBookingCreatedNotification(
                "buyer@example.com", "John Doe",
                "seller@example.com", "Jane Seller",
                "Sunset Heights", 50000.0, 2500.0
        );

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(2)).send(captor.capture());

        var messages = captor.getAllValues();
        assertEquals(2, messages.size());

        // Message 1: to buyer
        assertEquals("buyer@example.com", messages.get(0).getTo()[0]);
        assertTrue(messages.get(0).getSubject().contains("Sunset Heights"));

        // Message 2: to seller
        assertEquals("seller@example.com", messages.get(1).getTo()[0]);
        assertTrue(messages.get(1).getSubject().contains("New Booking Request Received"));
    }

    @Test
    void testBookingStatusUpdateConfirmed() {
        emailService.sendBookingStatusUpdate(
                "buyer@example.com", "John Doe",
                "Sunset Heights", "CONFIRMED",
                "Reservation approved by seller"
        );

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();
        assertEquals("buyer@example.com", msg.getTo()[0]);
        assertTrue(msg.getSubject().contains("CONFIRMED"));
        assertTrue(msg.getText().contains("approved"));
    }

    @Test
    void testBookingStatusUpdateRejected() {
        emailService.sendBookingStatusUpdate(
                "buyer@example.com", "John Doe",
                "Sunset Heights", "REJECTED",
                "Down payment does not meet criteria"
        );

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();
        assertEquals("buyer@example.com", msg.getTo()[0]);
        assertTrue(msg.getSubject().contains("REJECTED"));
        assertTrue(msg.getText().contains("Down payment does not meet criteria"));
    }
}
