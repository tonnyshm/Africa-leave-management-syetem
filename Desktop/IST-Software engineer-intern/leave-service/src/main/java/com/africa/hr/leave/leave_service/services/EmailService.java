package com.africa.hr.leave.leave_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendLeaveSubmittedNotification(String to, String requesterEmail, String dates) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Leave Request Submitted");
        message.setText("A leave request has been submitted by " + requesterEmail + " for the period: " + dates);
        mailSender.send(message);
    }

    public void sendLeaveApprovedNotification(String to, String startDate, String endDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Leave Request is Approved");
        message.setText("Your leave from " + startDate + " to " + endDate + " has been approved.\nEnjoy your time!");
        mailSender.send(message);
    }

    public void sendLeaveRejectedNotification(String to, String comment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Leave Request was Rejected");
        message.setText("Your leave request was rejected with the following comment:\n\n" + comment);
        mailSender.send(message);
    }
}


