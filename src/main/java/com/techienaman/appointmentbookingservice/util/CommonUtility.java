package com.techienaman.appointmentbookingservice.util;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalTime;


@UtilityClass
public class CommonUtility {

    public String calculateAppointmentDuration(LocalTime startTime,
                                               LocalTime endTime) {

        Duration duration = Duration.between(startTime, endTime);

        return duration.toHoursPart() + "h " + duration.toMinutesPart() + "m";
    }


}
