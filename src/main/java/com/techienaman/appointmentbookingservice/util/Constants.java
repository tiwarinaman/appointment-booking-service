package com.techienaman.appointmentbookingservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public final String MSG_SUCCESS_BOOKING = "Appointment Successfully Booked.";
    public final String MSG_APPOINTMENT_NOT_AVAILABLE = "Requested appointment time is not available";
    public final String MSG_INVALID_TIME = "Invalid data, start time can't be after end time";
    public final String MSG_INVALID_DATE = "Invalid data, date can't be in past";
    public final String MSG_APPOINTMENT_NOT_FOUND = "Requested appointment is not found with Id: %s";
    public final String MSG_RETRIEVE_SUCCESS = "Successfully retrieved.";
    public final String MSG_DELETE_SUCCESS = "Successfully deleted with appointment Id: %s";
    public final String MSG_UPDATE_SUCCESS = "Appointment successfully updated.";
}
