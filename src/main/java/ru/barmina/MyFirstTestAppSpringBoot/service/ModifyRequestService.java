package ru.barmina.MyFirstTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.barmina.MyFirstTestAppSpringBoot.model.Request;

@Service
public interface ModifyRequestService {
    void modify(Request request);
}

