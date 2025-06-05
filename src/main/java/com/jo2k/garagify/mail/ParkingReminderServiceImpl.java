package com.jo2k.garagify.mail;

import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
import com.jo2k.garagify.parking.persistence.repository.ParkingBorrowRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service("parkingReminderService")
@RequiredArgsConstructor
public class ParkingReminderServiceImpl implements ParkingReminderService {

    private final ParkingBorrowRepository parkingBorrowRepository;
    private final JavaMailSender mailSender;


    @Scheduled(fixedRate = 60000)
    @Transactional
    @Override
    public void runReminderScheduler() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime targetTime = now.plusMinutes(30000);

        List<ParkingBorrow> borrows = parkingBorrowRepository.findPendingReminders(now, targetTime);

        for (ParkingBorrow borrow : borrows) {
            sendEmailReminder(borrow.getUser().getEmail(), borrow.getReturnTime());
            borrow.setReminderSent(true);
        }

        parkingBorrowRepository.saveAll(borrows);
    }

    private void sendEmailReminder(String email, OffsetDateTime returnTime) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[Garagify] Przypomnienie o końcu wypożyczenia miejsca parkingowego");
            helper.setFrom("noreply@garagify.local");
            helper.setText(
                    "<html>" +
                            "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;'>" +
                            "  <div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05);'>" +
                            "    <h2 style='color: #333;'>Zbliża się koniec Twojego wypożyczenia</h2>" +
                            "    <p style='font-size: 16px; color: #555;'>Twoje wypożyczenie miejsca parkingowego kończy się o:</p>" +
                            "    <p style='font-size: 18px; font-weight: bold; color: #d9534f;'>" + returnTime + "</p>" +
                            "    <p style='font-size: 16px; color: #555;'>Prosimy o przygotowanie się do jego zakończenia.</p>" +
                            "    <hr style='margin: 20px 0;'/>" +
                            "    <p style='font-size: 12px; color: #999;'>Dziękujemy za skorzystanie z naszego systemu.</p>" +
                            "  </div>" +
                            "</body>" +
                            "</html>",
                    true
            );
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email reminder", e);
        }
    }
}
