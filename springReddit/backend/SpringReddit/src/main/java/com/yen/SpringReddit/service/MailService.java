package com.yen.SpringReddit.service;

import com.yen.SpringReddit.model.NotificationEmail;

public interface MailService {

    void sendMail(NotificationEmail notificationEmail);
}
