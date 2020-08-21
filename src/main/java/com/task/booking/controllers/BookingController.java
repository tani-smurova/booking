package com.task.booking.controllers;

import com.task.booking.models.Booking;
import com.task.booking.models.Resource;
import com.task.booking.models.User;
import com.task.booking.repo.BookingRepository;
import com.task.booking.repo.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Controller
public class BookingController {
    //Контроллер, отвечающий за бронирование ресурсов: вывод списка броней, добавление и удаления бронирований

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("booking-all")
    public String allBooking(@RequestParam(required = false, defaultValue = "") String fastFilter, Model model){
        Date today = new Date();
        LocalDate dateFilter = LocalDate.now().minusDays(7);
        Iterable <Booking> bookings = bookingRepository.findByDateBetween(
                Date.from(dateFilter.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),today);
        if (fastFilter != null && !fastFilter.isEmpty()){
            switch (fastFilter){
                case "week":
                    dateFilter = LocalDate.now().minusDays(7);
                    bookings = bookingRepository.findByDateBetween(
                            Date.from(dateFilter.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),today);
                    break;
                case "two_week":
                    dateFilter = LocalDate.now().minusDays(14);
                    bookings = bookingRepository.findByDateBetween(
                            Date.from(dateFilter.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),today);
                    break;
                case "month":
                    dateFilter = LocalDate.now().minusDays(30);
                    bookings = bookingRepository.findByDateBetween(
                            Date.from(dateFilter.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),today);
                    break;
                case "all":
                    bookings = bookingRepository.findAll();
                    break;
            }

        }
        model.addAttribute("bookings", bookings);
        return "booking-all";
    }

    @GetMapping ("booking-add")
    public String addBooking (Map<String, Object> model){
        Iterable <Resource> resources = resourceRepository.findAll();
        model.put("resources", resources);
        return "booking-add";
    }

    @PostMapping("booking-add")
    public String addPostBooking (@RequestParam Long idResource,
                                  @RequestParam Integer count,
                                  @RequestParam String date,
                                  @RequestParam String timeStart,
                                  @RequestParam String timeEnd,
                                  @AuthenticationPrincipal User user,
                                  @RequestParam String description)
            throws Exception{
        Booking book = new Booking();
        Resource res = resourceRepository.findById(idResource).orElseThrow();
        book.setResource(res);
        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        book.setDate(d);
        d = new SimpleDateFormat("HH:mm").parse(timeStart);
        book.setTimeStart(d);
        d = new SimpleDateFormat("HH:mm").parse(timeEnd);
        book.setTimeEnd(d);
        book.setCountPerson(count);
        book.setUser(user);
        book.setDescription(description);
        bookingRepository.save(book);
        return "redirect:/booking-all";
    }

    @PostMapping ("booking-remove-{id}")
    public String removePostBooking(@PathVariable(value = "id") Long id){
        Booking book = bookingRepository.findById(id).orElseThrow();
        bookingRepository.delete(book);
        return "redirect:/booking-all";
    }

}
