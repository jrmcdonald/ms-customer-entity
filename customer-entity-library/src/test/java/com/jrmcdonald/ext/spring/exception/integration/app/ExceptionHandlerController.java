package com.jrmcdonald.ext.spring.exception.integration.app;

import com.jrmcdonald.ext.spring.exception.ConflictException;
import com.jrmcdonald.ext.spring.exception.NotFoundException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jrmcdonald.ext.spring.exception.integration.app.ExceptionHandlerController.ENDPOINT;

@RestController
@RequestMapping(ENDPOINT)
public class ExceptionHandlerController {

    public static final String ENDPOINT = "/v1/exception";

    @PostMapping(path = "/not-found", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void postWithNotFoundException() {
        throw new NotFoundException();
    }

    @PostMapping(path = "/already-exists", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void postWithConflictException() {
        throw new ConflictException();
    }

}
