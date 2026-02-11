package com.smartgym.manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StravaAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StravaAccount getStravaAccountSample1() {
        return new StravaAccount().id(1L).stravaUserId("stravaUserId1").accessToken("accessToken1");
    }

    public static StravaAccount getStravaAccountSample2() {
        return new StravaAccount().id(2L).stravaUserId("stravaUserId2").accessToken("accessToken2");
    }

    public static StravaAccount getStravaAccountRandomSampleGenerator() {
        return new StravaAccount()
            .id(longCount.incrementAndGet())
            .stravaUserId(UUID.randomUUID().toString())
            .accessToken(UUID.randomUUID().toString());
    }
}
