package com.yen.SpringReddit.service;

import com.yen.SpringReddit.po.NotificationEmail;

public interface MailService {

    void sendMail(NotificationEmail notificationEmail);
}
