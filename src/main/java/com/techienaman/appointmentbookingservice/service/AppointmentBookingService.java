package com.techienaman.appointmentbookingservice.service;

import com.techienaman.appointmentbookingservice.entity.Appointment;
import com.techienaman.appointmentbookingservice.exception.AppointmentNotFoundException;
import com.techienaman.appointmentbookingservice.exception.AppointmentTimeNotAvailableException;
import com.techienaman.appointmentbookingservice.exception.InvalidDataException;
import com.techienaman.appointmentbookingservice.repository.AppointmentBookingRepository;
import com.techienaman.appointmentbookingservice.util.CommonUtility;
import com.techienaman.appointmentbookingservice.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AppointmentBookingService {

    private final AppointmentBookingRepository bookingRepository;

    public AppointmentBookingService(AppointmentBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Appointment bookAndSaveAppointment(Appointment appointment) {

        var duration = CommonUtility.calculateAppointmentDuration(appointment.getStartTime(),
                appointment.getEndTime());
        appointment.setDuration(duration);

        if (appointment.getStartTime().isAfter(appointment.getEndTime())) {
            throw new InvalidDataException(Constants.MSG_INVALID_TIME);
        } else if (LocalDate.now().isAfter(appointment.getDate())) {
            throw new InvalidDataException(Constants.MSG_INVALID_DATE);
        }

        if (!isAvailable(appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime())) {
            throw new AppointmentTimeNotAvailableException(Constants.MSG_APPOINTMENT_NOT_AVAILABLE);
        }

        return bookingRepository.save(appointment);
    }

    @Transactional
    public Appointment updateBookedAppointment(Long appointmentId, Appointment appointment) {

        Optional<Appointment> appointmentDetails = Optional.ofNullable(bookingRepository
                .findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        String.format(Constants.MSG_APPOINTMENT_NOT_FOUND, appointmentId))));

        if (appointment.getDate() != null &&
                !Objects.equals(appointment.getDate(),
                        appointmentDetails.get().getDate())) {
            appointmentDetails.get().setDate(appointment.getDate());
        } else if ((appointment.getStartTime() != null &&
                appointment.getEndTime() != null) &&
                !Objects.equals(appointment.getStartTime(),
                        appointmentDetails.get().getStartTime()) &&
                !Objects.equals(appointment.getEndTime(), appointmentDetails.get().getEndTime())) {

            boolean available = isAvailable(appointmentDetails.get().getDate(), appointment.getStartTime(),
                    appointment.getEndTime());

            if (!available) {
                throw new AppointmentTimeNotAvailableException(Constants.MSG_APPOINTMENT_NOT_AVAILABLE);
            }

            appointmentDetails.get().setStartTime(appointment.getStartTime());
            appointmentDetails.get().setEndTime(appointment.getEndTime());

        } else if (appointment.getPurpose() != null &&
                !Objects.equals(appointment.getPurpose(),
                        appointmentDetails.get().getPurpose())) {
            appointmentDetails.get().setPurpose(appointment.getPurpose());
        }

        var duration = CommonUtility.calculateAppointmentDuration(appointmentDetails
                .get().getStartTime(), appointmentDetails.get().getEndTime());
        appointmentDetails.get().setDuration(duration);


        if (appointmentDetails.get().getStartTime().isAfter(appointmentDetails.get().getEndTime())) {
            throw new InvalidDataException(Constants.MSG_INVALID_TIME);
        } else if (LocalDate.now().isAfter(appointmentDetails.get().getDate())) {
            throw new InvalidDataException(Constants.MSG_INVALID_DATE);
        }

        return bookingRepository.save(appointmentDetails.get());
    }

    public Appointment retrieveAppointmentById(Long Id) {
        return bookingRepository.findById(Id)
                .orElseThrow(() ->
                        new AppointmentNotFoundException(String
                                .format(Constants.MSG_APPOINTMENT_NOT_FOUND, Id)));
    }

    public void deleteAppointmentById(Long Id) {
        Appointment appointment = bookingRepository.findById(Id)
                .orElseThrow(() -> new AppointmentNotFoundException(String
                        .format(Constants.MSG_APPOINTMENT_NOT_FOUND, Id)));
        bookingRepository.delete(appointment);
    }

    public List<Appointment> retrieveAppointmentsWithDateRange(LocalDate startDate,
                                                               LocalDate endDate) {
        return bookingRepository.findAppointmentByDateRange(startDate, endDate);
    }

    public List<Appointment> findAllAppointments() {
        return bookingRepository.findAll();
    }

    private boolean isAvailable(LocalDate date, LocalTime startTime, LocalTime endTime) {
        var appointments = bookingRepository
                .findAppointmentByDate(date);

        for (Appointment appointment : appointments) {

            if ((startTime.isBefore(appointment.getStartTime()) &&
                    endTime.isAfter(appointment.getStartTime())) ||
                    (startTime.isBefore(appointment.getEndTime()) &&
                            endTime.isAfter(appointment.getEndTime())) ||
                    (startTime.isBefore(appointment.getStartTime()) &&
                            endTime.isAfter(appointment.getEndTime())) ||
                    (startTime.isAfter(appointment.getStartTime()) &&
                            endTime.isBefore(appointment.getEndTime())) ||
                    (startTime.equals(appointment.getStartTime()) &&
                            endTime.equals(appointment.getEndTime())) ||
                    (startTime.equals(appointment.getStartTime()) &&
                            endTime.isBefore(appointment.getEndTime()))) {
                return false;
            }
        }
        return true;
    }

}
