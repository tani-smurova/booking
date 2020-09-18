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

        //Если быстрый фильтр не задан, выводим по умолчанию бронирования на неделю
        if (fastFilter == null || fastFilter.isEmpty()) fastFilter = "week";

        switch (fastFilter){
            case "week":
                dateFilter = LocalDate.now().plusDays(7);
                bookings = bookingRepository.findByDateBetween(
                        today, Date.from(dateFilter.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                model.addAttribute("filter", "Показаны бронирования на неделю");
                break;
            case "two_week":
                dateFilter = LocalDate.now().plusDays(14);
                bookings = bookingRepository.findByDateBetween(
                        today, Date.from(dateFilter.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                model.addAttribute("filter", "Показаны бронирования на две недели");
                break;
            case "month":
                dateFilter = LocalDate.now().plusDays(30);
                bookings = bookingRepository.findByDateBetween(
                        today, Date.from(dateFilter.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                model.addAttribute("filter", "Показаны бронирования на месяц");
                break;
            case "all":
                bookings = bookingRepository.findAll();
                model.addAttribute("filter", "Показаны все бронирования");
                break;
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
                                  @RequestParam String description,
                                    Model model)
            throws Exception{
        Booking book = new Booking();
        Resource res = resourceRepository.findById(idResource).orElseThrow();
        book.setResource(res);
        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        Date tS = new SimpleDateFormat("HH:mm").parse(timeStart);
        Date tE = new SimpleDateFormat("HH:mm").parse(timeEnd);

        //Проверка доступности выбранного ресурса в указанный промежуток времени
        boolean available = true;
        Iterable<Booking> bookings = bookingRepository.findByResource(res);
        for (Booking bookFromDb: bookings){
            Date dFromDb = new Date(bookFromDb.getDate().getTime());
            Date tSFromDb = new Date(bookFromDb.getTimeStart().getTime());
            Date tEFromDb = new Date(bookFromDb.getTimeEnd().getTime());

            if (dFromDb.equals(d)){
                if ((tS.before(tSFromDb) & (tE.after(tSFromDb))) |
                        (tS.after(tSFromDb) & (tS.before(tEFromDb))) |
                        (tS.equals(tSFromDb))){
                            available=false;
                            model.addAttribute("message",
                                    "Ошибка при добавлении бронирования: ресурс занят");
                            break;
                        }
            }
        }
        if (available){
            book.setDate(d);
            book.setTimeStart(tS);
            book.setTimeEnd(tE);
            book.setCountPerson(count);
            book.setUser(user);
            book.setDescription(description);
            bookingRepository.save(book);
            model.addAttribute("message",
                    "Бронирование успешно добавлено");
        }

        return "booking-all";
    }

    @PostMapping ("booking-remove-{id}")
    public String removePostBooking(@PathVariable(value = "id") Long id){
        Booking book = bookingRepository.findById(id).orElseThrow();
        bookingRepository.delete(book);
        return "redirect:/booking-all";
    }

}
