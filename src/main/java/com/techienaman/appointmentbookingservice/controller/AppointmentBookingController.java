package com.techienaman.appointmentbookingservice.controller;

import com.techienaman.appointmentbookingservice.dto.ResponseWrapperDTO;
import com.techienaman.appointmentbookingservice.entity.Appointment;
import com.techienaman.appointmentbookingservice.exception.AppointmentNotFoundException;
import com.techienaman.appointmentbookingservice.exception.AppointmentTimeNotAvailableException;
import com.techienaman.appointmentbookingservice.exception.InvalidDataException;
import com.techienaman.appointmentbookingservice.service.AppointmentBookingService;
import com.techienaman.appointmentbookingservice.util.Constants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentBookingController {

    private final AppointmentBookingService bookingService;

    public AppointmentBookingController(AppointmentBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        try {
            var data = bookingService.bookAndSaveAppointment(appointment);
            return ResponseEntity.accepted().body(ResponseWrapperDTO.builder()
                    .status(HttpStatus.ACCEPTED)
                    .message(Constants.MSG_SUCCESS_BOOKING)
                    .info(data)
                    .build());
        } catch (AppointmentTimeNotAvailableException | InvalidDataException ex) {
            return ResponseEntity.badRequest().body(ResponseWrapperDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(ex.getMessage())
                    .build());
        }
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appointment,
                                               @PathVariable("appointmentId") Long Id) {
        try {
            var response = bookingService.updateBookedAppointment(Id, appointment);

            return ResponseEntity.ok(ResponseWrapperDTO.builder()
                    .status(HttpStatus.OK)
                    .message(Constants.MSG_UPDATE_SUCCESS)
                    .info(response)
                    .build());
        } catch (InvalidDataException | AppointmentTimeNotAvailableException ex) {
            return ResponseEntity.badRequest()
                    .body(ResponseWrapperDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(ex.getMessage())
                            .build());
        }
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointmentById(@PathVariable("appointmentId") Long Id) {
        try {
            return ResponseEntity.ok(ResponseWrapperDTO.builder()
                    .status(HttpStatus.OK)
                    .message(Constants.MSG_RETRIEVE_SUCCESS)
                    .info(bookingService.retrieveAppointmentById(Id))
                    .build());
        } catch (AppointmentNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseWrapperDTO.builder()
                            .status(HttpStatus.NOT_FOUND)
                            .message(ex.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("appointmentId") Long Id) {
        try {
            bookingService.deleteAppointmentById(Id);
            return ResponseEntity.ok(ResponseWrapperDTO.builder()
                    .status(HttpStatus.OK)
                    .message(String.format(Constants.MSG_DELETE_SUCCESS, Id))
                    .build());
        } catch (AppointmentNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseWrapperDTO.builder()
                            .status(HttpStatus.NOT_FOUND)
                            .message(ex.getMessage())
                            .build());
        }

    }

    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<?> fetchAppointmentsByDateRange(@PathVariable("startDate")
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                          LocalDate startDate,
                                                          @PathVariable("endDate")
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                          LocalDate endDate) {
        var appointments = bookingService
                .retrieveAppointmentsWithDateRange(startDate, endDate);

        return ResponseEntity.ok(ResponseWrapperDTO.builder()
                .status(HttpStatus.OK)
                .message(Constants.MSG_RETRIEVE_SUCCESS)
                .info(appointments)
                .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllAppointments() {
        return ResponseEntity.ok(ResponseWrapperDTO.builder()
                .status(HttpStatus.OK)
                .info(bookingService.findAllAppointments())
                .build());
    }

}
