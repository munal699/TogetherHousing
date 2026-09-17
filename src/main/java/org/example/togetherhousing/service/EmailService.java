package org.example.togetherhousing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:togetherhousing.notifications@gmail.com}")
    private String fromEmail;

    @Value("${spring.mail.password:}")
    private String mailPassword;

    @Value("${spring.mail.host:smtp.gmail.com}")
    private String mailHost;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send email with fail-safe error handling so SMTP offline/unconfigured doesn't break user flow.
     * Always displays email in server console so notifications are visible during testing.
     */
    @Async
    public void sendEmail(String to, String subject, String body) {
        if (to == null || to.trim().isEmpty()) {
            logger.warn("Skipping email send: recipient address is empty.");
            return;
        }

        // 1. Prominently display the email notification in the console
        System.out.println("\n========================================================================");
        System.out.println("📧 [SPRING MAIL NOTIFICATION DISPATCHED]");
        System.out.println("   To:       " + to);
        System.out.println("   From:     " + fromEmail);
        System.out.println("   Subject:  " + subject);
        System.out.println("------------------------------------------------------------------------");
        System.out.println(body);
        System.out.println("========================================================================\n");

        // 2. Attempt live SMTP internet delivery via JavaMailSender
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            if (fromEmail != null && !fromEmail.trim().isEmpty()) {
                message.setFrom(fromEmail);
            }
            message.setTo(to.trim());
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("✅ [GMAIL SMTP SUCCESS] Email successfully delivered to: " + to);
            logger.info("Successfully delivered live email via SMTP to {}", to);
        } catch (Exception ex) {
            System.err.println("⚠️ [GMAIL SMTP NOTICE] Failed to deliver email to " + to + ": " + ex.getMessage());
            System.err.println("   👉 Set your Gmail address & 16-character App Password in application.properties to receive live emails.");
            logger.warn("Live SMTP delivery to {} failed: {}. (Verify Gmail App Password in application.properties)", to, ex.getMessage());
        }
    }

    public void sendRegistrationEmail(String toEmail, String fullName) {
        String subject = "Welcome to Together Housing! Registration Confirmed";
        String body = String.format(
                "Hello %s,\n\n" +
                "Welcome to Together Housing! Your account has been successfully created.\n\n" +
                "You can now browse peer-funded shared housing properties, calculate flexible installment plans, " +
                "chat with sellers, and submit booking requests.\n\n" +
                "Best regards,\n" +
                "Together Housing Team\n" +
                "https://togetherhousing.com",
                fullName != null ? fullName : "User"
        );
        sendEmail(toEmail, subject, body);
    }

    public void sendLoginNotification(String toEmail, String fullName) {
        String subject = "Security Alert: New Login to Your Together Housing Account";
        String body = String.format(
                "Hello %s,\n\n" +
                "A new login to your Together Housing account was detected just now.\n" +
                "If this was you, you can safely ignore this notification.\n" +
                "If you did not perform this login, please contact support or secure your account immediately.\n\n" +
                "Best regards,\n" +
                "Together Housing Security Team",
                fullName != null ? fullName : "User"
        );
        sendEmail(toEmail, subject, body);
    }

    public void sendBookingCreatedNotification(String buyerEmail, String buyerName,
                                              String sellerEmail, String sellerName,
                                              String propertyTitle, Double downPayment, Double monthlyPayment) {
        // 1. Notify Buyer
        String buyerSubject = "Booking Request Submitted: " + propertyTitle;
        String buyerBody = String.format(
                "Hello %s,\n\n" +
                "Your booking request for '%s' has been successfully submitted to the seller!\n\n" +
                "Booking Details:\n" +
                "- Down Payment: $%,.2f\n" +
                "- Estimated Monthly Payment: $%,.2f\n" +
                "- Status: PENDING SELLER REVIEW\n\n" +
                "The seller has been notified. You can track this in your Buyer Dashboard.\n\n" +
                "Best regards,\n" +
                "Together Housing Team",
                buyerName, propertyTitle, downPayment != null ? downPayment : 0.0, monthlyPayment != null ? monthlyPayment : 0.0
        );
        sendEmail(buyerEmail, buyerSubject, buyerBody);

        // 2. Notify Seller
        if (sellerEmail != null && !sellerEmail.trim().isEmpty()) {
            String sellerSubject = "New Booking Request Received: " + propertyTitle;
            String sellerBody = String.format(
                    "Hello %s,\n\n" +
                    "Good news! Buyer %s has submitted a booking request for your property '%s'.\n\n" +
                    "Proposed Terms:\n" +
                    "- Down Payment: $%,.2f\n" +
                    "- Monthly Payment: $%,.2f\n\n" +
                    "Please visit your Seller Dashboard to review and confirm or reject this booking.\n\n" +
                    "Best regards,\n" +
                    "Together Housing Team",
                    sellerName != null ? sellerName : "Seller", buyerName, propertyTitle,
                    downPayment != null ? downPayment : 0.0, monthlyPayment != null ? monthlyPayment : 0.0
            );
            sendEmail(sellerEmail, sellerSubject, sellerBody);
        }
    }

    public void sendBookingStatusUpdate(String buyerEmail, String buyerName,
                                        String propertyTitle, String status, String noteOrReason) {
        String subject = String.format("Booking Request %s: %s", status, propertyTitle);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Hello %s,\n\n", buyerName));
        sb.append(String.format("Your booking request for '%s' has been updated to: %s.\n\n", propertyTitle, status));
        if (noteOrReason != null && !noteOrReason.trim().isEmpty()) {
            sb.append(String.format("Seller Note / Reason:\n\"%s\"\n\n", noteOrReason));
        }
        if ("CONFIRMED".equalsIgnoreCase(status)) {
            sb.append("Congratulations! The property owner has approved your reservation. Please visit your Buyer Dashboard to coordinate the next steps.\n\n");
        } else if ("REJECTED".equalsIgnoreCase(status)) {
            sb.append("You may browse other available shared properties in our catalog.\n\n");
        }
        sb.append("Best regards,\nTogether Housing Team");
        sendEmail(buyerEmail, subject, sb.toString());
    }
}
