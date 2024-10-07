package ru.barmina.MyFirstTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.barmina.MyFirstTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.barmina.MyFirstTestAppSpringBoot.exception.ValidationFailedException;
import ru.barmina.MyFirstTestAppSpringBoot.model.Request;

@Service
public class RequestValidationService implements ValidationService {

    @Override

    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        if (bindingResult.hasErrors()) {
            throw new
                    ValidationFailedException(bindingResult.getFieldError(). toString());
        }
    }

    @Override
    public void isUnsupportedCodeException(Request request) throws UnsupportedCodeException {
        if (request.getUid().equals("123"))
            throw new UnsupportedCodeException("Неподдерживаемая ошибка");
    }
}
