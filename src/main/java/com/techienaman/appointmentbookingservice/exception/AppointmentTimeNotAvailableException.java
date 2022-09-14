package com.techienaman.appointmentbookingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppointmentTimeNotAvailableException extends RuntimeException {

    public AppointmentTimeNotAvailableException(String message) {
        super(message);
    }
}
