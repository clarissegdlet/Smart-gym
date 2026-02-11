package com.smartgym.manager.service.mapper;

import static com.smartgym.manager.domain.StravaAccountAsserts.*;
import static com.smartgym.manager.domain.StravaAccountTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StravaAccountMapperTest {

    private StravaAccountMapper stravaAccountMapper;

    @BeforeEach
    void setUp() {
        stravaAccountMapper = new StravaAccountMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStravaAccountSample1();
        var actual = stravaAccountMapper.toEntity(stravaAccountMapper.toDto(expected));
        assertStravaAccountAllPropertiesEquals(expected, actual);
    }
}
