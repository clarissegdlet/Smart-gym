package com.smartgym.manager.web.rest;

import com.smartgym.manager.service.BookingService;
import com.smartgym.manager.service.dto.BookingDTO;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberBookingResource {

    private final BookingService bookingService;

    public MemberBookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}/bookings")
    public List<BookingDTO> getMemberBookings(@PathVariable Long id) {
        return bookingService.getBookingsForMember(id);
    }
}
