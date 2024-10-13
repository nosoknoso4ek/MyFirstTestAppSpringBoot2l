package ru.barmina.MyFirstTestAppSpringBoot.service;


import org.springframework.stereotype.Service;
import ru.barmina.MyFirstTestAppSpringBoot.model.Response;

@Service
public interface ModifyResponseService {

    Response modify(Response response);
}
