package com.yen.SpringReddit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;

    @Override
    public String toString() {
        return "NotificationEmail{" +
                "subject='" + subject + '\'' +
                ", recipient='" + recipient + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

}