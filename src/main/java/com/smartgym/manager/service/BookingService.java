package com.smartgym.manager.service;

import com.smartgym.manager.domain.Booking;
import com.smartgym.manager.repository.BookingRepository;
import com.smartgym.manager.repository.ClassSessionRepository;
import com.smartgym.manager.service.dto.BookingDTO;
import com.smartgym.manager.service.mapper.BookingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.smartgym.manager.domain.Booking}.
 */
@Service
@Transactional
public class BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ClassSessionRepository classSessionRepository;

    public BookingService(
        BookingRepository bookingRepository,
        BookingMapper bookingMapper,
        ClassSessionRepository classSessionRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.classSessionRepository = classSessionRepository;
    }

    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingDTO save(BookingDTO bookingDTO) {
        LOG.debug("Request to save Booking : {}", bookingDTO);

        Booking booking = bookingMapper.toEntity(bookingDTO);

        // 🔹 1. Vérifier que la session existe
        if (booking.getClassSession() == null || booking.getClassSession().getId() == null) {
            throw new IllegalArgumentException("Class session must be provided");
        }

        Long classSessionId = booking.getClassSession().getId();

        // 🔹 2. Récupérer la session depuis la base
        var classSession = classSessionRepository
            .findById(classSessionId)
            .orElseThrow(() -> new IllegalArgumentException("Class session not found"));

        // 🔹 3. Compter les réservations existantes
        long currentBookings = bookingRepository.countByClassSession_Id(classSessionId);

        // 🔹 4. Vérifier la capacité
        if (currentBookings >= classSession.getCapacity()) {
            throw new IllegalStateException("This class session is full");
        }

        // 🔹 5. Sauvegarde normale si OK
        booking = bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    /**
     * Update a booking.
     */
    public BookingDTO update(BookingDTO bookingDTO) {
        LOG.debug("Request to update Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    /**
     * Partially update a booking.
     */
    public Optional<BookingDTO> partialUpdate(BookingDTO bookingDTO) {
        LOG.debug("Request to partially update Booking : {}", bookingDTO);

        return bookingRepository
            .findById(bookingDTO.getId())
            .map(existingBooking -> {
                bookingMapper.partialUpdate(existingBooking, bookingDTO);
                return existingBooking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    /**
     * Get all the bookings.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findAll() {
        LOG.debug("Request to get all Bookings");
        return bookingRepository
            .findAll()
            .stream()
            .map(bookingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one booking by id.
     */
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        LOG.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    /**
     * Delete the booking by id.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
    }
}
