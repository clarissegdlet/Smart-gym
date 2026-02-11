package com.smartgym.manager.domain;

import static com.smartgym.manager.domain.BookingTestSamples.*;
import static com.smartgym.manager.domain.MemberTestSamples.*;
import static com.smartgym.manager.domain.StravaAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartgym.manager.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Member.class);
        Member member1 = getMemberSample1();
        Member member2 = new Member();
        assertThat(member1).isNotEqualTo(member2);

        member2.setId(member1.getId());
        assertThat(member1).isEqualTo(member2);

        member2 = getMemberSample2();
        assertThat(member1).isNotEqualTo(member2);
    }

    @Test
    void stravaAccountTest() {
        Member member = getMemberRandomSampleGenerator();
        StravaAccount stravaAccountBack = getStravaAccountRandomSampleGenerator();

        member.setStravaAccount(stravaAccountBack);
        assertThat(member.getStravaAccount()).isEqualTo(stravaAccountBack);

        member.stravaAccount(null);
        assertThat(member.getStravaAccount()).isNull();
    }

    @Test
    void bookingTest() {
        Member member = getMemberRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        member.addBooking(bookingBack);
        assertThat(member.getBookings()).containsOnly(bookingBack);
        assertThat(bookingBack.getMember()).isEqualTo(member);

        member.removeBooking(bookingBack);
        assertThat(member.getBookings()).doesNotContain(bookingBack);
        assertThat(bookingBack.getMember()).isNull();

        member.bookings(new HashSet<>(Set.of(bookingBack)));
        assertThat(member.getBookings()).containsOnly(bookingBack);
        assertThat(bookingBack.getMember()).isEqualTo(member);

        member.setBookings(new HashSet<>());
        assertThat(member.getBookings()).doesNotContain(bookingBack);
        assertThat(bookingBack.getMember()).isNull();
    }
}
