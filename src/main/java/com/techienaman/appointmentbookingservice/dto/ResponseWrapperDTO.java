package com.techienaman.appointmentbookingservice.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@Builder
public class ResponseWrapperDTO {
    private final HttpStatus status;
    private final String message;
    private final Object info;
}
