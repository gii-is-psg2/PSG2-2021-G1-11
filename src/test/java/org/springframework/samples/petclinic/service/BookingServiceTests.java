package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

	private static List<Booking> bookings;

	@Mock
	BookingRepository bookingRepository;

	@InjectMocks
	BookingService bookingService;

	@BeforeEach
	void data() {
		Booking booking = new Booking();
		booking.setStartDate("3048/02/02");
		booking.setFinishDate("3048/04/04");

		bookings = new ArrayList<>(Arrays.asList(booking));
	}

	@ParameterizedTest
	@CsvSource({ "3048/05/05, 3048/05/05", "3048/01/01, 3048/01/01" })
	void shouldInsertBooking(String startDate, String finishDate) {
		when(bookingRepository.findAllByCancelledFalse()).thenReturn(bookings);

		Booking newBooking = new Booking();
		newBooking.setStartDate(startDate);
		newBooking.setFinishDate(finishDate);
		bookingService.saveBooking(newBooking);

		verify(bookingRepository, times(1)).findAllByCancelledFalse();
		verify(bookingRepository, times(1)).save(newBooking);
		verifyNoMoreInteractions(bookingRepository);
	}
	
	@ParameterizedTest
	@CsvSource({ "3048/03/03, 3048/04/04" })
	void shouldInsertCancelledBooking(String startDate, String finishDate) {
		when(bookingRepository.findAllByCancelledFalse()).thenReturn(bookings);

		Booking newBooking = new Booking();
		newBooking.setStartDate(startDate);
		newBooking.setFinishDate(finishDate);
		newBooking.setCancelled(true);
		bookingService.saveBooking(newBooking);

		verify(bookingRepository, times(1)).save(newBooking);
		verifyNoMoreInteractions(bookingRepository);
	}

	@ParameterizedTest
	@CsvSource({ "3048/03/03, 3048/03/03", "3048/02/02, 3048/02/02", "3048/04/04, 3048/05/05" })
	void shouldNotInsertBookingOverlappingDates(String startDate, String finishDate) {
		when(bookingRepository.findAllByCancelledFalse()).thenReturn(bookings);
		Booking newBooking = new Booking();
		newBooking.setStartDate(startDate);
		newBooking.setFinishDate(finishDate);

		bookingService.saveBooking(newBooking);

		verify(bookingRepository, times(1)).findAllByCancelledFalse();
		verifyNoMoreInteractions(bookingRepository);
	}

	@ParameterizedTest
	@CsvSource({ "3048/05/05, 3048/06/06", "3049/01/01, 3049/01/01" })
	void shouldRenewBooking(String startDate, String finishDate) {
		when(bookingRepository.findAllByCancelledFalse()).thenReturn(bookings);

		Booking newBooking = new Booking();
		newBooking.setStartDate(startDate);
		newBooking.setFinishDate(finishDate);
		newBooking.setCancelled(true);
		assertTrue(bookingService.renewBooking(newBooking));

		verify(bookingRepository, times(1)).findAllByCancelledFalse();
		verify(bookingRepository, times(1)).save(newBooking);
		verifyNoMoreInteractions(bookingRepository);
	}

	@ParameterizedTest
	@CsvSource({ "3048/02/02, 3048/02/02", "3048/03/03, 3048/05/05" })
	void shouldNotRenewBooking(String startDate, String finishDate) {
		when(bookingRepository.findAllByCancelledFalse()).thenReturn(bookings);

		Booking newBooking = new Booking();
		newBooking.setStartDate(startDate);
		newBooking.setFinishDate(finishDate);
		newBooking.setCancelled(true);
		assertFalse(bookingService.renewBooking(newBooking));

		verify(bookingRepository, times(1)).findAllByCancelledFalse();
		verifyNoMoreInteractions(bookingRepository);
	}
}
