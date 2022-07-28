package com.yen.SpringBlog.utils;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-1.html

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// TODO : check if this is necessary
/**
 *  to map Java's LocalDateTime typed attribtue to MySQL's datetime typed column,
 *  we need to convert between the two types (from LocalDateTime to TimeStamp and vice-versa).
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        if (localDateTime != null){
            return Timestamp.valueOf(localDateTime);
        }
        return null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        if (timestamp != null){
            return timestamp.toLocalDateTime();
        }
        return null;
    }

}
