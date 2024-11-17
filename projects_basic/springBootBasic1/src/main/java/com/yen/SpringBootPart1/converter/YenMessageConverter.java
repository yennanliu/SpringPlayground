package com.yen.SpringBootPart1.converter;

// https://www.youtube.com/watch?v=NEGzyvm1IBc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=42

import com.yen.SpringBootPart1.bean.Person2;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class YenMessageConverter implements HttpMessageConverter<Person2> {
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    /**
     *  server needs to know all content type that all MessageConverter can write
     *
     *  1) needs to handle application/x-yen type
     *
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return MediaType.parseMediaTypes("application/x-yen");
        //return MediaType.parseMediaTypes("x-yen");
    }

    @Override
    public Person2 read(Class<? extends Person2> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(Person2 person2, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        // define user-defined type
        String data = person2.getUserName() + ";" + person2.getAge() + ";" + person2.getBirth();

        // write out
        OutputStream body = outputMessage.getBody();
        body.write(data.getBytes());
    }
}
