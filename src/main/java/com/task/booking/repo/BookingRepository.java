package com.task.booking.repo;

import com.task.booking.models.Booking;
import com.task.booking.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByDateBetween(Date start, Date end);
    List<Booking> findByResource(Resource resource);
}
