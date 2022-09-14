package com.techienaman.appointmentbookingservice.repository;

import com.techienaman.appointmentbookingservice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentBookingRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAppointmentByDate(LocalDate date);

    @Query("select a from Appointment as a where a.date between ?1 and ?2")
    List<Appointment> findAppointmentByDateRange(LocalDate startDate, LocalDate endDate);

}
