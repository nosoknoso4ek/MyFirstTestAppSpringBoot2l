package ru.barmina.MyFirstTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import ru.barmina.MyFirstTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.barmina.MyFirstTestAppSpringBoot.exception.ValidationFailedException;
import ru.barmina.MyFirstTestAppSpringBoot.model.Request;


@Service
public interface ValidationService {

    void isValid(BindingResult bindingResult) throws  ValidationFailedException;
    void isUnsupportedCodeException(@RequestBody Request request) throws UnsupportedCodeException;


}
