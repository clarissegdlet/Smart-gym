package com.smartgym.manager.repository;

import com.smartgym.manager.domain.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Booking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByClassSession_Id(Long classSessionId);
    List<Booking> findByMember_IdOrderByBookingDateDesc(Long memberId);
}
