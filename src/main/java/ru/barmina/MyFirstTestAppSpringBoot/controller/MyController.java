package ru.barmina.MyFirstTestAppSpringBoot.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import ru.barmina.MyFirstTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.barmina.MyFirstTestAppSpringBoot.exception.ValidationFailedException;

import ru.barmina.MyFirstTestAppSpringBoot.model.Codes;
import ru.barmina.MyFirstTestAppSpringBoot.model.ErrorCodes;
import ru.barmina.MyFirstTestAppSpringBoot.model.ErrorMessages;
import ru.barmina.MyFirstTestAppSpringBoot.service.ModifyRequestService;
import ru.barmina.MyFirstTestAppSpringBoot.service.ModifyResponseService;
import ru.barmina.MyFirstTestAppSpringBoot.service.ModifySystemTimeRequestService;
import ru.barmina.MyFirstTestAppSpringBoot.service.ValidationService;

import java.util.Date;


import org.springframework.beans.factory.annotation.Qualifier;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.barmina.MyFirstTestAppSpringBoot.model.Request;
import ru.barmina.MyFirstTestAppSpringBoot.model.Response;

@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;

    private final ModifyRequestService modifyRequestService;

    private final ModifyRequestService modifySystemTimeRequestService;

    @Autowired
    public MyController(ValidationService validationService,
        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
        @Qualifier("ModifySystemNameRequestService") ModifyRequestService modifyRequestService,
        @Qualifier("ModifySystemTimeRequestService") ModifyRequestService modifyDateTimeRequestService) {

        this.validationService = validationService;
        this.modifyResponseService=modifyResponseService;
        this.modifyRequestService = modifyRequestService;

        this.modifySystemTimeRequestService= modifyDateTimeRequestService;
    }
    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {
        log.info("request: {}", request);
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(ru.barmina.MyFirstTestAppSpringBoot.util.DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder("Validation errors: ");
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessages.append(String.format("[%s: %s] ", error.getField(), error.getDefaultMessage()));
            });
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);

            log.error("Validation failed: {}", errorMessages.toString());
            log.error("response after validation errors: {}", response);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            validationService.isValid(bindingResult);
            validationService.isUnsupportedCodeException(request);
        } catch (UnsupportedCodeException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.error("response unsupported exception: {}", response, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (ValidationFailedException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.error("response validation exception: {}", response, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.error("response unknown exception: {}", response, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        modifyResponseService.modify(response);
        log.info("response after modification: {}", response);

        modifyResponseService.modify(response);

        modifyRequestService.modify(request);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static class DateTimeUtil {
    }
}









