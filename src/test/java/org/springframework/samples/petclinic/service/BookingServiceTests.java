package org.springframework.samples.petclinic.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.repository.BookingRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BookingServiceTests {

	private static Booking booking;
	private static Booking newBooking;
	private static List<Booking> bookings;

	@Mock
	BookingRepository bookingRepository;

	@InjectMocks
	BookingService bookingService;

	@BeforeEach
	void data() {
		booking = new Booking();
		booking.setStartDate("3048/02/02");
		booking.setFinishDate("3048/04/04");

		bookings = new ArrayList<>();
		bookings.add(booking);
	}

	@ParameterizedTest
	@CsvSource({ "3048/05/05, 3048/05/05","3048/01/01, 3048/01/01" })
	void shouldInsertBooking(String startDate, String finishDate) {
		when(bookingRepository.findAll()).thenReturn(bookings);
		newBooking = new Booking();
		newBooking.setStartDate(startDate);
		newBooking.setFinishDate(finishDate);
		bookingService.saveBooking(newBooking);

		verify(bookingRepository, times(1)).save(any(Booking.class));
	}

	@ParameterizedTest
	@CsvSource({ "3048/03/03, 3048/03/03", "3048/02/02, 3048/02/02", "3048/04/04, 3048/05/05"  })
	void shouldNotInsertBookingOverlappingDates(String startDate, String finishDate) {
		when(bookingRepository.findAll()).thenReturn(bookings);
		newBooking = new Booking();
		newBooking.setStartDate(startDate);
		newBooking.setFinishDate(finishDate);
		bookingService.saveBooking(newBooking);

		verify(bookingRepository, times(0)).save(any(Booking.class));
	}

}
