package com.yen.service;

// https://matthung0807.blogspot.com/2019/09/spring-data-jpa-pagination-and-sorting.html

import com.yen.bean.Message;
import com.yen.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public List<Message> getPageMessage(int page, int size) {

        Page<Message> pageResult = messageRepository.findAll(

                /**
                 *  page : query page, start from 0
                 *  size : record per page
                 *  Sort.by("createTime").descending()) : descending sort by create_Time
                 */
                PageRequest.of(page, size, Sort.by("createTime").descending())

                );

                pageResult.getNumberOfElements(); // record of current page 本頁筆數
                pageResult.getSize();             // record per page 每頁筆數
                pageResult.getTotalElements();    // total record 全部筆數
                pageResult.getTotalPages();       // total page 全部頁數

                List<Message> messageList =  pageResult.getContent();

                return messageList;
        }

}
