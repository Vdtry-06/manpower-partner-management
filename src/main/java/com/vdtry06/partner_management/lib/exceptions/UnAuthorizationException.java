package com.vdtry06.partner_management.lib.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnAuthorizationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnAuthorizationException(String message) {
        super(message);
    }
}
