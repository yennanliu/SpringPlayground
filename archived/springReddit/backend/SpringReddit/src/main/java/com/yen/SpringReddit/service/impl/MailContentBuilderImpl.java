package com.yen.SpringReddit.service.impl;

import com.yen.SpringReddit.service.MailContentBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
// @AllArgsConstructor // for TemplateEngine init
public class MailContentBuilderImpl implements MailContentBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public String build(String message) {

        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }

}
